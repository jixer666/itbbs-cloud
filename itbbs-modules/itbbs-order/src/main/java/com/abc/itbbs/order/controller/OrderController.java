package com.abc.itbbs.order.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderDTO;
import com.abc.itbbs.order.domain.vo.OrderVO;
import com.abc.itbbs.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("查询订单分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getOrderPage(OrderDTO orderDTO) {
        PageResult orderPages = orderService.getOrderPageWithUiParam(orderDTO);

        return ApiResult.success(orderPages);
    }

    @ApiOperation("更新订单")
    @PutMapping
    public ApiResult<Void> updateOrder(@RequestBody OrderDTO orderDTO) {
        orderService.updateOrder(orderDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增订单")
    @PostMapping
    public ApiResult<Long> saveOrder(@RequestBody OrderDTO orderDTO) {
        Long orderId = orderService.saveOrder(orderDTO);

        return ApiResult.success(orderId);
    }

    @ApiOperation("删除订单")
    @DeleteMapping
    public ApiResult<Void> deleteOrder(@RequestBody OrderDTO orderDTO) {
        orderService.deleteOrder(orderDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增确认订单")
    @PostMapping("/confirm")
    public ApiResult<OrderVO> saveOrderConfirm(@RequestBody OrderDTO orderDTO) {
        OrderVO orderVO = orderService.saveOrderConfirm(orderDTO);

        return ApiResult.success(orderVO);
    }

    @ApiOperation("查询订单信息")
    @GetMapping("/info/{orderId}")
    public ApiResult<OrderVO> getOrderInfo(@PathVariable("orderId") Long orderId) {
        OrderVO orderVO = orderService.getOrderInfo(orderId);

        return ApiResult.success(orderVO);
    }


    @ApiOperation("测试")
    @GetMapping("/test")
    public ApiResult<String> test() {

        return ApiResult.success("ok");
    }
}
