package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.repositories.OrderRepository;
import com.example.book_store_back_end.services.OrderItemService;
import com.example.book_store_back_end.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public List<OrderDto> getOrdersByUid(long id) {
        List<Order> orders = orderRepository.findOrdersByUid(id);
        return orders.stream()
                .map(this::mapToOrderDto).collect(Collectors.toList());
    }

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
        List<OrderItemDto> orderItemDtos = orderItemService.getOrderItemsByOrder(order);
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

    private Order  mapToOrder(OrderDto orderDto){
        return Order.builder()
                .uid(orderDto.getUid())
                .address(orderDto.getAddress())
                .price(orderDto.getPrice())
                .receiver(orderDto.getReceiver())
                .phone(orderDto.getPhone())
                .build();
    }
}
