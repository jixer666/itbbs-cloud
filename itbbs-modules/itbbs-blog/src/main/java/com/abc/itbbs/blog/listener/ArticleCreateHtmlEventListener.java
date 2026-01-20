package com.abc.itbbs.blog.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.abc.itbbs.api.system.UserServiceClient;
import com.abc.itbbs.api.system.domain.entity.User;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.blog.constant.ArticleConstants;
import com.abc.itbbs.blog.constant.TemplateConstants;
import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.blog.domain.enums.ArticleVisibleRangeEnum;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.constant.FileSuffixConstants;
import com.abc.itbbs.common.core.constant.ServerConstants;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.module.threadlocal.ThreadLocalTempVar;
import com.abc.itbbs.common.core.util.BeanUtils;
import com.abc.itbbs.common.core.util.JsoupUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.abc.itbbs.common.redis.constant.CacheConstants;
import com.abc.itbbs.common.redis.util.RedisUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author LiJunXi
 * @date 2026/1/1
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQConstants.BLOG_ARTICLE_HTML_QUEUE)
public class ArticleCreateHtmlEventListener {

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 文章发布页面静态化事件监听
     */
    @RabbitHandler
    public void handleArticleHtmlEvent(Article article, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章页面静态化===");
        try {
            ThreadLocalTempVar.setTempTokenVar(message.getMessageProperties().getHeader(tokenHeader));

            Map<String, Object> articleContext = buildArticleHtmlContext(article);

            FileVO fileVO = templateService.saveStaticToOss(article.getArticleId() + "_" + article.getVer() + FileSuffixConstants.HTML,
                    TemplateConstants.ARTICLE_TEMPLATE,
                    articleContext
            );

            articleService.updateHtmlFilePathByArticleId(article.getArticleId(), fileVO.getFilePath());

            saveArticleCache(article);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("页面静态化出错：{}", e.getMessage(), e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        } finally {
            ThreadLocalTempVar.removeTempTokenVar();
        }
        log.info("===完成消费文章页面静态化===");
    }

    private Map<String, Object> buildArticleHtmlContext(Article article) {
        Map<String, Object> articleContext = BeanUtil.beanToMap(article, false, true);
        articleContext.put("tagDetailsList", JSONUtil.toList(article.getTagDetails(), String.class));

        Map<Long, User> userMap = ApiResult.invokeRemoteMethod(
                userServiceClient.getUserMapByUserIds(Arrays.asList(article.getUserId()))
        );
        // 用户信息
        articleContext.put("userInfo", userMap.get(article.getUserId()));
        // 预览内容
        Boolean isCharge = ArticleVisibleRangeEnum.isChargeRange(article.getVisibleRange());
        articleContext.put("previewContent", isCharge ?
                JsoupUtils.truncateHtml(article.getContent(), ArticleConstants.ARTICLE_PREVIEW_COUNT) :
                StringUtils.EMPTY
        );
        if (isCharge) {
            // 若是收费内容，生成JSON格式的完整内容文件
            FileVO fileVO = templateService.saveJsonToOss(
                    article.getArticleId() + "_" + article.getVer() + FileSuffixConstants.HTML,
                    MapUtil.of("content", article.getContent()));
            articleContext.put("fullContentRequestPath", ServerConstants.OSS_SERVICE + "/article/" + fileVO.getFilePath());
        }

        return articleContext;
    }


    private void saveArticleCache(Article article) {
        // 添加最新文章缓存
        RedisUtils.zAdd(CacheConstants.getFinalKey(CacheConstants.ARTICLE_NEW_LIST),
                article.getArticleId().toString(),
                article.getCreateTime().getTime() * -1);

        // 添加文章详情缓存
        RedisUtils.hmsetByStrMap(CacheConstants.getFinalKey(CacheConstants.ARTICLE_HASH_INFO, article.getArticleId()),
                BeanUtils.convertToMap(article),
                CacheConstants.ARTICLE_INFO_EXPIRE_TIME);
    }

}
