package com.example.lead_genX.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Logindto {
    @NotBlank(message = "Please provide the username")
    String username;
    @NotBlank(message = "Please give the password")
    String password;
}


