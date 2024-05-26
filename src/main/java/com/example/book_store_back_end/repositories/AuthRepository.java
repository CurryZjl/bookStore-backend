package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByUser_UidAndPassword(long uid, String password);
}
