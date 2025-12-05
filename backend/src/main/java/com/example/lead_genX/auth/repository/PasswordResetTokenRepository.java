package com.example.lead_genX.auth.repository;

import com.example.lead_genX.auth.entiy.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken,String> {

    List<PasswordResetToken> findByUserIdAndUsedFalse(String id);
}
