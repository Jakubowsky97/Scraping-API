package com.example.demo.repository;

import com.example.demo.model.ScrapModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends MongoRepository<ScrapModel, String> {
    Optional<ScrapModel> findByTitle(String title);
    Boolean existsByTitle(String title);
}
