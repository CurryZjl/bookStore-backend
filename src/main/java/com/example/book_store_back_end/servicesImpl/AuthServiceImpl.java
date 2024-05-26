package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.repositories.AuthRepository;
import com.example.book_store_back_end.services.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
