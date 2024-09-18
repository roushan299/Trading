package com.roushan.trading.service;

import com.roushan.trading.model.TwoFactorAuth;
import com.roushan.trading.model.TwoFactorOTP;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.TwoFactorOTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorServiceImpl implements TwoFactorOTPService{

    @Autowired
    private TwoFactorOTPRepository twoFactorOTPRepository;
    @Override
    public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        TwoFactorOTP twoFactorOTP = TwoFactorOTP.builder()
                .id(id)
                .otp(otp)
                .jwt(jwt)
                .user(user)
                .build();

        return  this.twoFactorOTPRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return  this.twoFactorOTPRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> opt = this.twoFactorOTPRepository.findById(id);
        return  opt.orElse( null);
    }

    @Override
    public boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp) {
        return  twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP) {
        this.twoFactorOTPRepository.delete(twoFactorOTP);
    }
}
