package com.app.analytics.controller;

import com.app.analytics.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OtpController {
    private final OtpService otpService;

    @GetMapping("/verfiy")
    public String verifyOtp(@RequestParam String mail, @RequestParam String otp){
        return otpService.verifyOtp(mail, otp);
    }
}
