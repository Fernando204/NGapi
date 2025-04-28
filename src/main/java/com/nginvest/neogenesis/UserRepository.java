package com.nginvest.neogenesis;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
} 
