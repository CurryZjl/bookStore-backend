package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUid(long uid);

    @Query("SELECT user.uid FROM  User user WHERE user.email = :email ")
    Optional<Long> findUidByEmail(String email);

    @Query("SELECT user.password FROM User user WHERE user.uid = :uid")
    Optional<String> findPasswordByUid(long uid);

}
