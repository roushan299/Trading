package com.roushan.trading.service;


import com.roushan.trading.model.TwoFactorAuth;
import com.roushan.trading.model.TwoFactorOTP;
import com.roushan.trading.model.User;
import org.springframework.stereotype.Service;

@Service
public interface TwoFactorOTPService {

    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt);


    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);

}
