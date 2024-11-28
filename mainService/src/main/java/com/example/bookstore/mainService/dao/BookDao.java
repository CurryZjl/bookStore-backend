package com.example.bookstore.mainService.dao;

import com.example.bookstore.mainService.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findBookByBid(long id);
    Page<Book> searchBooksByName(String name , Pageable pageable);
    Book deleteBookByBid(Long id);
    Book saveBook(Book book, Boolean isMongo);
}
