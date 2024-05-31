package com.example.book_store_back_end.dao.daoImpl;

import com.example.book_store_back_end.dao.BookDao;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.repositories.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;

    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> findBookByBid(long id) {
        return bookRepository.findBookByBid(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> searchBooksByName(String name ,Pageable pageable) {
        return bookRepository.findBooksByNameContainingIgnoreCase(name , pageable);
    }
}
