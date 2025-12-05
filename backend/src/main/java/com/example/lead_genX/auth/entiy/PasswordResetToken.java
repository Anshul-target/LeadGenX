package com.example.lead_genX.auth.entiy;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "password-reset_token")
@Data
@Builder
public class PasswordResetToken {
    private String id;
    private String userId;
    @Indexed(unique = true)
    private String tokenHash;
    private Instant createdAt;
    @Indexed(expireAfter = "0")
    private  Instant expiresAt;
    private boolean used=false;


}
