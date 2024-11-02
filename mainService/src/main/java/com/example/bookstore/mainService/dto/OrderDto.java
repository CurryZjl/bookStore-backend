package com.example.bookstore.mainService.dto;

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
    private Long uid;
    private Long oid;
    private String receiver;
    private String phone;
    private String address;
    private List<OrderItemDto> orderItems;
    private long price;
    private LocalDateTime createOn;
}
