package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.PosterDto;
import com.example.book_store_back_end.entity.Poster;
import com.example.book_store_back_end.repositories.PosterRepository;
import com.example.book_store_back_end.services.PosterService;
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
