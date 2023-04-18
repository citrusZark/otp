package com.example.OTP.repository;

import com.example.OTP.model.Otp;
import org.springframework.data.repository.CrudRepository;

public interface OtpRepository extends CrudRepository<Otp, Integer> {
    Otp findByUserId(String userId);
}
