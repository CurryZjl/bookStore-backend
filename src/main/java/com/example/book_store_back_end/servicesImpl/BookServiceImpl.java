package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dao.BookDao;
import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.entity.Tag;
import com.example.book_store_back_end.repositories.BookRepository;
import com.example.book_store_back_end.repositories.TagRepository;
import com.example.book_store_back_end.services.BookService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final TagRepository tagRepository;

    public BookServiceImpl(BookDao bookDao, TagRepository tagRepository) {
        this.bookDao = bookDao;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<BookDto> findAll() {
        List<Book> books = bookDao.findAll();
        return books.stream()
                .map(book -> {
                    List<Book> books1 = new ArrayList<>();
                    books1.add(book);
                    Tag tag = tagRepository.findTagByBooks(books1);
                    return mapToBookDto(book,tag);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findBookByBid(long id) {
        Optional<Book> book = bookDao.findBookByBid(id);
        return book.map(book1 -> {
            List<Book> bookList = new ArrayList<>();
            bookList.add(book1);
            Tag tag = tagRepository.findTagByBooks(bookList);
            return mapToBookDto(book1,tag);
        });
    }

    @Override
    public BookInOrderDto findBookByOrderItems(List<OrderItem> orderItems) {
        Book book = bookDao.findBookByOrderItems(orderItems);
        return mapToBookInOrderDto(book);
    }

    private static BookDto mapToBookDto(Book book, Tag tag){
        return BookDto.builder()
                .bid(book.getBid())
                .price(book.getPrice())
                .intro(book.getIntro())
                .name(book.getName())
                .author(book.getAuthor())
                .status(book.getStatus())
                .imagePath(book.getImagePath())
                .tag(tag.getName())
                .build();
    }
    private static BookInOrderDto mapToBookInOrderDto(Book book){
        return BookInOrderDto.builder()
                .name(book.getName())
                .imagePath(book.getImagePath())
                .build();
    }
 }
