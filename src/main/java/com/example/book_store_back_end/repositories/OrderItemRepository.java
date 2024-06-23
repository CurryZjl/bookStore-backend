package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
