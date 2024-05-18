package com.example.book_store_back_end.controller;
import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.CartItemDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.services.BookService;
import com.example.book_store_back_end.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

//表示该类作为一个controller，他的每个方法返回一个领域对象，而不是view，告诉我们这里需要返回东西给前端
@RestController
@RequestMapping("/api/books")
public class BookController {
   private final BookService bookService;

   @Autowired
    public BookController(BookService bookService) {
       this.bookService = bookService;
    }


    @GetMapping("/{id}")
    public ResponseDto<BookDto> getBookById(@PathVariable long id){
       Optional<BookDto> res = bookService.findBookByBid(id);
       if(res.isPresent()){
            return new ResponseDto<>(true,"GET OK",res.get());
       }
       else {
           return new ResponseDto<>(false,"BookId (%d) Not Found".formatted(id),null);
       }
   }

   @GetMapping
    public ResponseDto<List<BookDto>> getBooks(){
       //从数据库获取所有书籍信息
      List<BookDto> res = bookService.findAll();
      if(!res.isEmpty()){
          return new ResponseDto<>(true,"GET OK",res);
      }else {
          return new ResponseDto<>(false,"Books Not Found",null);
      }
   }


}