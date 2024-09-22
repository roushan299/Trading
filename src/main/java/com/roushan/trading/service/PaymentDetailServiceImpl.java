package com.roushan.trading.service;

import com.roushan.trading.model.PaymentDetails;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.PaymentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName) {
       PaymentDetails paymentDetails = PaymentDetails.builder()
               .accountNumber(accountNumber)
               .accountHolderName(accountHolderName)
               .ifsc(ifsc)
               .bankName(bankName)
               .build();
       paymentDetails = this.paymentDetailRepository.save(paymentDetails);
       return paymentDetails;
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(User user) throws Exception {
        PaymentDetails paymentDetails = this.paymentDetailRepository.findByUserId(user.getId());
        if(paymentDetails == null){
            throw new Exception("No payment details exits");
        }
        return paymentDetails;
    }
}
