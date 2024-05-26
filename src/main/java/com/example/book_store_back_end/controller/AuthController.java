package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.repositories.UserRepository;
import com.example.book_store_back_end.services.AuthService;
import com.example.book_store_back_end.services.UserServive;
import com.example.book_store_back_end.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @Autowired
    public AuthController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody AuthDto authDto){
        Optional<Long> uid = userRepository.findUidByEmail(authDto.getEmail());
        if(uid.isEmpty()){
            return new ResponseDto<>(false,"邮箱输入错误！", null);
        }

        if(authService.checkPasswordByUidAndPassword(uid.get(), authDto.getPassword())){
            HttpSession session = SessionUtils.getSession();
            if(session != null){
                session.setAttribute("uid", uid.get());
            }
            return new ResponseDto<>(true, "登录成功", null);
        }
        else {
            return new ResponseDto<>(false,"密码错误！", null);
        }
    }
}
