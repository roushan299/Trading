package com.roushan.trading.repository;

import com.roushan.trading.model.TwoFactorAuth;
import com.roushan.trading.model.TwoFactorOTP;
import com.roushan.trading.service.TwoFactorOTPService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TwoFactorOTPRepository extends JpaRepository<TwoFactorOTP, String> {


    TwoFactorOTP findByUserId(Long userId);


}
