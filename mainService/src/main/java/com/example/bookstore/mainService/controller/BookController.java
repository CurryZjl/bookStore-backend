package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.dto.BookDto;
import com.example.bookstore.mainService.dto.ResponseDto;
import com.example.bookstore.mainService.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

   @DeleteMapping("/{bid}")
   public ResponseDto<BookDto> deleteBookByBid(@PathVariable long bid){
        BookDto bookDto = bookService.deleteBookByBid(bid);
        if(bookDto == null){
            return new ResponseDto<>(false, "删除错误" , null);
        }
        return new ResponseDto<>(true, "删除成功", bookDto);
   }

   @PostMapping
   public ResponseDto<BookDto> saveBook(@RequestBody BookDto bookDto){
        try {
            //注意同时包含了更新和新添加
            BookDto bookDto1 = bookService.saveBook(bookDto);
            if(bookDto1 != null)
                return new ResponseDto<>(true, "更新书籍成功", bookDto1);
            else
                return new ResponseDto<>(false, "更新书籍失败", null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseDto<>(false, e.getMessage(), null);
        }
   }

   @GetMapping
    public ResponseDto<Page<BookDto>> getBooks(@RequestParam(required = false, defaultValue = "") String query,
                                               @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                               @RequestParam(required = false, defaultValue = "8") int pageSize){
       Pageable pageable = PageRequest.of(pageIndex, pageSize);
       Page<BookDto> bookPage;
       try {
           bookPage = bookService.searchBooksByName(query, pageable);
       }catch (Exception e){
           return new ResponseDto<>(false, e.getMessage(), null);
       }
       return new ResponseDto<>(true, "拿取书籍成功", bookPage);

   }
}