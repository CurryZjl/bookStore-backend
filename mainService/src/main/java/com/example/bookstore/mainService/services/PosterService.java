package com.example.bookstore.mainService.services;

import com.example.bookstore.mainService.dto.PosterDto;

import java.util.List;
import java.util.Optional;

public interface PosterService {
    Optional<PosterDto> findPosterByPid(long id);
    List<PosterDto> findAllPoster();
}
