package com.roushan.trading.service;

import com.razorpay.RazorpayException;
import com.roushan.trading.domain.PAYMENT_METHOD;
import com.roushan.trading.model.PaymentOrder;
import com.roushan.trading.model.User;
import com.roushan.trading.response.PaymentResponse;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorPayPaymentLink(User user, Long amount, Long orderId);

    PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;




}
