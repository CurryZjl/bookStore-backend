package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findOrderItemsByOrder(Order order);
}
