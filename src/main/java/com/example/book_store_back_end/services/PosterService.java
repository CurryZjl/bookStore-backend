package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.PosterDto;

import java.util.List;
import java.util.Optional;

public interface PosterService {
    Optional<PosterDto> findPosterByPid(long id);
    List<PosterDto> findAllPoster();
}
