package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {
    Optional<BookDto> findBookByBid(long id);
    Long updateBookStatus(long bid, long amount);

    Page<BookDto> searchBooksByName(String name, Pageable pageable);

    BookDto saveBook(BookDto bookDto);
    BookDto deleteBookByBid(long bid);
}
