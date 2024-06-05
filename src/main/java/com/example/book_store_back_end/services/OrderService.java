package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUid(long id);
    ResponseDto<Long> createOrder(OrderDto orderDto);
    List<OrderDto> findOrdersByBookNameLike(String bookName, long uid);
    List<OrderDto> findOrdersByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, long uid);
}
