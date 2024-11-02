package com.example.bookstore.mainService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatItemDto {
    private long bookNums;
    private long allPrice;
    private List<StatBookDto> books;
}
