package com.roushan.trading.repository;

import com.roushan.trading.model.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordTokenRepository extends JpaRepository<ForgetPasswordToken, String> {

    ForgetPasswordToken findByUserId(Long userId);
}
