package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.MessageDto;
import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.mapper.OrderItemMapper;
import com.example.book_store_back_end.repositories.OrderRepository;
import com.example.book_store_back_end.services.BookService;
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
    private final BookService bookService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookService bookService) {
        this.orderRepository = orderRepository;
        this.bookService = bookService;
    }

    @Override
    public List<OrderDto> getOrdersByUid(long id) {
        List<Order> orders = orderRepository.findOrdersByUid(id);
        return orders.stream()
                .map(this::mapToOrderDto).collect(Collectors.toList());
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
