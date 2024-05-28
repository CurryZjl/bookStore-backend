package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.repositories.OrderRepository;
import com.example.book_store_back_end.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> getOrdersByUid(long id) {
        List<Order> orders = orderRepository.findOrdersByUid(id);
        return orders.stream()
                .map(this::mapToOrderDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public long createOrder(OrderDto orderDto) {
       try{
           Order order = mapToOrder(orderDto);
           Order saveOrder =  orderRepository.save(order);
           return saveOrder.getOid();
       } catch (Exception e){
           System.err.println(e.getMessage());
           return -1; //保存错误，返回-1
       }
    }

    private OrderDto mapToOrderDto(Order order){
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(OrderItemMapper::mapToOrderItemDto)
                .collect(Collectors.toList());
        return OrderDto.builder()
                .uid(order.getUid())
                .receiver(order.getReceiver())
                .price(order.getPrice())
                .oid(order.getOid())
                .address(order.getAddress())
                .phone(order.getPhone())
                .orderItems(orderItemDtos)
                .createOn(order.getCreateOn())
                .build();
    }

    private Order mapToOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .uid(orderDto.getUid())
                .address(orderDto.getAddress())
                .price(orderDto.getPrice())
                .receiver(orderDto.getReceiver())
                .phone(orderDto.getPhone())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            OrderItem orderItem = OrderItemMapper.mapToOrderItem(orderItemDto);
            // Set the order for the OrderItem
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        // Set the orderItems for the Order
        order.setOrderItems(orderItems);

        return order;
    }

}
