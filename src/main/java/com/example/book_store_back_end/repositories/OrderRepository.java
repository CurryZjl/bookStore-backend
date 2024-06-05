package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrdersByUid(long uid);
    
    List<Order> findOrdersByCreateOnBetweenAndUid(LocalDateTime startTime, LocalDateTime endTime, long uid);


    @Query("SELECT o FROM Order o JOIN o.orderItems oi JOIN oi.book b WHERE b.name LIKE %:bookName% AND o.uid = :uid")
    List<Order> findOrdersByBookNameLikeAndUid(String bookName, long uid);
}
