package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.constants.UserRole;
import com.example.bookstore.mainService.dto.ResponseDto;
import com.example.bookstore.mainService.dto.SalesDto;
import com.example.bookstore.mainService.services.OrderItemService;
import com.example.bookstore.mainService.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/sales")
    public ResponseDto<List<SalesDto>> getSales(@RequestParam String startTime,
                                                @RequestParam String endTime) {
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth ERROR", null);
        }
        UserRole role = SessionUtils.getCurrentRole();
        if(!role.equals(UserRole.ADMIN)){
            return new ResponseDto<>(false, "非管理员不可操作", null);
        }
        List<SalesDto> sortedSalesDtos;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime sTime = LocalDateTime.parse(startTime + "T00:00:00", formatter);
            LocalDateTime eTime = LocalDateTime.parse(endTime + "T23:59:59", formatter);
            List<SalesDto> salesDtos = orderItemService.findSalesByCreateOnBetween(sTime,eTime);
            sortedSalesDtos = salesDtos.stream()
                    .sorted(Comparator.comparingLong(SalesDto::getAmountAll).reversed())
                    .collect(Collectors.toList());
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage(), null);
        }
        return new ResponseDto<>(true, "拿取销量信息成功", sortedSalesDtos
        );
    }
}
