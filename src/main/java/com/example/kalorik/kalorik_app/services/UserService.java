package com.example.kalorik.kalorik_app.services;

import com.example.kalorik.kalorik_app.domain.User;
import com.example.kalorik.kalorik_app.repositories.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo)
    {
        this.userRepo=userRepo;
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
