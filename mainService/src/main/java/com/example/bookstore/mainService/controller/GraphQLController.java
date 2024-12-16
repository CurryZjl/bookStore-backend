package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.dto.BookDto;
import com.example.bookstore.mainService.dto.BookGraphDto;
import com.example.bookstore.mainService.entity.BookPage;
import com.example.bookstore.mainService.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class GraphQLController {
    private final BookService bookService;
    @QueryMapping
    public BookPage searchBooks(@Argument String query,@Argument Integer pageIndex,@Argument Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex != null ? pageIndex : 0, pageSize != null ? pageSize : 8);
        Page<BookDto> bookPage = null;
        // 转换结果
        try {
            bookPage = bookService.searchBooksByName(query != null ? query : "" , pageable);
            System.out.println(bookPage);
            BookPage res = BookPage.builder()
                    .totalPages(bookPage.getTotalPages())
                    .content(bookPage.getContent().stream().map(bookDto -> {
                        BookGraphDto bookGraphDto = BookGraphDto.builder()
                                .bid(bookDto.getBid())
                                .tag(bookDto.getTag())
                                .name(bookDto.getName())
                                .price(bookDto.getPrice())
                                .isbn(bookDto.getIsbn())
                                .status(bookDto.getStatus())
                                .author(bookDto.getAuthor())
                                .imagePath(bookDto.getImagePath())
                                .build();
                        return bookGraphDto;
                    }).collect(Collectors.toList()))
                    .build();
            return res;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }

    }
}
