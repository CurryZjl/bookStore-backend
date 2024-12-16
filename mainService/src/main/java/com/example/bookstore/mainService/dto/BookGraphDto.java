package com.example.bookstore.mainService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookGraphDto {
    private long bid;
    private String imagePath;
    private String name;
    private String tag;
    private String author;
    private long price;
    private long status;
    private String isbn;
}
