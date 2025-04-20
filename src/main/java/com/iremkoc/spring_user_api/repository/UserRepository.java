package com.iremkoc.spring_user_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iremkoc.spring_user_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
