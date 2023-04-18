package com.example.OTP.dto;

import lombok.Data;

@Data
public class RequestOtpResponse {
    private String userId;
    private String otp;

}
