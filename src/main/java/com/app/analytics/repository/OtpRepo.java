package com.app.analytics.repository;

import com.app.analytics.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OtpRepo extends MongoRepository<Otp, String> {
    Optional<Otp> findByMailAndOtpAndExpireTimeAfter(String mail, String otp, long time);
}