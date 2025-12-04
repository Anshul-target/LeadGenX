package com.example.lead_genX.auth.replydto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public record LoginReplydto(String accessToken, String refreshToken, String message) {
   public static LoginReplydto toLoginReplydto(String accessToken, String refreshToken, String message){
       return new LoginReplydto(refreshToken,refreshToken,message);
   }
}


