package com.example.demo.repository;

import com.example.demo.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AuthRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);
}
