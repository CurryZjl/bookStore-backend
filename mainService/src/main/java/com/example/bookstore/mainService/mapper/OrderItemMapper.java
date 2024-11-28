package com.example.bookstore.mainService.mapper;

import com.example.bookstore.mainService.dto.BookInOrderDto;
import com.example.bookstore.mainService.dto.OrderItemDto;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.entity.BookInfo;
import com.example.bookstore.mainService.entity.Order;
import com.example.bookstore.mainService.entity.OrderItem;
import com.example.bookstore.mainService.repositories.BookInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderItemMapper {
    @Autowired
    private final BookInfoRepository bookInfoRepository;

    public OrderItemDto mapToOrderItemDto(OrderItem orderItem){
        //拿取Order错误的原因是JPA拿到的Book内容中BookInfo是Null，这里mapper就失败了
        Optional<BookInfo> bookInfo = this.bookInfoRepository.findById(orderItem.getBook().getBid());
        BookInOrderDto bookInOrderDto = BookInOrderDto.builder()
                .name(orderItem.getBook().getName())
                .bid(orderItem.getBook().getBid())
                .build();
        bookInfo.ifPresent(bookInfo1 -> {
            bookInOrderDto.setImagePath(bookInfo1.getImage());
        });
        return OrderItemDto.builder()
                .tid(orderItem.getTid())
                .amount(orderItem.getAmount())
                .book(bookInOrderDto)
                .oid(orderItem.getOrder().getOid())
                .build();
    }

    public static OrderItem mapToOrderItem(OrderItemDto orderItemDto){
        return  OrderItem.builder()
                .order(Order.builder().oid(orderItemDto.getOid()).build())
                .book(Book.builder().bid(orderItemDto.getBook().getBid()).build())
                .amount(orderItemDto.getAmount())
                .build();
    }
}
