package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dao.BookDao;
import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.repositories.BookRepository;
import com.example.book_store_back_end.services.BookService;
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

    @Override
    public boolean updateBookStatus(long bid, long newStatus) {
        Optional<Book> book = bookDao.findBookByBid(bid);
        if(book.isPresent()){
            Book book1 = book.get();
            book1.setStatus(newStatus);
            bookRepository.save(book1);
            return true;
        }
        return false;
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
                .build();
    }
 }
