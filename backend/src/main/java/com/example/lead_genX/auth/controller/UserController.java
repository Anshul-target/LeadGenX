package com.example.lead_genX.auth.controller;

import com.example.lead_genX.CustomException.BusinessException;
import com.example.lead_genX.auth.dto.Userdto;
import com.example.lead_genX.auth.entiy.UserEntity;
import com.example.lead_genX.auth.replydto.UserReplydto;
import com.example.lead_genX.auth.service.UserService;
import com.example.lead_genX.auth.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication api",description = "Endpoint for the User related operation")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
private UserUtil userUtil;
private  final UserService userService;
    @Operation(summary = "To register the user")
    @PostMapping("/register")
    public ResponseEntity<UserReplydto> register(@Valid Userdto userdto){
       if(!userUtil.isEmailCorrect(userdto.getEmail()) && !userUtil.isPasswordCorrect(userdto.getPassword(),userdto.getConfirmPassword()))
           throw new BusinessException("The email or password is incorrect");

        UserReplydto save = userService.save(UserEntity.toEntity(userdto));
return ResponseEntity.ok(save);

    }


}
