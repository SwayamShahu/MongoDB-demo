package com.app.analytics.service;

import com.app.analytics.model.Student;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;
    int min = 1000;
    int max = 9999;

    public void sentmail(String to){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("SMTP WORKING");
        message.setText("Hello from springboot - SMTP");
        mailSender.send(message);
    }

    public void otpMail(Student student){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(student.getMail());
        message.setSubject("Your OTP Code - Analytics Portal");
        message.setText("Hi, " + student.getName() + "\n" + "Your One Time Password (OTP) is: " + min + (int)(Math.random() * ((max - min) + 1)) +
                " This OTP is valid for 5 minutes.\n" +
                "Please do not share it with anyone." + "\n" + "Thanks," + "\n" +
                "Analytics Team");
        mailSender.send(message);
    }

    public void sendOtpMail(String to, String name, String otp) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);
        String html = """
            <h2>Hello %s,</h2>
            <p>Your OTP is:</p>
            <h1 style='letter-spacing:5px;color:green;'>%s</h1>
            <p>Valid for 5 minutes.</p>
            """.formatted(name, otp);
        helper.setTo(to);
        helper.setSubject("Your OTP Code - Analytics Portal");
        helper.setText(html, true); // TRUE = HTML
        mailSender.send(message);
    }

}
