package com.example.book_store_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private long bid;
    private String imagePath;
    private String name;
    private String tag;
    private String author;
    private long price;
    private long status;
    private String intro;
}
