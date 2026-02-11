package com.abc.itbbs.order.service;

import com.abc.itbbs.order.domain.dto.PaymentAsyncDTO;
import com.abc.itbbs.order.domain.vo.PaymentVO;

import javax.servlet.http.HttpServletRequest;

public interface PaymentService {

    PaymentVO getOrderPaymentInfo(String orderSn);

    Boolean rsaCheck(HttpServletRequest request);

    String paymentCallback(PaymentAsyncDTO paymentAsyncDTO);
}
