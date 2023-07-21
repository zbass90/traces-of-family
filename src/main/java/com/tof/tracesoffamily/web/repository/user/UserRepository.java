package com.tof.tracesoffamily.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // oauth2
    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);

    // oauth2
    Boolean existsByEmail(String email);
}
