package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.repositories.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody AuthDto authDto){
        Optional<Long> uid = userRepository.findUidByEmail(authDto.getEmail());
        //TODO::修改登录验证
        if(uid.isEmpty()){
            return new ResponseDto<>(false,"邮箱输入错误！", null);
        }

        Optional<String> password = userRepository.findPasswordByUid(uid.get());
        if(authDto.getPassword().equals(password.get())){
            return new ResponseDto<>(true, "登录成功", null);
        }
        else {
            return new ResponseDto<>(false,"密码错误！", null);
        }
    }
}
