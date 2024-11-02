package com.example.bookstore.mainService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tid;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oid" , referencedColumnName = "oid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bid")
    private Book book;

    private int amount;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}
