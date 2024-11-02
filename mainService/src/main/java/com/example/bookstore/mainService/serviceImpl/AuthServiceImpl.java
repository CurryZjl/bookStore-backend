package com.example.bookstore.mainService.serviceImpl;


import com.example.bookstore.mainService.repositories.AuthRepository;
import com.example.bookstore.mainService.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    private final AuthRepository authRepository;

    @Override
    public boolean checkPasswordByUidAndPassword(long uid, String password) {
        return authRepository.existsByUser_UidAndPassword(uid, password);
    }
}
