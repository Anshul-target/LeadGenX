package com.example.lead_genX.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Userdto {
    @NotBlank(message = "Name must not be empty")
    String name;
@NotBlank(message = "Email must not be empty")
    String email;
@NotBlank(message = "Password must not be empty")
        @Size(min=8,message = "Password must be 8 characters long")
        @Pattern(regexp = "",
        message = "Password must contain at least one uppercase, one lowercase, and atleast one" +
                "digit and special character"
        )
    String password;
@NotBlank(message = "Confirm password must not be empty")
    String confirmPassword;

String address;



}
