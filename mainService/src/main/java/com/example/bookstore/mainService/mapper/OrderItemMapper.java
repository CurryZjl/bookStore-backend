package com.example.bookstore.mainService.mapper;

import com.example.bookstore.mainService.dto.BookInOrderDto;
import com.example.bookstore.mainService.dto.OrderItemDto;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.entity.Order;
import com.example.bookstore.mainService.entity.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class OrderItemMapper {
    public static OrderItemDto mapToOrderItemDto(OrderItem orderItem){
        BookInOrderDto bookInOrderDto = BookInOrderDto.builder()
                .name(orderItem.getBook().getName())
                .imagePath(orderItem.getBook().getImagePath())
                .bid(orderItem.getBook().getBid())
                .build();
        return OrderItemDto.builder()
                .tid(orderItem.getTid())
                .amount(orderItem.getAmount())
                .book(bookInOrderDto)
                .oid(orderItem.getOrder().getOid())
                .build();
    }

    public static OrderItem mapToOrderItem(OrderItemDto orderItemDto){
        return  OrderItem.builder()
                .order(Order.builder().oid(orderItemDto.getOid()).build())
                .book(Book.builder().bid(orderItemDto.getBook().getBid()).build())
                .amount(orderItemDto.getAmount())
                .build();
    }
}
