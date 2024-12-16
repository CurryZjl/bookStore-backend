package com.example.bookstore.mainService.entity;

import com.example.bookstore.mainService.dto.BookGraphDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookPage {
    private int totalPages;
    private List<BookGraphDto> content;
}
