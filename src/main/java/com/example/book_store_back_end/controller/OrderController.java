package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.OrderService;
import com.example.book_store_back_end.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseDto<List<OrderDto>> getOrders(){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        try {
            List<OrderDto> orderDtos = orderService.getOrdersByUid(uid);
            return new ResponseDto<>(true,"GET OK",orderDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
    }

    @PostMapping
    public ResponseDto<OrderDto> postOrders(@RequestBody OrderDto orderInfo){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        orderInfo.setUid(uid);
        long newOid = orderService.createOrder(orderInfo);
        if(newOid != -1){
            /* 成功创建订单信息 */
            return new ResponseDto<>(true, "订单提交成功", orderInfo);
        }else{
            /* 订单信息创建失败 */
            return new ResponseDto<>(false,"订单提交错误", orderInfo);
        }

    }
}
