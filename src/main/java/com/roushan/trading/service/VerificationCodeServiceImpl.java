package com.roushan.trading.service;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.User;
import com.roushan.trading.model.VerificationCode;
import com.roushan.trading.repository.VerificationCodeRepository;
import com.roushan.trading.util.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationOtp(User user, VERIFICATION_TYPE verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOtp());
        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);

        return this.verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> opt = this.verificationCodeRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new Exception("Verification code not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) throws Exception {
        VerificationCode verificationCode = this.verificationCodeRepository.findByUserId(userId);
        if(verificationCode==null){
            throw new Exception("Verification code not found");
        }
        return verificationCode;
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        this.verificationCodeRepository.delete(verificationCode);
    }

    @Override
    public boolean verifyOtp(String otp, VerificationCode verificationCode) {
        return verificationCode.getOtp().equals(otp);
    }
}
