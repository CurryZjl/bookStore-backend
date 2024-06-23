package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.UserDto;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.entity.UserAuth;
import com.example.book_store_back_end.mapper.UserAuthMapper;
import com.example.book_store_back_end.repositories.AuthRepository;
import com.example.book_store_back_end.repositories.UserRepository;
import com.example.book_store_back_end.services.UserServive;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServive {
    UserRepository userRepository;
    AuthRepository authRepository;

    public UserServiceImpl(UserRepository userRepository , AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    public Optional<UserDto> getUserByUid(long uid){
       Optional<User> res = userRepository.findUserByUid(uid);
       return res.map(UserServiceImpl::mapToUserDto);
    }

    @Override
    public String findNameByUid(long uid) {
        return userRepository.findNameByUid(uid);
    }

    @Override
    public boolean existsUserByName(String name) {
        return userRepository.existsUserByName(name);
    }

    @Override
    public Optional<Long> findUidByName(String name) {
        return userRepository.findUidByName(name);
    }

    @Override
    public long createUser(AuthDto authDto) {
        try{
            User user = mapToUser(authDto);
            User savedUser = userRepository.save(user);
            UserAuth userAuth = UserAuthMapper.mapToUserAuth(authDto,user);
            authRepository.save(userAuth);
            return savedUser.getUid();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return -1; //保存错误，返回-1
        }
    }

    @Override
    public Optional<UserRole> findRoleByUid(long uid){
        return userRepository.findRoleByUid(uid);
    }

    @Override
    public boolean changeUserRole(long uid, UserRole userRole) {
        Optional<User> user = userRepository.findUserByUid(uid);
        if(user.isPresent()){
            User user1 = user.get();
            user1.setRole(userRole);
            userRepository.save(user1);
            return true;
        }
        return false;
    }

    @Override
    public List<UserDto> findAllSimpleUser() {
        List<User> users = userRepository.findAllByRoleNotEqual(UserRole.ADMIN);
        return users.stream()
                .map(UserServiceImpl::mapToUserDto)
                .collect(Collectors.toList());
    }

    private static UserDto mapToUserDto(User user){
        return UserDto.builder()
                .uid(user.getUid())
                .level(user.getLevel())
                .avatarSrc(user.getAvatarSrc())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .name(user.getName())
                .userRole(user.getRole())
                .build();
    }


    /* 用于注册用户，默认都为普通用户 */
    private static User mapToUser(AuthDto authDto){
        User user = User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .role(UserRole.NORMAL)
                .build();
        return user;
    }
}
