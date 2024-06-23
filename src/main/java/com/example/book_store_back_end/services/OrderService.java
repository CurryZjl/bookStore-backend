package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Page<OrderDto> getOrdersByUid(long id, Pageable pageable);
    List<OrderDto> getOrdersByUidNoPage(long id);
    ResponseDto<Long> createOrder(OrderDto orderDto);
    Page<OrderDto> findOrdersByBookNameLike(String bookName, long uid, Pageable pageable);
    Page<OrderDto> findOrdersByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, long uid, Pageable pageable);

    List<OrderDto> findOrdersByCreateOnBetweenNoPage(LocalDateTime startTime, LocalDateTime endTime, long uid);
    Page<OrderDto> findAllOrders(Pageable pageable);
    List<OrderDto> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime);

    Page<OrderDto> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<OrderDto> findAllByBookNameLike(String bookName, Pageable pageable);
}
