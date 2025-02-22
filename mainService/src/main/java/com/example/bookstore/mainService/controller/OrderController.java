package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.constants.KafkaConfig;
import com.example.bookstore.mainService.constants.UserRole;
import com.example.bookstore.mainService.dto.*;
import com.example.bookstore.mainService.services.OrderService;
import com.example.bookstore.mainService.services.UserServive;
import com.example.bookstore.mainService.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserServive userServive;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping()
    public ResponseDto<Page<OrderDto>> getOrders(@RequestParam(required = false, defaultValue = "0") int pageIndex,
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
            LocalDateTime eTime = LocalDateTime.parse(endTime + "T23:59:59", formatter);
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
        String data = null;
        try {
            data = objectMapper.writeValueAsString(orderInfo);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        if(data == null){
            return new ResponseDto<>(false, "接收订单信息失败", null);
        }
        kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC1, KafkaConfig.KAFKA_ORDER_KEY, data);
        return new ResponseDto<>(true, "成功接收订单等待处理", null);
    }

    @PostMapping("/stat")
    public ResponseDto<StatItemDto> getStatItemByTime(@RequestBody TimeDto timeDto){
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(timeDto.getStartTime() + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(timeDto.getEndTime() + "T23:59:59", formatter);
            List<StatBookDto> statBookDtos = orderService.findBooksPurchasedByUserInTimeRange(uid, sTime, eTime);
            StatItemDto statItemDto = StatItemDto.builder().books(statBookDtos).build();
            long nums = 0;
            long price = 0;
            for(StatBookDto book1: statBookDtos){
                nums += book1.getCount();
                price += book1.getPrice();
            }
            statItemDto.setBookNums(nums);
            statItemDto.setAllPrice(price);
            return new ResponseDto<>(true,"GET OK", statItemDto);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
        }
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
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(startTime + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(endTime + "T23:59:59", formatter);
            List<ConsumptionDto> consumptionDtos  = orderService.findUserConsumptionInTimeRange( sTime, eTime);
            for(ConsumptionDto consumptionDto : consumptionDtos)
            {
                String name = this.userServive.findNameByUid(consumptionDto.getUid());
                consumptionDto.setName(name);
            }
            return new ResponseDto<>(true,"拿取消费情况成功", consumptionDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,e.getMessage(), null);
        }
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
