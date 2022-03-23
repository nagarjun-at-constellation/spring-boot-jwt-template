package com.bancocaminos.impactbackendapi.user.usecase.repository;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.user.usecase.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}
