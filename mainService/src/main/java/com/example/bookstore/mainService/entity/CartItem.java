package com.example.bookstore.mainService.entity;


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
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cid;

    @ManyToOne
    @JoinColumn(name = "bid")
    private Book book;

    @Column(nullable = false, columnDefinition = "bigint")
    private long amount = 1L;
    private long uid;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}
