package com.roushan.trading.service;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.ForgetPasswordToken;
import com.roushan.trading.model.User;
import org.springframework.stereotype.Service;

@Service
public interface ForgetPasswordService {

    ForgetPasswordToken createToken(User user, String id, String otp, VERIFICATION_TYPE verificationType, String sendTo);

    ForgetPasswordToken findById(String id);

    ForgetPasswordToken findByUser(Long userId);

    Boolean verifyToken(String otp, ForgetPasswordToken forgetPasswordToken);

    void deleteForgetPasswordToken(ForgetPasswordToken forgetPasswordToken);

}
