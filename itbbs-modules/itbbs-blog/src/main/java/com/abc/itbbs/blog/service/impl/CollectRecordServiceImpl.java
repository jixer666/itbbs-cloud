package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.enums.CollectBizEnum;
import com.abc.itbbs.blog.factory.CollectRecordStrategyFactory;
import com.abc.itbbs.blog.strategy.collectrecord.CollectRecordStrategy;
import com.abc.itbbs.blog.util.BlogRedisUtils;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.blog.convert.CollectRecordConvert;
import com.abc.itbbs.blog.domain.dto.CollectRecordDTO;
import com.abc.itbbs.blog.domain.entity.CollectRecord;
import com.abc.itbbs.blog.domain.vo.CollectRecordVO;
import com.abc.itbbs.blog.mapper.CollectRecordMapper;
import com.abc.itbbs.blog.service.CollectRecordService;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.abc.itbbs.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 收藏记录业务处理
 *
 * @author LiJunXi
 * @date 2026-01-05
 */
@Slf4j
@Service
public class CollectRecordServiceImpl extends BaseServiceImpl<CollectRecordMapper, CollectRecord> implements CollectRecordService {

    @Autowired
    private CollectRecordMapper collectRecordMapper;
    
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageResult getCollectRecordPageWithUiParam(CollectRecordDTO collectRecordDTO) {
        startPage();
        List<CollectRecord> collectRecords = collectRecordMapper.selectCollectRecordList(collectRecordDTO);
        List<CollectRecordVO> collectRecordVOList = pageList2CustomList(collectRecords, (List<CollectRecord> list) -> {
            return BeanUtil.copyToList(list, CollectRecordVO.class);
        });

        return buildPageResult(collectRecordVOList);
    }

    @Override
    public void updateCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkUpdateParams();
        CollectRecord collectRecord = collectRecordMapper.selectById(collectRecordDTO.getCollectRecordId());
        AssertUtils.isNotEmpty(collectRecord, "收藏记录不存在");
        BeanUtils.copyProperties(collectRecordDTO, collectRecord);
        collectRecordMapper.updateById(collectRecord);
    }

    @Override
    public void saveCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkSaveParams();
        CollectRecord collectRecord = CollectRecordConvert.buildDefaultCollectRecordByCollectRecordDTO(collectRecordDTO);
        collectRecordMapper.insert(collectRecord);
    }

    @Override
    public void deleteCollectRecord(CollectRecordDTO collectRecordDTO) {
        collectRecordDTO.checkDeleteParams();

        collectRecordMapper.deleteBatchIds(collectRecordDTO.getCollectRecordIds());
    }

    @Override
    public void increaseRecordRecord(Long targetId, Integer biz) {
        AssertUtils.isNotEmpty(targetId, "收藏目标对象ID不能为空");
        AssertUtils.isNotEmpty(biz, "收藏业务不能为空");
        CollectRecordStrategy collectRecordStrategy = CollectRecordStrategyFactory.getCollectRecordStrategy(biz);
        AssertUtils.isNotEmpty(collectRecordStrategy, "收藏业务不存在");

        checkAndRebuildLikeCountCache(targetId, biz, collectRecordStrategy);

        String collectCountKey = collectRecordStrategy.getCountCacheKey(targetId);
        String userCollectSetKey = CacheConstants.getFinalKey(CacheConstants.USER_COLLECT_SET, SecurityUtils.getUserId(), biz);

        try {
            BlogRedisUtils.toggleLikeOrCollectByLua(
                    collectCountKey, userCollectSetKey,
                    collectRecordStrategy.getWaitDoTask(),
                    CacheConstants.getFinalKey(CacheConstants.COLLECT_RECORD_WAIT_DO_TASK),
                    targetId, SecurityUtils.getUserId(),
                    CacheConstants.USER_COLLECT_SET_MAX_LENGTH,
                    CacheConstants.ARTICLE_COLLECT_COUNT_EXPIRE_TIME,
                    biz,
                    new Date().getTime());
        } catch (Exception e) {
            log.error("执行保存收藏Lua脚本出错", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.BIZ_ERROR.getCode(), "执行收藏出错");
        }

    }

    private void checkAndRebuildLikeCountCache(Long targetId, Integer biz, CollectRecordStrategy collectRecordStrategy) {
        String articleLikeCountKey = collectRecordStrategy.getWaitDoTask();
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_COLLECT_SET, SecurityUtils.getUserId(), biz);

        Long isExist = BlogRedisUtils.checkArticleLikeCountKeyExist(articleLikeCountKey, userLikeSetKey);
        if (Objects.nonNull(isExist) && isExist != -1) {
            return;
        }

        // 需要重建缓存
        rebuildCollectCountCache(targetId, collectRecordStrategy);
        rebuildUserCollectSetCache(targetId);
    }

    private void rebuildUserCollectSetCache(Long targetId) {
        AssertUtils.isNotEmpty(targetId, "收藏目标对象ID不能为空");
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_COLLECT_SET, SecurityUtils.getUserId(), CollectBizEnum.ARTICLE.getBiz());

        // 第一层检查
        if (RedisUtils.hasKey(userLikeSetKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.USER_COLLECT_SET_LOCK, targetId));
        try {
            // 等待5s，锁持有10s
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return;
            }

            // 第二层检查
            if (RedisUtils.hasKey(userLikeSetKey)) {
                return;
            }

            List<CollectRecord> collectRecordList = selectCollectTargetIdsByUserId(SecurityUtils.getUserId(), true);
            Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
            for (CollectRecord collectRecord : collectRecordList) {
                tuples.add(new DefaultTypedTuple<>(collectRecord.getTargetId().toString(), collectRecord.getCreateTime().getTime() * 1.0));
            }
            // 防止缓存穿透
            tuples.add(new DefaultTypedTuple<>(CommonConstants.ZERO.toString(), Double.MAX_VALUE));
            RedisUtils.zAdd(userLikeSetKey, tuples);

        } catch (Exception e) {
            log.error("用户收藏集合重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void rebuildCollectCountCache(Long targetId, CollectRecordStrategy collectRecordStrategy) {
        AssertUtils.isNotEmpty(targetId, "收藏目标对象ID不能为空");
        String articleLikeCountKey = collectRecordStrategy.getCountCacheKey(targetId);

        // 第一层检查
        if (RedisUtils.hasKey(articleLikeCountKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(collectRecordStrategy.getCountLockCacheKey(targetId));
        try {
            // 等待5s，锁持有10s
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return;
            }

            // 第二层检查
            if (RedisUtils.hasKey(articleLikeCountKey)) {
                return;
            }

            collectRecordStrategy.saveCollectRecordCache(targetId);

        } catch (Exception e) {
            log.error("点赞重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<CollectRecord> selectCollectTargetIdsByUserId(Long userId, Boolean isLimit) {
        AssertUtils.isNotEmpty(userId, "用户ID不存在");

        List<CollectRecord> collectRecords = collectRecordMapper.selectCollectTargetIdsByUserId(userId, isLimit);

        Map<Long, List<CollectRecord>> collectMap = collectRecords.stream().collect(Collectors.groupingBy(CollectRecord::getUserId));

        return collectMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() % 2 != 0) // 可以重复收藏，所以单数就代表收藏，双数就代表已取消
                .map(entry -> {
                    List<CollectRecord> collectRecordTemps = collectMap.get(entry.getKey());
                    return collectRecordTemps.get(collectRecordTemps.size() - 1);
                })
                .collect(Collectors.toList());
    }
}