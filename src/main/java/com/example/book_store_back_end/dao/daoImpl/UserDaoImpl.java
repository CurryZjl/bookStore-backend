package com.example.book_store_back_end.dao.daoImpl;

import com.example.book_store_back_end.dao.UserDao;
import com.example.book_store_back_end.entity.User;
import com.example.book_store_back_end.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;

    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserByUid(long uid) {
        return userRepository.findUserByUid(uid);
    }

    @Override
    public Optional<Long> findUidByName(String name) {
        return userRepository.findUidByName(name);
    }

}
