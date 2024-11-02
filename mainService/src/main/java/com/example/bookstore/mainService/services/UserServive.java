package com.example.bookstore.mainService.services;

import com.example.bookstore.mainService.constants.UserRole;
import com.example.bookstore.mainService.dto.AuthDto;
import com.example.bookstore.mainService.dto.UserDto;
import org.hibernate.type.descriptor.jdbc.TinyIntJdbcType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserServive {
    Optional<UserDto> getUserByUid(long uid);
    String findNameByUid(long uid);
    Optional<Long> findUidByName(String name);
    boolean existsUserByName(String name);

    Optional<UserRole> findRoleByUid(long uid);

    long createUser(AuthDto authDto);

    boolean changeUserRole(long uid, UserRole userRole);

    List<UserDto> findAllSimpleUser();
}
