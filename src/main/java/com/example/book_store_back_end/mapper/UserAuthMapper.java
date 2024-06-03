package com.example.book_store_back_end.mapper;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.entity.UserAuth;

public class UserAuthMapper {
    public static UserAuth  mapToUserAuth(AuthDto authDto , User user){
        UserAuth userAuth = UserAuth.builder()
                .password(authDto.getPassword())
                .user(user)
                .build();
        return userAuth;
    }
}
