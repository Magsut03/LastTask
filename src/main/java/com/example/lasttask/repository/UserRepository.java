package com.example.lasttask.repository;

import com.example.lasttask.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from users u " +
            "where not(u.state = 'DELETED') and u.email = ?1")
    Optional<UserEntity> findByEmail(String email);

    @Query("select u from users u " +
            "where not(u.state = 'DELETED') and u.role = 'ROLE_USER'")
    List<UserEntity> findAllUsers();

    @Query("select u from users u " +
            "where not(u.state = 'DELETED') and u.role = 'ROLE_ADMIN' and u.id = 1")
    List<UserEntity> findAllAdmins();

}
