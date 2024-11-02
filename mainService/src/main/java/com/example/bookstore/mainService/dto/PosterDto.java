package com.example.bookstore.mainService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosterDto {
    private long pid;
    private String name;
    private String posterPath;
}
