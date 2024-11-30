package com.example.bookstore.mainService.services;

import com.example.bookstore.mainService.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    Optional<BookDto> findBookByBid(long id);
    Long updateBookStatus(long bid, long amount);

    Page<BookDto> searchBooksByName(String name, Pageable pageable);

    BookDto saveBook(BookDto bookDto);
    BookDto deleteBookByBid(long bid);

//    List<BookDto> searchBooksByTagName(String tagName);
//    List<BookDto> testInsert();
}
