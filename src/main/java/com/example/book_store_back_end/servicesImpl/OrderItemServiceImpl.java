package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.SalesDto;
import com.example.book_store_back_end.repositories.OrderItemRepository;
import com.example.book_store_back_end.services.OrderItemService;
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
