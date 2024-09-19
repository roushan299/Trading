package com.roushan.trading.service;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.User;
import com.roushan.trading.model.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {

    VerificationCode sendVerificationOtp(User user, VERIFICATION_TYPE verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId) throws Exception;

    void deleteVerificationCode(VerificationCode verificationCode);

    boolean verifyOtp(String otp, VerificationCode verificationCode);
}
