package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.entity.Poster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosterRepository extends JpaRepository<Poster,Long> {
    Optional<Poster> findPosterByPid(long id);
    List<Poster> findAll();
}
