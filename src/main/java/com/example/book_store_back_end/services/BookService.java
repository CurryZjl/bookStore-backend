package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.dto.MessageDto;
import com.example.book_store_back_end.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    Optional<BookDto> findBookByBid(long id);
    List<BookDto> findAll();
    MessageDto updateBookStatus(long bid, long amount);

    Page<BookDto> searchBooksByName(String name, Pageable pageable);
}
