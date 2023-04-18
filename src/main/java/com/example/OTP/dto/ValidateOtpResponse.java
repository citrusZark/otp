package com.example.OTP.dto;

import lombok.Data;

@Data
public class ValidateOtpResponse {
    private String userId;
    private String message;
    private String error;
    private String errorDescription;
}
