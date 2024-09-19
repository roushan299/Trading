package com.roushan.trading.request;


import com.roushan.trading.domain.VERIFICATION_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPasswordTokenRequest {

    private String sendTo;

    private VERIFICATION_TYPE verificationType;
}
