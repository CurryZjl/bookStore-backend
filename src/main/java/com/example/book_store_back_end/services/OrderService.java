package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.ConsumptionDto;
import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.dto.StatBookDto;
import com.example.book_store_back_end.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Page<OrderDto> getOrdersByUid(long id, Pageable pageable);
    Long createOrder(OrderDto orderDto);
    Page<OrderDto> findOrdersByBookNameLike(String bookName, long uid, Pageable pageable);
    Page<OrderDto> findOrdersByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, long uid, Pageable pageable);
    Page<OrderDto> findAllOrders(Pageable pageable);

    Page<OrderDto> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<OrderDto> findAllByBookNameLike(String bookName, Pageable pageable);

    List<StatBookDto> findBooksPurchasedByUserInTimeRange(long uid,LocalDateTime startTime, LocalDateTime endTime);

    List<ConsumptionDto> findUserConsumptionInTimeRange(LocalDateTime startTime,LocalDateTime endTime);
}
