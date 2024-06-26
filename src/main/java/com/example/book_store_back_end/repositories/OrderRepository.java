package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.Order;
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
    Page<Order> findOrdersByUid(long uid, Pageable pageable);

    List<Order> findOrdersByUid(long uid);
    
    Page<Order> findOrdersByCreateOnBetweenAndUid(LocalDateTime startTime, LocalDateTime endTime, long uid, Pageable pageable);

    List<Order> findOrdersByCreateOnBetweenAndUid(LocalDateTime startTime, LocalDateTime endTime, long uid);


    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN oi.book b WHERE b.name LIKE %:bookName% AND o.uid = :uid")
    Page<Order> findOrdersByBookNameLikeAndUid(@Param("bookName") String bookName, @Param("uid") long uid, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    List<Order> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN oi.book b WHERE b.name LIKE %:bookName% ")
    Page<Order> findAllByBookNameLike(@Param("bookName") String bookName, Pageable pageable);
}
