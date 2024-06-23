package com.example.book_store_back_end.utils;

import com.example.book_store_back_end.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;
import java.util.stream.Collectors;

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

    public static List<ConsumptionDto> calculateConsumption(List<OrderDto> orderDtos){
        Map<Long, Long> uidToTotalPrice = new HashMap<>();
        for(OrderDto orderDto : orderDtos){
            long uid = orderDto.getUid();
            long price = orderDto.getPrice();
            uidToTotalPrice.put(uid, uidToTotalPrice.getOrDefault(uid, 0L) + price);
        }

        List<ConsumptionDto> consumptionDtoList = uidToTotalPrice.entrySet().stream()
                .map(longLongEntry -> ConsumptionDto.builder()
                        .uid(longLongEntry.getKey())
                        .price_all(longLongEntry.getValue())
                        .build())
                .collect(Collectors.toList());
        consumptionDtoList.sort(Comparator.comparingLong(ConsumptionDto::getPrice_all).reversed());

        return consumptionDtoList;
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
