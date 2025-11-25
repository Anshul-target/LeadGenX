package com.example.lead_genX.auth.repository;

import com.example.lead_genX.auth.entiy.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity,String> {

    Optional<UserEntity> findByEmail(String email);
}
