package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.PosterDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.PosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/poster")
public class PosterController {
    private final PosterService posterService;

    @Autowired
    public PosterController(PosterService posterService) {
        this.posterService = posterService;
    }

    @GetMapping("/{id}")
    public ResponseDto<PosterDto> getPosterById(@PathVariable long id){
        Optional<PosterDto> posterDto = posterService.findPosterByPid(id);
        if(posterDto.isPresent()){
            return new ResponseDto<>(true,"Get OK", posterDto.get());
        }else{
            return new ResponseDto<>(false,"PosterId (%d) Not Found".formatted(id),null);
        }
    };

    @GetMapping
    public ResponseDto<List<PosterDto>> getPosters(){
        List<PosterDto> posters;
        posters = posterService.findAllPoster();
        if(!posters.isEmpty()){
            return new ResponseDto<>(true,"GET OK", posters);
        }else {
            return new ResponseDto<>(false,"Posters Not Found", null);
        }
    }
}