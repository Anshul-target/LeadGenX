package com.example.lead_genX.auth.service;

import com.example.lead_genX.auth.entiy.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;


    @Async
    public void sendPasswordResetEmail(String to, String resetLink) {
        // Compose an email (text+HTML). Do not log raw token.
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Reset your password");
        msg.setText("Click the link to reset your password: " + resetLink + "\nThis link expires in 1 hour.");
        mailSender.send(msg);
    }
    @Async
    public void sendPasswordChangedNotification(String to) {
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Password update notice");
        msg.setText(
                "Your password was recently changed.\n\n" +
                        "If you performed this action, you can ignore this message.\n" +
                        "If you did NOT change your password, please contact support or secure your account immediately."
        );
        mailSender.send(msg);

    }
}





