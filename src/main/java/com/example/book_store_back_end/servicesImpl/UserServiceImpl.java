package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.UserDto;
import com.example.book_store_back_end.entity.User;
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

    private static UserDto mapToUserDto(User user){
        return UserDto.builder()
                .uid(user.getUid())
                .level(user.getLevel())
                .avatarSrc(user.getAvatarSrc())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
