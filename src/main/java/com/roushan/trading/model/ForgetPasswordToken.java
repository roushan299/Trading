package com.roushan.trading.model;


import com.roushan.trading.domain.VERIFICATION_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String otp;

    private String sendTo;

    private VERIFICATION_TYPE verificationType;

    @OneToOne
    private User user;
}
