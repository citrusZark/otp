package com.example.OTP.service;

import com.example.OTP.model.Otp;
import com.example.OTP.repository.OtpRepository;
import com.example.OTP.repository.UserRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.example.OTP.utils.Util.getRandomNumberString;

@Service
public class OtpServiceImpl implements OtpService {
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${otp.expiration.duration.minute}")
    @Getter
    @Setter
    private Integer otpExpirationInMinutes;

    @Value("${otp.interval.duration.minute}")
    @Getter
    @Setter
    private Integer otpIntervalInMinutes;


    @Value("${otp.failed.threshold}")
    @Getter
    @Setter
    private Integer failedValidateOtpThreshold;

    @Override
    public Otp requestOtp(String userId) {
        // check if user exist
        boolean isUserExist = userRepository.existsByUserId(userId);
        if (!isUserExist) {
            System.out.println("user did not exist");
            return null;
        }

        // check existing otp
        Otp otp = otpRepository.findByUserId(userId);

        Otp newOtp = new Otp();

        // check otp created_at + interval nya > now {return error}
        if (otp != null) {
            if (otp.getCreatedAt().plusMinutes(otpIntervalInMinutes).isAfter(LocalDateTime.now())) {
                return null;
            }

            // update existing otp
            newOtp = otp;

        }

        // save or update
        newOtp.setOtp(getRandomNumberString());
        newOtp.setUserId(userId);
        newOtp.setCreatedAt(LocalDateTime.now());
        newOtp.setFailedValidateCount(0);
        otpRepository.save(newOtp);

        return newOtp;
    }

    @Override
    public Boolean validateOtp(String userId, String otpInput) {
        // get otp by user id
        Otp otp = otpRepository.findByUserId(userId);

        if (otp == null) {
            return false;
        }

        // check failed count
        if (otp.getFailedValidateCount() >= failedValidateOtpThreshold) {
            return false;
        }

        // check otp
        if (!otp.getOtp().equals(otpInput)) {
            otp.setFailedValidateCount(otp.getFailedValidateCount()+ 1);
            return false;
        }

        // check expiration
        LocalDateTime datePlus2 = otp.getCreatedAt().plusMinutes(otpExpirationInMinutes);
        LocalDateTime otpValidUntil = datePlus2;
        if (!LocalDateTime.now().isBefore(otpValidUntil)) {
            otp.setFailedValidateCount(otp.getFailedValidateCount()+ 1);
            return false;
        }

        return true;
    }
}
