package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.UserDto;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.entity.UserAuth;
import com.example.book_store_back_end.repositories.UserRepository;
import com.example.book_store_back_end.services.UserServive;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserServive {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> getUserByUid(long uid){
       Optional<User> res = userRepository.findUserByUid(uid);
       return res.map(UserServiceImpl::mapToUserDto);
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
            return savedUser.getUid();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return -1; //保存错误，返回-1
        }
    }

    private static UserDto mapToUserDto(User user){
        return UserDto.builder()
                .uid(user.getUid())
                .level(user.getLevel())
                .avatarSrc(user.getAvatarSrc())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .name(user.getName())
                .build();
    }

    private static User mapToUser(AuthDto authDto){
        User user = User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .build();

        UserAuth userAuth =  UserAuth.builder()
                .password(authDto.getPassword())
                .user(user)
                .build();
        user.setUserAuth(userAuth);
        return user;
    }
}
