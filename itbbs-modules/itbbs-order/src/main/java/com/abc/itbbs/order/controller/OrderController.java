package com.abc.itbbs.order.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderDTO;
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
    public ApiResult<Void> saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.saveOrder(orderDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除订单")
    @DeleteMapping
    public ApiResult<Void> deleteOrder(@RequestBody OrderDTO orderDTO) {
        orderService.deleteOrder(orderDTO);

        return ApiResult.success();
    }

}
