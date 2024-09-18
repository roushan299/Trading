package com.roushan.trading.util;

import java.util.Random;

public class OtpUtils {

    private static  final int OTP_LENGTH = 6;

    public static  String generateOtp(){
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for(int index = 0;index<OTP_LENGTH;index++){
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

}
