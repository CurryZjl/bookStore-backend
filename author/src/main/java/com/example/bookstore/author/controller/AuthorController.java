package com.example.bookstore.author.controller;

import com.example.bookstore.author.dto.ResponseDto;
import com.example.bookstore.author.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/micro/author")
public class AuthorController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/{bookName}")
    public ResponseDto<String> getBookAuthorByName(@PathVariable("bookName") String name){
        String author = bookRepository.findBook_AuthorByBookName(name);
        log.info("microService get:" + author);
        if(author != null){
            return new ResponseDto<>(true, "GET OK", author);
        }
        else{
            return new ResponseDto<>(false, "No Author", null);
        }
    }
}
