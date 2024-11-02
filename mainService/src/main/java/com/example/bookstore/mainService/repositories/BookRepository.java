package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findBookByBidAndDeletedFalse(long id);
    Page<Book> findBooksByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
}
