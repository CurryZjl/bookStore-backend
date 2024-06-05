package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dao.BookDao;
import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.MessageDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.repositories.BookRepository;
import com.example.book_store_back_end.services.BookService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final BookRepository bookRepository;

    public BookServiceImpl(BookDao bookDao, BookRepository bookRepository) {
        this.bookDao = bookDao;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> books = bookDao.findAll();
        return books.stream()
                .map(BookServiceImpl::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findBookByBid(long id) {
        Optional<Book> book = bookDao.findBookByBid(id);
        return book.map(BookServiceImpl::mapToBookDto);
    }

    @Transactional
    @Override
    public MessageDto updateBookStatus(long bid, long amount) {
        Optional<Book> book = bookDao.findBookByBid(bid);
        if(book.isPresent()){
            Book book1 = book.get();
            long status = book1.getStatus();
            long newStatus = status - amount;
            if(newStatus < 0){
                return MessageDto.builder()
                        .valid(false)
                        .message("本书籍缺货")
                        .build();
            }
            book1.setStatus(newStatus);
            bookRepository.save(book1);
            return MessageDto.builder()
                    .message("更新库存成功")
                    .valid(true)
                    .build();
        }
        return MessageDto.builder()
                .valid(false)
                .message("更新书籍库存错误")
                .build();
    }

    @Override
    public Page<BookDto> searchBooksByName(String name , Pageable pageable) {
        Page<Book> books = bookDao.searchBooksByName(name, pageable);
        List<BookDto> bookDtos = books.getContent().stream()
                .map(BookServiceImpl::mapToBookDto)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDtos, pageable, books.getTotalElements());
    }

    private static BookDto mapToBookDto(Book book){
        return BookDto.builder()
                .bid(book.getBid())
                .price(book.getPrice())
                .intro(book.getIntro())
                .name(book.getName())
                .author(book.getAuthor())
                .status(book.getStatus())
                .imagePath(book.getImagePath())
                .tag(book.getTag().getName())
                .isbn(book.getISBN())
                .build();
    }
 }
