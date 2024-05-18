package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findTagByTid(long tid);
    Tag findTagByBooks(List<Book> books);
}
