package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    Optional<BookDto> findBookByBid(long id);
    BookInOrderDto findBookByOrderItems(List<OrderItem> orderItems);
    List<BookDto> findAll();
}
