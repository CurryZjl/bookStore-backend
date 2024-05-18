package com.example.book_store_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private long uid;
    private long oid;
    private String receiver;
    private String phone;
    private String address;
    private List<OrderItemDto> orderItems;
    private long price;
    private LocalDateTime createOn;
}
