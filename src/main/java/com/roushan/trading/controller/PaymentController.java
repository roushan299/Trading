package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.PaymentOrder;
import com.roushan.trading.model.User;
import com.roushan.trading.response.PaymentResponse;
import com.roushan.trading.service.PaymentService;
import com.roushan.trading.service.UserService;
import com.roushan.trading.domain.PAYMENT_METHOD;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable("paymentMethod")PAYMENT_METHOD paymentMethod, @PathVariable Long amount, @RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse = new PaymentResponse();
        PaymentOrder order = this.paymentService.createOrder(user, amount, paymentMethod);

        if(paymentMethod.equals(PAYMENT_METHOD.RAZORPAY)){
            paymentResponse = this.paymentService.createRazorPayPaymentLink(user, amount, order.getId());
        }else {
            paymentResponse = this.paymentService.createStripePaymentLink(user, amount, order.getId());
        }
        return new ResponseEntity<PaymentResponse>(paymentResponse, HttpStatus.CREATED);
    }



}
