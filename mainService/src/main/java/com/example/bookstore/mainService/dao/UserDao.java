package com.example.bookstore.mainService.dao;


import com.example.bookstore.mainService.entity.User;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUserByUid(long uid);

    Optional<Long> findUidByName(String name);
}
