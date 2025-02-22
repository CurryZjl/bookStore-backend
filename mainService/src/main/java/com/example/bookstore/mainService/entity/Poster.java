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
@Table(name = "posters")
public class Poster{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pid;
    private String name;
    private String posterPath;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}