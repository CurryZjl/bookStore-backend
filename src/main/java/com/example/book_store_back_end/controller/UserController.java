package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.dto.UserDto;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.services.UserServive;
import com.example.book_store_back_end.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
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
        long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false,"Auth fault", null);
        }
        Optional<UserDto> res = userServive.getUserByUid(uid);
        if(res.isPresent()){
            return new ResponseDto<>(true,"GET OK" , res.get());
        }
        else
            return new ResponseDto<>(false,"User Not Found", null);
    }
}