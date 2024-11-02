package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.dto.ConsumptionDto;
import com.example.bookstore.mainService.dto.StatBookDto;
import com.example.bookstore.mainService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findOrdersByUidOrderByCreateOnDesc(long uid, Pageable pageable);
    
    Page<Order> findOrdersByCreateOnBetweenAndUid(LocalDateTime startTime, LocalDateTime endTime, long uid, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o "
            + "JOIN FETCH o.orderItems oi "
            + "JOIN FETCH oi.book b "
            + "WHERE b.name LIKE %:bookName% AND o.uid = :uid")
    Page<Order> findOrdersByBookNameLikeAndUid(@Param("bookName") String bookName, @Param("uid") long uid, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN oi.book b WHERE b.name LIKE %:bookName% ")
    Page<Order> findAllByBookNameLike(@Param("bookName") String bookName, Pageable pageable);

    //普通用户统计自己购买书籍的情况
    @Query("SELECT new com.example.bookstore.mainService.dto.StatBookDto(oi.book.bid, oi.book.imagePath, oi.book.name, SUM(oi.amount), SUM(oi.amount * oi.book.price))"
            + " FROM Order o"
            + " JOIN o.orderItems oi"
            + " WHERE o.uid = :uid"
            + " AND o.createOn BETWEEN :startTime AND :endTime"
            + " GROUP BY oi.book.bid, oi.book.imagePath, oi.book.name")
    List<StatBookDto> findBooksPurchasedByUserInTimeRange(@Param("uid") long uid,
                                                          @Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);
    //统计用户消费情况
    @Query("SELECT new com.example.bookstore.mainService.dto.ConsumptionDto(o.uid, u.name, SUM(o.price))"
            + " FROM Order o"
            + " JOIN User u ON o.uid = u.uid"
            + " WHERE o.createOn BETWEEN :startTime AND :endTime"
            + " GROUP BY o.uid, u.name"
            + " ORDER BY SUM(o.price) DESC")
    List<ConsumptionDto> findUserConsumptionInTimeRange(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);
}
