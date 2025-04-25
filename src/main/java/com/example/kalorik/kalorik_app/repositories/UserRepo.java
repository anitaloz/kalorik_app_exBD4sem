package com.example.kalorik.kalorik_app.repositories;

import com.example.kalorik.kalorik_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
