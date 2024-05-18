package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.dto.Message;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.services.OrderItemService;
import com.example.book_store_back_end.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }


    @GetMapping()
    public ResponseDto<List<OrderDto>> getOrders(){
        final long uid = 1L;
        try {
            List<OrderDto> orderDtos = orderService.getOrdersByUid(uid);
            return new ResponseDto<>(true,"GET OK",orderDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
    }

    @PostMapping
    public ResponseDto<OrderDto> postOrders(@RequestBody OrderDto orderInfo){
        //TODO::辨别身份
        orderInfo.setUid(1L);
        long newOid = orderService.createOrder(orderInfo);
        if(newOid != -1){
            /* 成功创建订单信息 */
            List<OrderItemDto> orderItemDtos = orderInfo.getOrderItems();
            orderItemDtos.stream().forEachOrdered(orderItemDto -> {
                orderItemDto.setOid(newOid);
            });
            this.orderItemService.createOrderItems(orderItemDtos);
            return new ResponseDto<>(true, "订单提交成功", orderInfo);
        }else{
            /* 订单信息创建失败 */
            return new ResponseDto<>(false,"订单提交错误", orderInfo);
        }

    }
}
