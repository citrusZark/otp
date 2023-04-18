package com.example.OTP.dto;

import lombok.Data;

@Data
public class ValidateOtpRequest {
    private String userId;
    private String otp;
}
