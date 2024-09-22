package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.PaymentDetails;
import com.roushan.trading.model.User;
import com.roushan.trading.service.PaymentDetailService;
import com.roushan.trading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paymentDetail")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @RequestBody PaymentDetails paymentDetailsRequest) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailService.addPaymentDetails(paymentDetailsRequest.getAccountNumber(), paymentDetailsRequest.getAccountHolderName(), paymentDetailsRequest.getIfsc(), paymentDetailsRequest.getBankName());
        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PaymentDetails> getPaymentDetails(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailService.getUsersPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }

}
