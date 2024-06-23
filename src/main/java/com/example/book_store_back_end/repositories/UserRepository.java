package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUid(long uid);

    @Query("SELECT user.name FROM User user WHERE user.uid = :uid")
    String findNameByUid(long uid);

    @Query("SELECT user.uid FROM  User user WHERE user.name = :name ")
    Optional<Long> findUidByName(String name);

    @Query("SELECT  user.role FROM User user WHERE user.uid = :uid")
    Optional<UserRole> findRoleByUid(long uid);

    boolean existsUserByName(String name);

    @Query("SELECT user FROM User user WHERE user.role<> :role ")
    List<User> findAllByRoleNotEqual(UserRole role);

}
