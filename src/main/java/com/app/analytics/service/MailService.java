package com.app.analytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;

    public void sentmail(String to){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("SMTP WORKING");
        message.setText("Hello from springboot - SMTP");
        mailSender.send(message);
    }
}
