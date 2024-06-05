package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.dto.StatItemDto;
import com.example.book_store_back_end.dto.TimeDto;
import com.example.book_store_back_end.services.OrderService;
import com.example.book_store_back_end.utils.OrderStatUtils;
import com.example.book_store_back_end.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/book")
    public ResponseDto<List<OrderDto>> getOrdersByBookName(@RequestParam String bookName){
        long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        try{
            List<OrderDto> orderDtos = orderService.findOrdersByBookNameLike(bookName, uid);
            return new  ResponseDto<>(true,"GET OK",orderDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
    }

    @PostMapping("/time")
    public ResponseDto<List<OrderDto>> getOrdersByTime(@RequestBody TimeDto timeDto){
        long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(timeDto.getStartTime() + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(timeDto.getEndTime() + "T00:00:00", formatter);
            List<OrderDto> orderDtos = orderService.findOrdersByCreateOnBetween(sTime,eTime, uid);
            return new  ResponseDto<>(true,"GET OK",orderDtos);
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
        ResponseDto<Long> responseDto = orderService.createOrder(orderInfo);
        long newOid = responseDto.resource();
        if(newOid != -1){
            /* 成功创建订单信息 */
            return new ResponseDto<>(true, responseDto.message(), orderInfo);
        }else{
            /* 订单信息创建失败 */
            return new ResponseDto<>(false, responseDto.message(), orderInfo);
        }

    }

    @GetMapping("/stat")
    public ResponseDto<StatItemDto> getAllStatItem(){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        List<OrderDto> orderDtos;
        try {
            orderDtos = orderService.getOrdersByUid(uid);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return new ResponseDto<>(false, "GET ERROR", null);
        }

        StatItemDto statItemDto = OrderStatUtils.calculateUserOrderStatistics(orderDtos);
        return new ResponseDto<>(true,"GET OK", statItemDto);
    }

    @PostMapping("/stat")
    public ResponseDto<StatItemDto> getStatItemByTime(@RequestBody TimeDto timeDto){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        List<OrderDto> orderDtos;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(timeDto.getStartTime() + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(timeDto.getEndTime() + "T23:59:59", formatter);
            orderDtos = orderService.findOrdersByCreateOnBetween(sTime,eTime, uid);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }

        StatItemDto statItemDto = OrderStatUtils.calculateUserOrderStatistics(orderDtos);
        return new ResponseDto<>(true,"GET OK", statItemDto);
    }
}
