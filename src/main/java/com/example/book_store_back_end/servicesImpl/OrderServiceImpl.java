package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.*;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.mapper.OrderItemMapper;
import com.example.book_store_back_end.repositories.OrderRepository;
import com.example.book_store_back_end.services.BookService;
import com.example.book_store_back_end.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookService bookService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookService bookService) {
        this.orderRepository = orderRepository;
        this.bookService = bookService;
    }

    @Override
    public Page<OrderDto> getOrdersByUid(long id, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByUidOrderByCreateOnDesc(id, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto).collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Transactional
    @Override
    public ResponseDto<Long> createOrder(OrderDto orderDto) {
       try{
           Order order = mapToOrder(orderDto);
           List<OrderItem> orderItems = order.getOrderItems();
           for(OrderItem orderItem : orderItems){
               long bid = orderItem.getBook().getBid();
               long amount = orderItem.getAmount();
               MessageDto messageDto = this.bookService.updateBookStatus(bid,amount);
               if(!messageDto.isValid()){
                   return new ResponseDto<>(false, messageDto.getMessage(), -1L);
               }
           }
           Order saveOrder =  orderRepository.save(order);
           return new ResponseDto<>(true, "创建订单成功" , saveOrder.getOid()) ;
       } catch (Exception e){
           System.err.println(e.getMessage());
           return new ResponseDto<>(false,"创建订单失败", -1L); //保存错误，返回-1
       }
    }

    @Override
    public Page<OrderDto> findOrdersByBookNameLike(String bookName, long uid, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByBookNameLikeAndUid(bookName, uid, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDto> findOrdersByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, long uid, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByCreateOnBetweenAndUid(startTime, endTime, uid, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDto> findAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderDto> orderDtos= orders.getContent().stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderDto> findAllByBookNameLike(String bookName, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByBookNameLike(bookName, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Override
    public List<StatBookDto> findBooksPurchasedByUserInTimeRange(long uid, LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.findBooksPurchasedByUserInTimeRange(uid, startTime, endTime);
    }

    @Override
    public Page<OrderDto> findAllByCreateOnBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByCreateOnBetween(startTime, endTime, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Override
    public List<ConsumptionDto> findUserConsumptionInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.findUserConsumptionInTimeRange(startTime, endTime);
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
