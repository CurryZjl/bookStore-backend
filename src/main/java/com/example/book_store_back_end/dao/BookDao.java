package com.example.book_store_back_end.dao;

import com.example.book_store_back_end.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findBookByBid(long id);
    List<Book> findAll();
    Page<Book> searchBooksByName(String name , Pageable pageable);
}
