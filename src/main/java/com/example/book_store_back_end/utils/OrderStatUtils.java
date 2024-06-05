package com.example.book_store_back_end.utils;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.dto.StatBookDto;
import com.example.book_store_back_end.dto.StatItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatUtils {
    public static StatItemDto calculateUserOrderStatistics(List<OrderDto> orderDtos){
        StatItemDto statItemDto = new StatItemDto();
        statItemDto.setBooks(new ArrayList<>());

        long totalBooks = 0;
        long totalPrice = 0;
        Map<Long, StatBookDto> bookMap = new HashMap<>();

        // Iterate through each order
        for (OrderDto order : orderDtos) {
            totalBooks += calculateTotalBooks(order);
            totalPrice += order.getPrice();
            updateBookMap(bookMap, order);
        }

        statItemDto.setBookNums(totalBooks);
        statItemDto.setAllPrice(totalPrice);
        statItemDto.setBooks(new ArrayList<>(bookMap.values()));

        return statItemDto;
    }

    private static long calculateTotalBooks(OrderDto order){
        long totalBooks = 0;
        for(OrderItemDto item : order.getOrderItems()){
            totalBooks += item.getAmount();
        }
        return totalBooks;
    }

    private static void updateBookMap(Map<Long, StatBookDto> bookMap, OrderDto order) {
        for (OrderItemDto item : order.getOrderItems()) {
            long bid = item.getBook().getBid();
            StatBookDto statBookDto = bookMap.getOrDefault(bid, new StatBookDto());
            statBookDto.setBid(bid);
            statBookDto.setImagePath(item.getBook().getImagePath());
            statBookDto.setName(item.getBook().getName());
            statBookDto.setCount(statBookDto.getCount() + item.getAmount());
            bookMap.put(bid, statBookDto);
        }
    }
}
