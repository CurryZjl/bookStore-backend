package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.dto.*;
import com.example.book_store_back_end.services.OrderService;
import com.example.book_store_back_end.services.UserServive;
import com.example.book_store_back_end.utils.OrderStatUtils;
import com.example.book_store_back_end.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserServive userServive;

    @Autowired
    public OrderController(OrderService orderService, UserServive userServive) {
        this.orderService = orderService;
        this.userServive = userServive;
    }

    @GetMapping()
    public ResponseDto<Page<OrderDto>> getOrders( @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        try {
            Page<OrderDto> orderDtos = orderService.getOrdersByUid(uid, pageable);
            return new ResponseDto<>(true,"GET OK",orderDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
    }

    @GetMapping("/book")
    public ResponseDto<Page<OrderDto>> getOrdersByBookName(@RequestParam String bookName,
                                                           @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                                           @RequestParam(required = false, defaultValue = "10") int pageSize
    ){
        long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        try{
            Page<OrderDto> orderDtos = orderService.findOrdersByBookNameLike(bookName, uid, pageable);
            return new  ResponseDto<>(true,"GET OK",orderDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
    }

    @GetMapping("/time")
    public ResponseDto<Page<OrderDto>> getOrdersByTime(@RequestParam String startTime,
                                                       @RequestParam String endTime,
                                                       @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                                       @RequestParam(required = false, defaultValue = "10") int pageSize){
        long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(startTime + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(endTime + "T00:00:00", formatter);
            Page<OrderDto> orderDtos = orderService.findOrdersByCreateOnBetween(sTime,eTime, uid, pageable);
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
            orderDtos = orderService.getOrdersByUidNoPage(uid);
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
            orderDtos = orderService.findOrdersByCreateOnBetweenNoPage(sTime,eTime, uid);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }

        StatItemDto statItemDto = OrderStatUtils.calculateUserOrderStatistics(orderDtos);
        return new ResponseDto<>(true,"GET OK", statItemDto);
    }

    @GetMapping("/price")
    public ResponseDto<List<ConsumptionDto>> getUsersConsumption(@RequestParam String startTime,
                                                           @RequestParam String endTime
    ){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        UserRole role = SessionUtils.getCurrentRole();
        if(!role.equals(UserRole.ADMIN)){
            return new ResponseDto<>(false, "非管理员不可操作", null);
        }
        List<OrderDto> orderDtos;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(startTime + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(endTime + "T23:59:59", formatter);
            orderDtos = orderService.findAllByCreateOnBetween(sTime,eTime);
        }catch (Exception e){
            return new ResponseDto<>(false,e.getMessage(), null);
        }
        List<ConsumptionDto> consumptionDtos = OrderStatUtils.calculateConsumption(orderDtos);
        for(ConsumptionDto consumptionDto : consumptionDtos)
        {
            String name = this.userServive.findNameByUid(consumptionDto.getUid());
            consumptionDto.setName(name);
        }
        return new ResponseDto<>(true,"拿取消费情况成功", consumptionDtos);
    }

    @GetMapping("/all")
    public ResponseDto<Page<OrderDto>> getAllOrdersInDB( @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                                         @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                         @RequestParam(required = false, defaultValue = "") String startTime,
                                                         @RequestParam(required = false, defaultValue = "") String endTime,
                                                         @RequestParam(required = false, defaultValue = "") String bookName
                                                         ){
        UserRole role = SessionUtils.getCurrentRole();
        if(!role.equals(UserRole.ADMIN)){
            return new ResponseDto<>(false, "非管理员不可操作", null);
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<OrderDto> orderDtos;

        if(!bookName.isEmpty()){
            try{
                orderDtos = orderService.findAllByBookNameLike(bookName, pageable);
            }catch (Exception e){
                return new ResponseDto<>(false, e.getMessage(), null);
            }
        }

        else if( !startTime.isEmpty() && !endTime.isEmpty()){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime sTime = LocalDateTime.parse(startTime + "T00:00:00", formatter);
                LocalDateTime eTime = LocalDateTime.parse(endTime + "T23:59:59", formatter);
                orderDtos = orderService.findAllByCreateOnBetween(sTime, eTime, pageable);
            }catch (Exception e){
                return new ResponseDto<>(false, e.getMessage(), null);
            }
        }

        else try{
            orderDtos = orderService.findAllOrders(pageable);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage(), null);
        }
        return new ResponseDto<>(true, "拿取全部订单成功", orderDtos);
    }
}
