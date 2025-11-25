package com.example.lead_genX.auth.controller;

import com.example.lead_genX.auth.dto.Userdto;
import com.example.lead_genX.auth.replydto.UserReplydto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication api",description = "Endpoint for the User related operation")
@RequestMapping("/auth")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<UserReplydto>register(Userdto userdto){

    }

}
