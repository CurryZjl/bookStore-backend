package com.example.book_store_back_end.dao;

import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findBookByBid(long id);
    Book findBookByOrderItems(List<OrderItem> orderItems);
    List<Book> findAll();
}
