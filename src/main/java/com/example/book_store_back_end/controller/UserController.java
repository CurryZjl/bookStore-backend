package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.dto.UserDto;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.services.UserServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserServive userServive;

    @Autowired
    public UserController(UserServive userServive) {
        this.userServive = userServive;
    }

    @GetMapping
    public ResponseDto<UserDto> getProfile(){
        long uid = 1; //TODO::增加身份验证之后就能直接拿到发来请求的uid
        Optional<UserDto> res = userServive.getUserByUid(uid);
        if(res.isPresent()){
            return new ResponseDto<>(true,"GET OK" , res.get());
        }
        else
            return new ResponseDto<>(false,"User Not Found", null);
    }
}