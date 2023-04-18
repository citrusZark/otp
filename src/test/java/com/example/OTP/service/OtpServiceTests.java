package com.example.OTP.service;

import com.example.OTP.model.Otp;
import com.example.OTP.repository.OtpRepository;
import com.example.OTP.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTests {
    @InjectMocks
    OtpServiceImpl otpService;

    @Mock
    OtpRepository otpRepository;

    @Mock
    UserRepository userRepository;

   @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.otpService.setOtpExpirationInMinutes(2);
        this.otpService.setFailedValidateOtpThreshold(5);
        this.otpService.setOtpIntervalInMinutes(1);
    }

    @Test
    public void validateOtpTest() {
        Otp mockOtp = new Otp();
        mockOtp.setUserId("Robert");
        mockOtp.setOtp("61531");
        mockOtp.setCreatedAt(LocalDateTime.now());
        mockOtp.setFailedValidateCount(0);
        when(otpRepository.findByUserId("Robert")).thenReturn(mockOtp);

        Boolean isOtpValid = otpService.validateOtp("Robert", "61531");
        assertTrue(isOtpValid);

    }

    @Test
    public void validateOtpFailedExceedFailThresholdTest() {
        Otp mockOtp = new Otp();
        mockOtp.setUserId("Robert");
        mockOtp.setOtp("61531");
        mockOtp.setCreatedAt(LocalDateTime.now());
        mockOtp.setFailedValidateCount(5);
        when(otpRepository.findByUserId("Robert")).thenReturn(mockOtp);
        Boolean isOtpValid = otpService.validateOtp("Robert", "61531");
        assertTrue(!isOtpValid);
    }

    @Test
    public void validateRequestOtpAfterIntervalTest() {
        Otp mockOtp = new Otp();
        mockOtp.setUserId("Robert");
        mockOtp.setOtp("61531");
        mockOtp.setCreatedAt(LocalDateTime.now());
        mockOtp.setFailedValidateCount(5);
        when(userRepository.existsByUserId("Robert")).thenReturn(true);
        when(otpRepository.findByUserId("Robert")).thenReturn(mockOtp);
        Otp requestedOtp = otpService.requestOtp("Robert");
        assertEquals(requestedOtp, null);
    }

    @Test
    public void UpdateExistingOtpTest() {
        Otp mockOtp = new Otp();
        mockOtp.setId(1);
        mockOtp.setUserId("Robert");
        mockOtp.setOtp("61531");
        mockOtp.setCreatedAt(LocalDateTime.now().minusMinutes(2));
        mockOtp.setFailedValidateCount(4);
        when(userRepository.existsByUserId("Robert")).thenReturn(true);
        when(otpRepository.findByUserId("Robert")).thenReturn(mockOtp);
        Otp requestedOtp = otpService.requestOtp("Robert");
        assertEquals(requestedOtp.getId(), 1);
        assertEquals(requestedOtp.getFailedValidateCount(), 0);
    }

}
