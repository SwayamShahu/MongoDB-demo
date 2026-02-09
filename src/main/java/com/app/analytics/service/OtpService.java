package com.app.analytics.service;

import com.app.analytics.model.Otp;
import com.app.analytics.model.Student;
import com.app.analytics.repository.OtpRepo;
import com.app.analytics.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OtpService {
    private final StudentRepo studentRepo;
    private final OtpRepo otpRepo;
    public String verifyOtp(String mail, String otp){
        Otp o = otpRepo.findByMailAndOtpAndExpireTimeAfter(mail, otp, System.currentTimeMillis()).orElse(null);

        if (otp == null){
            return "Invalid Otp";
        }
        o.setUsed(true);
        otpRepo.save(o);
        Student stu = studentRepo.findByMail(mail).orElseThrow(() -> new RuntimeException("Something went wrong"));
        stu.setVerified(true);
        studentRepo.save(stu);
        return "Successfully opt verfied";




//        Otp o = otpRepo.findByMail(mail).orElseThrow(() -> new RuntimeException("Something went wrong"));
//
//        long currentTimeMillis = System.currentTimeMillis();
//        long expireTimeMillis = o.getCreatedAT() + (5 * 60 * 1000);
//
//        if (!o.getOtp().equals(otp)){
//            return "Invalid OTP, please try again";
//        }
//        if (o.isUsed()) {
//            return "Otp is already used, please try again";
//        }
//        if (currentTimeMillis > expireTimeMillis){
//            return "Try again, you have gone late";
//        }
//        o.setUsed(true);
//        otpRepo.save(o);
//        return "Successfully";
    }
}
