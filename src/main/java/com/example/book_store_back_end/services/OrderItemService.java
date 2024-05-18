package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;

import java.util.List;
public interface OrderItemService {
    List<OrderItemDto> getOrderItemsByOrder(Order order);
    List<OrderItem> createOrderItems(List<OrderItemDto> orderItemDtos);
}
