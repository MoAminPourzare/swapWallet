package com.example.rest_service.Services;

import com.example.rest_service.Entities.User;
import com.example.rest_service.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void addUser(User newUser) {
        userRepo.save(newUser);
    }
}
