package com.example.lead_genX.auth.controller;

import com.example.lead_genX.CustomException.BusinessException;
import com.example.lead_genX.auth.dto.Logindto;
import com.example.lead_genX.auth.dto.RefreshRequest;
import com.example.lead_genX.auth.dto.Userdto;
import com.example.lead_genX.auth.entiy.UserEntity;
import com.example.lead_genX.auth.replydto.LoginReplydto;
import com.example.lead_genX.auth.replydto.UserReplydto;
import com.example.lead_genX.auth.service.JwtService;
import com.example.lead_genX.auth.service.UserService;
import com.example.lead_genX.auth.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Authentication api",description = "Endpoint for the User related operation")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
private UserUtil userUtil=new UserUtil();
private  final UserService userService;
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;

    @Operation(summary = "To register the user")
    @PostMapping("/register")
    public ResponseEntity<UserReplydto> register(@Valid Userdto userdto){
       if(!userUtil.isEmailCorrect(userdto.getEmail()) && !userUtil.isPasswordCorrect(userdto.getPassword(),userdto.getConfirmPassword()))
           throw new BusinessException("The email or password is incorrect");

        UserReplydto save = userService.save(UserEntity.toEntity(userdto));
return ResponseEntity.ok(save);

    }

    @Operation(summary = "To Login the user")
    @PostMapping("/login")
    public ResponseEntity<LoginReplydto> login(@Valid Logindto userdto){
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userdto.getUsername(),userdto.getPassword());
        Authentication auth =authenticationManager.authenticate(token);
        if(!auth.isAuthenticated())
            throw new BusinessException("Credentials are invalid");
        UserEntity user = userService.findUserByEmail(userdto.getUsername());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);
        userService.saveToken(user,refreshToken);
        return ResponseEntity.ok(LoginReplydto.toLoginReplydto(accessToken,refreshToken,"Login successful"));
    }

@Operation(summary = "To generate the access token")
@PostMapping("/generate-accessToken")
public ResponseEntity<?> generateAccessToken(@RequestBody RefreshRequest request) {

    String refreshToken = request.refreshToken();




    if (jwtService.isTokenExpired(refreshToken))
        throw new BusinessException("Refresh token is expired");


    String username = jwtService.extractUsername(refreshToken);


    UserEntity user = userService.findUserByEmail(username);


    String accessToken = jwtService.generateAccessToken(user);

    return ResponseEntity.ok(
            userUtil.createTemplate("accessToken", accessToken)
    );
}
@Operation(summary = "To implement the forgot password")
@PostMapping("/forgot-password")
    public void forgotPassword(String email){
        UserEntity user = userService.findUserByEmail(email);

    }
}

