package com.example.OTP.service;

import com.example.OTP.model.Otp;

public interface OtpService {
    Otp requestOtp(String userId);
    Boolean validateOtp(String userId, String otp);
}
