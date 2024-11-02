package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.constants.UserRole;
import com.example.bookstore.mainService.dto.ResponseDto;
import com.example.bookstore.mainService.dto.UserDto;
import com.example.bookstore.mainService.services.UserServive;
import com.example.bookstore.mainService.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/role")
    public ResponseDto<UserRole> getRole(){
        UserRole userRole = SessionUtils.getCurrentRole();
        if(userRole.equals(UserRole.ERROR)){
            return new ResponseDto(false,"GET ERROR ROLE", null );
        }
        else
            return new ResponseDto<>(true, "GET OK", userRole);
    }

    @GetMapping("/all")
    public ResponseDto<List<UserDto>> getAllUsers(){
        UserRole userRole = SessionUtils.getCurrentRole();
        if(!userRole.equals(UserRole.ADMIN)){
            return new ResponseDto<>(false, "非管理员不可操作", null);
        }
        List<UserDto> userDtos;
        try{
            userDtos = userServive.findAllSimpleUser();
            return new ResponseDto<>(true,"拿取全部普通用户信息成功", userDtos);
        }catch (Exception e){
            return new ResponseDto<>(false, "拿取全部用户信息失败", null);
        }
    }

    @PatchMapping("/{uid}")
    public ResponseDto<String> changeUserRole(@PathVariable("uid") long uid, @RequestBody String roleS){
        UserRole userRole = SessionUtils.getCurrentRole();
        if(!userRole.equals(UserRole.ADMIN)){
            return new ResponseDto<>(false, "非管理员不可操作", null);
        }
        UserRole userRole1;
        try {
           userRole1 = UserRole.valueOf(roleS);
        } catch (Exception e){
            return new ResponseDto<>(false,"错误角色", null);
        }

        if(!userServive.changeUserRole(uid,userRole1)){
            return new ResponseDto<>(false,"修改角色错误", null);
        }
        return new ResponseDto<>(true,"成功修改%d号用户角色为：%s".formatted(uid , userRole1.getRoleName()), null);
    }
}