package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.repositories.UserRepository;
import com.example.book_store_back_end.services.AuthService;
import com.example.book_store_back_end.services.UserServive;
import com.example.book_store_back_end.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserServive userServive;
    private final AuthService authService;

    public AuthController(UserServive userServive, AuthService authService) {
        this.userServive = userServive;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody AuthDto authDto){
        Optional<Long> uid = userServive.findUidByName(authDto.getName());
        if(uid.isEmpty()){
            return new ResponseDto<>(false,"找不到用户！", null);
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

    @PostMapping("/user/name")
    public ResponseDto<String> checkName(@RequestBody AuthDto authDto){
        boolean res = userServive.existsUserByName(authDto.getName());
        if(res){
            return new ResponseDto<>(false,"用户名重复，请重新输入", null);
        }
        else {
            return new ResponseDto<>(true, "用户名校验成功", null);
        }
    }

    @PostMapping("/user")
    public ResponseDto<AuthDto> signUp(@RequestBody AuthDto authDto){
       long newUid = userServive.createUser(authDto);
        if(newUid != -1){
            /* 成功创建订单信息 */
            return new ResponseDto<>(true, "注册用户成功", authDto);
        }else{
            /* 订单信息创建失败 */
            return new ResponseDto<>(false,"注册用户错误", authDto);
        }
    }
}
