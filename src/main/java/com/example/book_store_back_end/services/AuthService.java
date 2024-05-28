package com.example.book_store_back_end.services;


public interface AuthService {
    boolean checkPasswordByUidAndPassword(long uid, String password);
}
