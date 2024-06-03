package com.example.book_store_back_end.services;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.dto.AuthDto;
import com.example.book_store_back_end.dto.UserDto;
import org.hibernate.type.descriptor.jdbc.TinyIntJdbcType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserServive {
    Optional<UserDto> getUserByUid(long uid);
    Optional<Long> findUidByName(String name);
    boolean existsUserByName(String name);

    Optional<UserRole> findRoleByUid(long uid);

    long createUser(AuthDto authDto);

    boolean changeUserRole(long uid, UserRole userRole);

    List<UserDto> findAllSimpleUser();
}
