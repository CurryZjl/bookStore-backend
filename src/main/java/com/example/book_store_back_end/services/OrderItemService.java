package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.SalesDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemService {
    List<SalesDto> findSalesByCreateOnBetween(
            LocalDateTime startTime,
            LocalDateTime endTime);
}
