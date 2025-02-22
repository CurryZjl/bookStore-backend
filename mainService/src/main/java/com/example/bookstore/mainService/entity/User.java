package com.example.bookstore.mainService.entity;

import com.example.bookstore.mainService.constants.UserRole;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;
    private String name;

    @Column(nullable = false, columnDefinition = "int")
    private int level = 1;
    private String email;
    private String introduction;
    private String avatarSrc;

    private UserRole role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;
}