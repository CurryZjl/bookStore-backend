package com.example.bookstore.mainService.serviceImpl;

import com.example.bookstore.mainService.dto.PosterDto;
import com.example.bookstore.mainService.entity.Poster;
import com.example.bookstore.mainService.repositories.PosterRepository;
import com.example.bookstore.mainService.services.PosterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PosterServiceImpl implements PosterService {
    private final PosterRepository posterRepository;

    public PosterServiceImpl(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    @Override
    public List<PosterDto> findAllPoster() {
        List<Poster> posters = posterRepository.findAll();
        return posters.stream()
                .map(
                        PosterServiceImpl::mapToPosterDto
                ).collect(Collectors.toList());
    }

    @Override
    public Optional<PosterDto> findPosterByPid(long id) {
        Optional<Poster> poster = posterRepository.findPosterByPid(id);
        return poster.map(PosterServiceImpl::mapToPosterDto);
    }

    private static PosterDto mapToPosterDto(Poster poster){
        return PosterDto.builder()
                .pid(poster.getPid())
                .posterPath(poster.getPosterPath())
                .name(poster.getName())
                .build();
    }
}
