package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
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
