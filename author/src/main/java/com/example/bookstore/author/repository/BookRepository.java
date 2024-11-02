package com.example.bookstore.author.repository;

import com.example.bookstore.author.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT b.author from Book b where b.name = :name")
    String findBook_AuthorByBookName(String name);
}