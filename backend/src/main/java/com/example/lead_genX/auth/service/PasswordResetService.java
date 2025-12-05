package com.example.lead_genX.auth.service;

import com.example.lead_genX.CustomException.BusinessException;
import com.example.lead_genX.auth.entiy.PasswordResetToken;
import com.example.lead_genX.auth.entiy.UserEntity;
import com.example.lead_genX.auth.repository.PasswordResetTokenRepository;
import com.example.lead_genX.auth.repository.UserRepository;
import com.example.lead_genX.auth.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;
    private final MongoOperations mongoOps;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private static final int TOKEN_BYTES = 32;
    private static final long TTL_SECONDS = 3600;

    public void requestPasswordReset(String email,String url){
    userRepository.findByEmail(email).ifPresent(user->{
        tokenRepository.findByUserIdAndUsedFalse(user.getId()).forEach(t->{
            t.setUsed(true);tokenRepository.save(t);
            String rawToken= TokenUtil.generateToken(TOKEN_BYTES);
            String tokenHash=TokenUtil.sha256Hex(rawToken);
            PasswordResetToken token = PasswordResetToken.builder().tokenHash(tokenHash).userId(user.getId()).createdAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(TTL_SECONDS)).build();
            tokenRepository.save(token);
            String resetLink=url+"/reset-password?token="+ URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
            emailService.sendPasswordResetEmail(user.getEmail(),resetLink);

        });
    });

}
    public void resetPassword(String rawToken, String newPassword) {
        String tokenHash = TokenUtil.sha256Hex(rawToken);

        // Atomic find + mark used
        Query q = new Query();
        q.addCriteria(Criteria.where("tokenHash").is(tokenHash));
        q.addCriteria(Criteria.where("used").is(false));
        q.addCriteria(Criteria.where("expiresAt").gt(Instant.now()));

        Update update = new Update().set("used", true);

        PasswordResetToken token = mongoOps.findAndModify(q, update, PasswordResetToken.class);
        if (token == null) {
            throw new BusinessException("Invalid or expired token");
        }

        // Load user
        UserEntity user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

        // Validate password strength (implement your own checks)
        validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        // used to invalidate tokens
        // OR increment tokenVersion: user.setTokenVersion(user.getTokenVersion() + 1);


        // Revoke refresh tokens / sessions (implementation dependent)
        jwtService.revokeAllTokensForUser(user);
        userRepository.save(user);

        // Notify user
        emailService.sendPasswordChangedNotification(user.getEmail());
    }

    private void validatePassword(String pwd) {
        if (pwd.length() < 8) throw new BusinessException("Password too weak");
        // add checks against breached-passwords or complexity
    }

}