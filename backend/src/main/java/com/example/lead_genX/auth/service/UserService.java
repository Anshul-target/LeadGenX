package com.example.lead_genX.auth.service;

import com.example.lead_genX.CustomException.BusinessException;
import com.example.lead_genX.CustomException.ResourceNotFoundException;
import com.example.lead_genX.auth.entiy.UserEntity;
import com.example.lead_genX.auth.replydto.UserReplydto;
import com.example.lead_genX.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class UserService implements UserDetailsService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

public UserService(UserRepository repository,PasswordEncoder passwordEncoder){
    this.userRepository=repository;
    this.passwordEncoder=passwordEncoder;
}

    public  void saveToken(UserEntity user, String accessToken) {
    user.setToken(accessToken);
    userRepository.save(user);
    }


    @CacheEvict(value = "users",key = "#userEntity.email")
public UserReplydto save(UserEntity userEntity){
try {
    userEntity.setCreatedAt(LocalDateTime.now());
    String hashPasswrd=passwordEncoder.encode(userEntity.getPassword());
    userEntity.setPassword(hashPasswrd);
    UserEntity save = userRepository.save(userEntity);
    return UserReplydto.fromEntity(save);
}
catch (Exception e){
throw new BusinessException("Failed to save the user: "+ e.getMessage());
}
}
    public UserReplydto getUserById(String id){
     UserEntity user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User is not found"));
    return  UserReplydto.fromEntity(user);
}

public void findUserExistById(String id){
    userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No user exists of this id"));
}
@CacheEvict(value = "users",key = "#id")
public void deleteUser(String id){
    findUserExistById(id);
    userRepository.deleteById(id);
}

public UserEntity findUserByEmail(String email){
    UserEntity user=userRepository.findByEmail(email).orElseThrow(()->
            new ResourceNotFoundException("Email not found")
            );
    return user;
}
@CacheEvict(value = "users",key = "#email")
public void updatePassword(String email,String password){
    UserEntity userByEmail = findUserByEmail(email);
    save(userByEmail);
}

    @Override
    @Cacheable(value = "users",key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findUserByEmail(username);

        return new AuthUserService(user);

    }
}