package com.example.OTP.controller;

import com.example.OTP.dto.RequestOtpRequest;
import com.example.OTP.dto.RequestOtpResponse;
import com.example.OTP.dto.ValidateOtpRequest;
import com.example.OTP.dto.ValidateOtpResponse;
import com.example.OTP.model.Otp;
import com.example.OTP.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/request")
    public ResponseEntity<RequestOtpResponse> requestOtp(@RequestBody RequestOtpRequest request) {
        Otp result = otpService.requestOtp(request.getUserId());
        RequestOtpResponse response = new RequestOtpResponse();
        response.setUserId(result.getUserId());
        response.setOtp(result.getOtp());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateOtpResponse> validateOtp(@RequestBody ValidateOtpRequest request) {
        Boolean isOtpValid = otpService.validateOtp(request.getUserId(), request.getOtp());
        if (isOtpValid) {
            ValidateOtpResponse response = new ValidateOtpResponse();
            response.setUserId(request.getUserId());
            response.setMessage("OTP validated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        ValidateOtpResponse errorResponse = new ValidateOtpResponse();
        errorResponse.setError("otp_not_found");
        errorResponse.setErrorDescription("OTP Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


}
