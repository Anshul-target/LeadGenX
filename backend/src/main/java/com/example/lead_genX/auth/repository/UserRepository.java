package com.example.lead_genX.auth.repository;

import com.example.lead_genX.auth.entiy.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity,String> {

}
