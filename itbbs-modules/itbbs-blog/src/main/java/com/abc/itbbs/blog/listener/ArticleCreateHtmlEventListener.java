package com.abc.itbbs.blog.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abc.itbbs.api.system.domain.vo.FileVO;
import com.abc.itbbs.blog.constant.TemplateConstants;
import com.abc.itbbs.blog.domain.entity.Article;
import com.abc.itbbs.blog.service.ArticleService;
import com.abc.itbbs.blog.service.TemplateService;
import com.abc.itbbs.common.core.constant.FileSuffixConstants;
import com.abc.itbbs.common.core.module.threadlocal.ThreadLocalTempVar;
import com.abc.itbbs.common.mq.constant.RabbitMQConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    /**
     * 文章发布页面静态化事件监听
     */
    @RabbitHandler
    public void handleArticleHtmlEvent(Article article, Message message, Channel channel) throws IOException {
        log.info("===开始消费文章页面静态化===");
        try {
            ThreadLocalTempVar.setTempTokenVar(message.getMessageProperties().getHeader(tokenHeader));

            FileVO fileVO = templateService.saveStaticToOss(article.getArticleId() + "_" + article.getVer() + FileSuffixConstants.HTML,
                    TemplateConstants.ARTICLE_TEMPLATE,
                    BeanUtil.beanToMap(article, false, true)
            );

            articleService.updateHtmlFilePathByArticleId(article.getArticleId(), fileVO.getFilePath());

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.error("页面静态化出错：{}", e.getMessage(), e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        } finally {
            ThreadLocalTempVar.removeTempTokenVar();
        }
        log.info("===完成消费文章页面静态化===");
    }

}
