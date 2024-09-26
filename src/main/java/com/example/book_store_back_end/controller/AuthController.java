package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.AuthService;
import com.example.book_store_back_end.services.ClockService;
import com.example.book_store_back_end.services.UserServive;
import com.example.book_store_back_end.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServive userServive;
    private final AuthService authService;
    private final ClockService clockService;

    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody AuthDto authDto){
        Optional<Long> uid = userServive.findUidByName(authDto.getName());
        if(uid.isEmpty()){
            return new ResponseDto<>(false,"找不到用户！", null);
        }

        Optional<UserRole> userRole = userServive.findRoleByUid(uid.get());
        if(userRole.isEmpty()){
            return new ResponseDto<>(false,"用户角色错误", null); //不可到达
        }
        if(userRole.get().equals(UserRole.BANNED)){
            return new ResponseDto<>(false,"您的账号已经被禁用", null);
        }


        if(authService.checkPasswordByUidAndPassword(uid.get(), authDto.getPassword())){
            HttpSession session = SessionUtils.getSession();
            if(session != null){
                session.setAttribute("uid", uid.get());
                session.setAttribute("role", userRole.get());
            }
            clockService.startClock();
            return new ResponseDto<>(true, "登录成功", null);
        }
        else {
            return new ResponseDto<>(false,"密码错误！", null);
        }
    }

    @GetMapping("/logout")
    public ResponseDto<String> logout(){
        HttpSession session = SessionUtils.getSession();
        String logoutMessage = null;
        if(session != null){
            logoutMessage = clockService.closeClock();
            session.invalidate(); //销毁对应的session
        }
        return new ResponseDto<>(true,  "Logout successfully", logoutMessage);
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
            return new ResponseDto<>(true, "注册用户成功", authDto);
        }else{
            return new ResponseDto<>(false,"注册用户错误", authDto);
        }
    }
}
