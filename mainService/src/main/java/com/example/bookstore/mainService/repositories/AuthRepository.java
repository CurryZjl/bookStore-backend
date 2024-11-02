package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByUser_UidAndPassword(long uid, String password);
}
