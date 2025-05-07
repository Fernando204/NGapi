package com.nginvest.neogenesis.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nginvest.neogenesis.Model.User;
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
} 
