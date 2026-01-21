package com.abc.itbbs.order.util;

import com.abc.itbbs.order.domain.enums.OrderBizEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderUtils {

    public static String getOrderSn(Integer biz) {
        // 时间部分：精确到秒
        String timePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 随机数部分：5位
        String randomPart = String.format("%05d", new Random().nextInt(100000));

        return OrderBizEnum.getDescByBiz(biz) + timePart + randomPart;
    }
}
