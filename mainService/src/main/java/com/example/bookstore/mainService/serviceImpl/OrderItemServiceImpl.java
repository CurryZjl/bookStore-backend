package com.example.bookstore.mainService.serviceImpl;

import com.example.bookstore.mainService.dto.SalesDto;
import com.example.bookstore.mainService.repositories.OrderItemRepository;
import com.example.bookstore.mainService.services.OrderItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<SalesDto> findSalesByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return orderItemRepository.findSalesByCreateOnBetween(startTime,endTime);
    }
}
