package com.example.book_store_back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;
    private long uid;
    private String receiver;
    private String phone;
    private String address;
    private long price;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)//级联删除，如果删除/写入order,对应的orderItem也会被删除/写入 孤儿删除，如果移除order中的orderItem，那么这个orderItem也会被删除
    private List<OrderItem> orderItems;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}
