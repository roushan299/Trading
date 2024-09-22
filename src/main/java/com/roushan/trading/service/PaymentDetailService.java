package com.roushan.trading.service;

import com.roushan.trading.model.PaymentDetails;
import com.roushan.trading.model.User;
import org.springframework.stereotype.Service;

@Service
public interface PaymentDetailService {

    PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName);

    PaymentDetails getUsersPaymentDetails(User user) throws Exception;
}
