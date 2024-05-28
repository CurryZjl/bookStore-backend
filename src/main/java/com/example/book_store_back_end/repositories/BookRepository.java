package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findBookByBid(long id);
    List<Book> findAll();
}
