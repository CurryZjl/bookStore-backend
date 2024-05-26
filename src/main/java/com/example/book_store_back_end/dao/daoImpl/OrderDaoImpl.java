package com.example.book_store_back_end.dao.daoImpl;

import com.example.book_store_back_end.dao.OrderDao;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.repositories.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    private final OrderRepository orderRepository;

    public OrderDaoImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findOrdersByUid(long uid) {
        return orderRepository.findOrdersByUid(uid);
    }
}
