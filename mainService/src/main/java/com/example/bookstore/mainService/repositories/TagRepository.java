package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findTagByName(String name);
}
