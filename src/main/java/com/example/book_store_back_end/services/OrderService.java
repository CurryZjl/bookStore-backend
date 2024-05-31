package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Order;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUid(long id);
    ResponseDto<Long> createOrder(OrderDto orderDto);
}
