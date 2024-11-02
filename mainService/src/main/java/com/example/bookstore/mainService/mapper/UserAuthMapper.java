package com.example.bookstore.mainService.mapper;

import com.example.bookstore.mainService.dto.AuthDto;
import com.example.bookstore.mainService.entity.User;
import com.example.bookstore.mainService.entity.UserAuth;

public class UserAuthMapper {
    public static UserAuth mapToUserAuth(AuthDto authDto , User user){
        UserAuth userAuth = UserAuth.builder()
                .password(authDto.getPassword())
                .user(user)
                .build();
        return userAuth;
    }
}
