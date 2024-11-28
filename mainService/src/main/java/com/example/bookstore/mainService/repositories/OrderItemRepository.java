package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.dto.SalesDto;
import com.example.bookstore.mainService.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    //管理员统计书的销量
    @Query("SELECT new com.example.bookstore.mainService.dto.SalesDto(oi.book.bid, oi.book.name, SUM(oi.amount)) " +
            "FROM OrderItem oi " +
            "WHERE oi.createOn BETWEEN :startTime AND :endTime " +
            "GROUP BY oi.book.bid, oi.book.name")
    List<SalesDto> findSalesByCreateOnBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
