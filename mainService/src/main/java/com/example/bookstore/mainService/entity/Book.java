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
@Table(name = "books")

public class Book{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid")
    private long bid;
    @Lob
    @Column(name = "imagePath", length = 1000000)
    private String imagePath;
    private String name;
    private String author;
    private long price;
    private long status;
    private String intro;
    private String ISBN;
    private boolean deleted;
    @ManyToOne
    @JoinColumn(name = "tid")
    private Tag tag;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}