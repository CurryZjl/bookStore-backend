package com.example.book_store_back_end.dto;

import com.example.book_store_back_end.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long uid;
    private String name;
    private int level;
    private String email;
    private String introduction;
    private String avatarSrc;
    private UserRole userRole;
}
