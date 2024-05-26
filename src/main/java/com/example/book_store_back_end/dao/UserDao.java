package com.example.book_store_back_end.dao;

import com.example.book_store_back_end.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao {
    Optional<User> findUserByUid(long uid);

    Optional<Long> findUidByEmail(String email);
}
