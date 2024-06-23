package com.example.book_store_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {
    private long bid;
    private String name;
    private String imagePath;
    private long amountAll;
}
