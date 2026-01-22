package com.abc.itbbs.order.strategy.order;

import com.abc.itbbs.api.blog.ArticleServiceClient;
import com.abc.itbbs.api.blog.domain.entity.Article;
import com.abc.itbbs.api.blog.domain.enums.ArticleVisibleRangeEnum;
import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.order.constant.OrderConstants;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.domain.entity.Order;
import com.abc.itbbs.order.domain.enums.OrderBizEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleOrderBizStrategy implements OrderBizStrategy {

    @Autowired
    private ArticleServiceClient articleServiceClient;

    @Override
    public void checkAndFillCreateParams(OrderDTO orderDTO) {
        AssertUtils.isTrue(OrderBizEnum.ARTICLE.getBiz().equals(orderDTO.getOrderType()), "订单业务不正确");

        List<Long> articleIds = orderDTO.getOrderItemList().stream().map(OrderItemDTO::getProductId).collect(Collectors.toList());
        Map<Long, Article> articleMap = ApiResult.invokeRemoteMethod(
                articleServiceClient.getArticleMapByIds(articleIds)
        );
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItemList()) {
            Article article = articleMap.get(orderItemDTO.getProductId());
            AssertUtils.isNotEmpty(article, "存在文章不存在，无法创建");
            AssertUtils.isTrue(ArticleVisibleRangeEnum.isChargeRange(article.getVisibleRange()), "存在文章不是收费类型，无法创建");

            orderItemDTO.setProductName(article.getTitle());
            orderItemDTO.setProductPrice(article.getPrice());
        }
    }

    @Override
    public void fillOrderSubmitParams(Order order, List<OrderItemDTO> orderItemList) {
//        todo 优惠券服务待做
//        if (Objects.nonNull(order.getCouponId())) {
//            order.setCouponAmount();
//        }

        BigDecimal totalAmount = new BigDecimal(OrderConstants.DEFAULT_PRICE);
        int totalGrowth = 0;
        for (OrderItemDTO orderItemDTO : orderItemList) {
            // 算出总金额和总成长值
            BigDecimal promotionAmount = orderItemDTO.getProductPrice().multiply(new BigDecimal(orderItemDTO.getProductQuantity().toString()));
            totalAmount = totalAmount.add(promotionAmount);
            totalGrowth += orderItemDTO.getPromotionAmount().intValue();
        }

        order.setGrowth(totalGrowth);
        order.setValidTime(OrderBizEnum.ARTICLE.getValidTime());
        order.setFreightAmount(OrderConstants.DEFAULT_FREIGHT_AMOUNT);
        order.setTotalAmount(totalAmount.subtract(order.getFreightAmount()).subtract(order.getCouponAmount()).subtract(order.getIntegrationAmount()));
    }

}
