package com.example.book_store_back_end.services;

import java.util.Optional;

public interface AuthService {
    boolean checkPasswordByUidAndPassword(long uid, String password);
}
