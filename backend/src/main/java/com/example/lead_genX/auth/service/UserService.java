package com.example.lead_genX.auth.service;

import com.example.lead_genX.CustomException.BusinessException;
import com.example.lead_genX.CustomException.ResourceNotFoundException;
import com.example.lead_genX.auth.entiy.UserEntity;
import com.example.lead_genX.auth.replydto.UserReplydto;
import com.example.lead_genX.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
UserRepository userRepository;

public UserReplydto save(UserEntity userEntity){
try {

    userEntity.setCreatedAt(LocalDateTime.now());
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
public void updatePassword(String email,String password){
    UserEntity userByEmail = findUserByEmail(email);
    save(userByEmail);
}
}