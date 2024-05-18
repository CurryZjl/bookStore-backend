package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.BookInOrderDto;
import com.example.book_store_back_end.dto.OrderItemDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.Order;
import com.example.book_store_back_end.entity.OrderItem;
import com.example.book_store_back_end.repositories.BookRepository;
import com.example.book_store_back_end.repositories.OrderItemRepository;
import com.example.book_store_back_end.services.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, BookRepository bookRepository) {
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public List<OrderItemDto> getOrderItemsByOrder(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrder(order);
        return orderItems.stream().map(orderItem -> {
            List<OrderItem> orderItems1 = new ArrayList<>();
            orderItems1.add(orderItem);
            Book book = bookRepository.findBookByOrderItems(orderItems1);
            return mapToOrderItemDto(orderItem,book);
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderItem> createOrderItems(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemDto orderItemDto: orderItemDtos){
            try{
                OrderItem orderItem = mapToOrderItem(orderItemDto);
                orderItemRepository.save(orderItem);
                orderItems.add(orderItem);
            } catch (Exception e){
                e.printStackTrace();;
                return Collections.emptyList();
            }
        }

        return  orderItems;
    }

    private static OrderItemDto mapToOrderItemDto(OrderItem orderItem, Book book){
        BookInOrderDto bookInOrderDto = BookInOrderDto.builder()
                .name(book.getName())
                .imagePath(book.getImagePath())
                .bid(book.getBid())
                .build();
        return OrderItemDto.builder()
                .tid(orderItem.getTid())
                .amount(orderItem.getAmount())
                .book(bookInOrderDto)
                .oid(orderItem.getOrder().getOid())
                .build();
    }

    private static OrderItem mapToOrderItem(OrderItemDto orderItemDto){
        return  OrderItem.builder()
                .order(Order.builder().oid(orderItemDto.getOid()).build())
                .book(Book.builder().bid(orderItemDto.getBook().getBid()).build())
                .amount(orderItemDto.getAmount())
                .build();
    }
}
