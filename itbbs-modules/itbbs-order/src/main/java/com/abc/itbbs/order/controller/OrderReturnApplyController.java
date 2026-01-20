package com.abc.itbbs.order.controller;

import com.abc.itbbs.common.core.domain.vo.ApiResult;
import com.abc.itbbs.common.core.domain.vo.PageResult;
import com.abc.itbbs.order.domain.dto.OrderReturnApplyDTO;
import com.abc.itbbs.order.service.OrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单退款申请控制器
 *
 * @author LiJunXi
 * @date 2026-01-20
 */
@Api(tags = "订单退款申请接口")
@RestController
@RequestMapping("/order/orderReturnApply")
public class OrderReturnApplyController {

    @Autowired
    private OrderReturnApplyService orderReturnApplyService;

    @ApiOperation("查询订单退款申请分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getOrderReturnApplyPage(OrderReturnApplyDTO orderReturnApplyDTO) {
        PageResult orderReturnApplyPages = orderReturnApplyService.getOrderReturnApplyPageWithUiParam(orderReturnApplyDTO);

        return ApiResult.success(orderReturnApplyPages);
    }

    @ApiOperation("更新订单退款申请")
    @PutMapping
    public ApiResult<Void> updateOrderReturnApply(@RequestBody OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyService.updateOrderReturnApply(orderReturnApplyDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增订单退款申请")
    @PostMapping
    public ApiResult<Void> saveOrderReturnApply(@RequestBody OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyService.saveOrderReturnApply(orderReturnApplyDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除订单退款申请")
    @DeleteMapping
    public ApiResult<Void> deleteOrderReturnApply(@RequestBody OrderReturnApplyDTO orderReturnApplyDTO) {
        orderReturnApplyService.deleteOrderReturnApply(orderReturnApplyDTO);

        return ApiResult.success();
    }

}
