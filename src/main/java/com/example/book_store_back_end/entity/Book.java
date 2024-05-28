package com.example.book_store_back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.book_store_back_end.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "books")
public class Book{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid")
    private long bid;
    private String imagePath;
    private String name;
    private String author;
    private long price;
    private long status;
    private String intro;
    @ManyToOne
    @JoinColumn(name = "tid")
    private Tag tag;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL, fetch = FetchType.LAZY) //级联 如果删除一个book，那么含有这个bookID的orderItem也会被删除
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    @CreationTimestamp
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}