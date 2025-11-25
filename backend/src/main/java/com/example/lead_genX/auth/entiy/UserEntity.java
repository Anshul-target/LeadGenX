package com.example.lead_genX.auth.entiy;

import com.example.lead_genX.auth.dto.Userdto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "userAuth")
@Builder
@Data
public class UserEntity {
    @Id
private String id;
    private String name;
    private String email;
    private String password;
    private String role="User";
    private LocalDateTime createdAt;

    public static UserEntity toEntity(Userdto userdto){
        return UserEntity.builder()
                .email(userdto.getEmail())
                .name(userdto.getName())
                .password(userdto.getPassword())
                .build();
    }
}
