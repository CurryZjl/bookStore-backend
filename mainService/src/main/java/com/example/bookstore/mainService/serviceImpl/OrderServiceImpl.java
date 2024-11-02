package com.example.bookstore.mainService.serviceImpl;


import com.example.bookstore.mainService.dto.ConsumptionDto;
import com.example.bookstore.mainService.dto.OrderDto;
import com.example.bookstore.mainService.dto.OrderItemDto;
import com.example.bookstore.mainService.dto.StatBookDto;
import com.example.bookstore.mainService.entity.Order;
import com.example.bookstore.mainService.entity.OrderItem;
import com.example.bookstore.mainService.mapper.OrderItemMapper;
import com.example.bookstore.mainService.repositories.OrderRepository;
import com.example.bookstore.mainService.services.BookService;
import com.example.bookstore.mainService.services.CartItemService;
import com.example.bookstore.mainService.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final CartItemService cartItemService;

    @Override
    public Page<OrderDto> getOrdersByUid(long id, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByUidOrderByCreateOnDesc(id, pageable);
        List<OrderDto> orderDtos = orders.getContent().stream()
                .map(this::mapToOrderDto).collect(Collectors.toList());
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    @Transactional
    @Override
    public Long createOrder(OrderDto orderDto) {
       try{
           Order order = mapToOrder(orderDto);
           List<OrderItem> orderItems = order.getOrderItems();
           for(OrderItem orderItem : orderItems){
               long bid = orderItem.getBook().getBid();
               long amount = orderItem.getAmount();
               this.bookService.updateBookStatus(bid,amount);
               //若更新书籍库存成功，同时对应删除购物车的记录
               cartItemService.deleteCartItemByUidAndBid(order.getUid(), bid);
           }
           Order saveOrder =  orderRepository.save(order);
           return saveOrder.getOid();
       } catch (RuntimeException e){
           System.err.println(e.getMessage());
           throw e; //重新抛出异常使得调用 createOrder 的方法能知道更具体的错误信息
       }
       catch (Exception e){
           System.err.println(e.getMessage());
           return null; //保存错误，返回空
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
