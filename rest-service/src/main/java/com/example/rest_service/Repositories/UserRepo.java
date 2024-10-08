package com.example.rest_service.Repositories;

import com.example.rest_service.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository <User, Long> {
    User findByUsername(String username);
}
