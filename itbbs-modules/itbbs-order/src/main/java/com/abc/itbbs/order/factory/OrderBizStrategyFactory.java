package com.abc.itbbs.order.factory;

import com.abc.itbbs.common.core.util.AssertUtils;
import com.abc.itbbs.order.domain.enums.OrderBizEnum;
import com.abc.itbbs.order.strategy.order.OrderBizStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBizStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, OrderBizStrategy> ORDER_BIZ_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(OrderBizStrategy.class).forEach((k, v)->{
            ORDER_BIZ_MAP.put(OrderBizEnum.getTypeByClass(k), v);
        });
    }

    public static OrderBizStrategy getOrderBizStrategy(Integer loadType){
        AssertUtils.isNotEmpty(loadType, "订单业务不能为空");
        OrderBizStrategy orderBizStrategy = ORDER_BIZ_MAP.get(loadType);
        AssertUtils.isNotEmpty(orderBizStrategy, "订单业务不存在");

        return orderBizStrategy;
    }


}
