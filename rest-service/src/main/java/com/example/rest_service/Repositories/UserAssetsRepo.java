package com.example.rest_service.Repositories;

import com.example.rest_service.Entities.UserAssets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAssetsRepo extends JpaRepository<UserAssets, Long> {
    UserAssets findByUsername(String username);
}
