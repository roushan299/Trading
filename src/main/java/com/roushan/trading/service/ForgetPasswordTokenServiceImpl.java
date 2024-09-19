package com.roushan.trading.service;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.ForgetPasswordToken;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.ForgetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.PrimitiveIterator;

@Service
public class ForgetPasswordTokenServiceImpl implements ForgetPasswordService{

    @Autowired
    private ForgetPasswordTokenRepository forgetPasswordTokenRepository;
    @Override
    public ForgetPasswordToken createToken(User user, String id, String otp, VERIFICATION_TYPE verificationType, String sendTo) {
        ForgetPasswordToken forgetPasswordToken = new ForgetPasswordToken();
        forgetPasswordToken.setUser(user);
        forgetPasswordToken.setSendTo(sendTo);
        forgetPasswordToken.setOtp(otp);
        forgetPasswordToken.setVerificationType(verificationType);
        forgetPasswordToken.setId(id);

        return this.forgetPasswordTokenRepository.save(forgetPasswordToken);

    }

    @Override
    public ForgetPasswordToken findById(String id) {
        Optional<ForgetPasswordToken> opt = this.forgetPasswordTokenRepository.findById(id);
        return opt.orElse( null);
    }

    @Override
    public ForgetPasswordToken findByUser(Long userId) {
        return this.forgetPasswordTokenRepository.findByUserId(userId);
    }

    @Override
    public Boolean verifyToken(String otp, ForgetPasswordToken forgetPasswordToken) {
        return forgetPasswordToken.getOtp().equals(otp);
    }

    @Override
    public void deleteForgetPasswordToken(ForgetPasswordToken forgetPasswordToken) {
        this.forgetPasswordTokenRepository.delete(forgetPasswordToken);
    }
}
