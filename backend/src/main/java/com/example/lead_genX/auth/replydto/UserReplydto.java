package com.example.lead_genX.auth.replydto;

import com.example.lead_genX.auth.entiy.UserEntity;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class UserReplydto {
private String id;
private String name;
private String email;
private LocalDateTime createdAt;
private String role;
public static UserReplydto fromEntity(UserEntity user){
    return UserReplydto
            .builder()
            .name(user.getName())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt())
            .id(user.getId())
            .role(user.getRole())
            .build();
}
}
