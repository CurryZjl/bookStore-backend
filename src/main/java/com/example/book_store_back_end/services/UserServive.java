package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserServive {
    Optional<UserDto> getUserByUid(long uid);
    Optional<Long> findUidByName(String name);
    boolean existsUserByName(String name);

    long createUser(AuthDto authDto);
}
