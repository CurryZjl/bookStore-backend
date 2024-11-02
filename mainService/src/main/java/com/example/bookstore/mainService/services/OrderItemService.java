package com.example.bookstore.mainService.services;

import com.example.bookstore.mainService.dto.SalesDto;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemService {
    List<SalesDto> findSalesByCreateOnBetween(
            LocalDateTime startTime,
            LocalDateTime endTime);
}
