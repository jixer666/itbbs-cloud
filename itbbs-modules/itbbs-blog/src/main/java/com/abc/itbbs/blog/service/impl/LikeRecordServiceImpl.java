package com.abc.itbbs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.blog.domain.enums.LikeBizEnum;
import com.abc.itbbs.blog.factory.LikeRecordStrategyFactory;
import com.abc.itbbs.blog.strategy.likerecord.LikeRecordStrategy;
import com.abc.itbbs.blog.util.BlogRedisUtils;
import com.abc.itbbs.common.core.constant.CommonConstants;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.service.BaseServiceImpl;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.blog.convert.LikeRecordConvert;
import com.abc.itbbs.blog.domain.dto.LikeRecordDTO;
import com.abc.itbbs.blog.domain.entity.LikeRecord;
import com.abc.itbbs.blog.domain.vo.LikeRecordVO;
import com.abc.itbbs.blog.mapper.LikeRecordMapper;
import com.abc.itbbs.blog.service.LikeRecordService;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.util.AssertUtils;
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
 * 点赞记录业务处理
 *
 * @author LiJunXi
 * @date 2026-01-04
 */
@Slf4j
@Service
public class LikeRecordServiceImpl extends BaseServiceImpl<LikeRecordMapper, LikeRecord> implements LikeRecordService {

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageResult getLikeRecordPageWithUiParam(LikeRecordDTO likeRecordDTO) {
        startPage();
        List<LikeRecord> likeRecords = likeRecordMapper.selectLikeRecordList(likeRecordDTO);
        List<LikeRecordVO> likeRecordVOList = pageList2CustomList(likeRecords, (List<LikeRecord> list) -> {
            return BeanUtil.copyToList(list, LikeRecordVO.class);
        });

        return buildPageResult(likeRecordVOList);
    }

    @Override
    public void updateLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkUpdateParams();
        LikeRecord likeRecord = likeRecordMapper.selectById(likeRecordDTO.getLikeRecordId());
        AssertUtils.isNotEmpty(likeRecord, "点赞记录不存在");
        BeanUtils.copyProperties(likeRecordDTO, likeRecord);
        likeRecordMapper.updateById(likeRecord);
    }

    @Override
    public void saveLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkSaveParams();
        LikeRecord likeRecord = LikeRecordConvert.buildDefaultLikeRecordByLikeRecordDTO(likeRecordDTO);
        try {
            likeRecordMapper.insert(likeRecord);
        } catch (Exception e) {
            log.error("保存点赞出错", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.BIZ_ERROR.getCode(), "保存点赞出错");
        }
    }

    @Override
    public void deleteLikeRecord(LikeRecordDTO likeRecordDTO) {
        likeRecordDTO.checkDeleteParams();

        likeRecordMapper.deleteBatchIds(likeRecordDTO.getLikeRecordIds());
    }

    @Override
    public List<LikeRecord> selectLikeTargetIdsByUserId(Long userId, Boolean isLimit) {
        AssertUtils.isNotEmpty(userId, "用户ID不存在");

        List<LikeRecord> likeRecords = likeRecordMapper.selectLikeTargetIdsByUserId(userId, isLimit);
        Map<Long, List<LikeRecord>> likeMap = likeRecords.stream().collect(Collectors.groupingBy(LikeRecord::getUserId));

        return likeMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() % 2 != 0) // 可以重复点赞，所以单数就代表点赞，双数就代表已取消
                .map(entry -> {
                    List<LikeRecord> likeRecordTemps = likeMap.get(entry.getKey());
                    return likeRecordTemps.get(likeRecordTemps.size() - 1);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void increaseLikeRecord(Long targetId, Integer biz) {
        AssertUtils.isNotEmpty(targetId, "点赞目标对象ID不能为空");
        AssertUtils.isNotEmpty(biz, "点赞业务不能为空");
        LikeRecordStrategy likeRecordStrategy = LikeRecordStrategyFactory.getLikeRecordStrategy(biz);
        AssertUtils.isNotEmpty(likeRecordStrategy, "点赞业务不存在");

        checkAndRebuildLikeCountCache(targetId, biz, likeRecordStrategy);

        String likeCountKey = likeRecordStrategy.getCountCacheKey(targetId);
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), biz);

//      方案一：全为redis操作，但无点赞操作
//        // 缓存点赞数量
//        RedisUtils.inc(likeCountKey, Math.toIntExact(CacheConstants.ARTICLE_LIKE_COUNT_EXPIRE_TIME), TimeUnit.HOURS);
//
//        // 缓存用户点赞列表
//        Long size = RedisUtils.sGetSetSize(userLikeSetKey);
//        if (size > CacheConstants.USER_LIKE_SET_MAX_LENGTH) {
//            // 超过了最大点赞长度，抛弃第一个
//            RedisUtils.zRemoveRange(userLikeSetKey, 0, 0);
//        }
//        RedisUtils.zAdd(userLikeSetKey, targetId, new Date().getTime());
//
//        // 点赞数加入到待同步集合
//        RedisUtils.zAdd(likeRecordStrategy.getWaitDoTask(), targetId, new Date().getTime());
//
//        // 点赞记录加入到待同步集合
//        LikeRecordDTO likeRecordDTO = LikeRecordConvert.buildDefaultLikeRecordDTO(targetId, biz);
//        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.LIKE_RECORD_WAIT_DO_TASK), JSONUtil.toJsonStr(likeRecordDTO), new Date().getTime());


//        方案二：改为Lua脚本，减少网络IO
        try {
            BlogRedisUtils.toggleLikeOrCollectByLua(
                    likeCountKey, userLikeSetKey,
                    likeRecordStrategy.getWaitDoTask(),
                    CacheConstants.getFinalKey(CacheConstants.LIKE_RECORD_WAIT_DO_TASK),
                    targetId, SecurityUtils.getUserId(),
                    CacheConstants.USER_LIKE_SET_MAX_LENGTH,
                    CacheConstants.ARTICLE_LIKE_COUNT_EXPIRE_TIME,
                    biz,
                    new Date().getTime());
        } catch (Exception e) {
            log.error("执行保存点赞Lua脚本出错", e.getMessage(), e);
            throw new GlobalException(BizCodeEnum.BIZ_ERROR.getCode(), "执行点赞出错");
        }
    }

    private void checkAndRebuildLikeCountCache(Long targetId, Integer biz, LikeRecordStrategy likeRecordStrategy) {
        String articleLikeCountKey = likeRecordStrategy.getWaitDoTask();
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), biz);

        Long isExist = BlogRedisUtils.checkArticleLikeCountKeyExist(articleLikeCountKey, userLikeSetKey);
        if (Objects.nonNull(isExist) && isExist != -1) {
            return;
        }

        // 需要重建缓存
        rebuildLikeCountCache(targetId, likeRecordStrategy);
        rebuildUserLikeSetCache(targetId);
    }

    private void rebuildUserLikeSetCache(Long targetId) {
        AssertUtils.isNotEmpty(targetId, "点赞目标对象ID不能为空");
        String userLikeSetKey = CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET, SecurityUtils.getUserId(), LikeBizEnum.ARTICLE.getBiz());

        // 第一层检查
        if (RedisUtils.hasKey(userLikeSetKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(CacheConstants.getFinalKey(CacheConstants.USER_LIKE_SET_LOCK, targetId));
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

            List<LikeRecord> likeRecords = selectLikeTargetIdsByUserId(SecurityUtils.getUserId(), true);
            Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
            for (LikeRecord likeRecord : likeRecords) {
                tuples.add(new DefaultTypedTuple<>(likeRecord.getTargetId().toString(), likeRecord.getCreateTime().getTime() * 1.0));
            }
            // 防止缓存穿透
            tuples.add(new DefaultTypedTuple<>(CommonConstants.ZERO.toString(), Double.MAX_VALUE));
            RedisUtils.zAdd(userLikeSetKey, tuples);

        } catch (Exception e) {
            log.error("用户点赞集合重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void rebuildLikeCountCache(Long targetId, LikeRecordStrategy likeRecordStrategy) {
        AssertUtils.isNotEmpty(targetId, "点赞目标对象ID不能为空");
        String articleLikeCountKey = likeRecordStrategy.getCountCacheKey(targetId);

        // 第一层检查
        if (RedisUtils.hasKey(articleLikeCountKey)) {
            return;
        }

        RLock lock = redissonClient.getLock(likeRecordStrategy.getCountLockCacheKey(targetId));
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

            likeRecordStrategy.saveLikeRecordCache(targetId);

        } catch (Exception e) {
            log.error("点赞重建加锁失败：{}", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

}