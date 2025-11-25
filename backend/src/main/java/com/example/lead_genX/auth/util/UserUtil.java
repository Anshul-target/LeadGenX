package com.example.lead_genX.auth.util;

import java.util.regex.Pattern;

public class UserUtil {
   public static final String emailRegx="^[A-Za-z0-9+_.-]+@(.+)$";
    public boolean isPasswordCorrect(String password,String confirmPassword){

        return
                password!=null
                && confirmPassword!=null
                && !password.isEmpty()
                && !confirmPassword.isEmpty()
                && password.equals(confirmPassword);
    }
    public boolean isEmailCorrect(String email){
 return email!=null &&
         !email.isEmpty()
         && Pattern.matches(emailRegx,email);
    }
}
