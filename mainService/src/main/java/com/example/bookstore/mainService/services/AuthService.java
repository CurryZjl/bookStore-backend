package com.example.bookstore.mainService.services;


public interface AuthService {
    boolean checkPasswordByUidAndPassword(long uid, String password);
}
