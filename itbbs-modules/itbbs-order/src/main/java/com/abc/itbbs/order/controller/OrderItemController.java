package com.abc.itbbs.order.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderItemDTO;
import com.abc.itbbs.order.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单明细控制器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Api(tags = "订单明细接口")
@RestController
@RequestMapping("/order/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @ApiOperation("查询订单明细分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getOrderItemPage(OrderItemDTO orderItemDTO) {
        PageResult orderItemPages = orderItemService.getOrderItemPageWithUiParam(orderItemDTO);

        return ApiResult.success(orderItemPages);
    }

    @ApiOperation("更新订单明细")
    @PutMapping
    public ApiResult<Void> updateOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        orderItemService.updateOrderItem(orderItemDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增订单明细")
    @PostMapping
    public ApiResult<Void> saveOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        orderItemService.saveOrderItem(orderItemDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除订单明细")
    @DeleteMapping
    public ApiResult<Void> deleteOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        orderItemService.deleteOrderItem(orderItemDTO);

        return ApiResult.success();
    }

}
