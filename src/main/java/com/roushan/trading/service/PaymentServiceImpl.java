package com.roushan.trading.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.roushan.trading.domain.PAYMENT_METHOD;
import com.roushan.trading.domain.PAYMENT_ORDER_STATUS;
import com.roushan.trading.model.PaymentOrder;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.PaymentOrderRepository;
import com.roushan.trading.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.key}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder = this.paymentOrderRepository.save(paymentOrder);
        return  paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        Optional<PaymentOrder> paymentOrderOptional = this.paymentOrderRepository.findById(id);
        if(paymentOrderOptional.isPresent()){
            return paymentOrderOptional.get();
        }
        throw new Exception("Payment order not found");
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getPaymentOrderStatus().equals(PAYMENT_ORDER_STATUS.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PAYMENT_METHOD.RAZORPAY)){
                //TODO
                RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");
                if(status.equals("captured")){
                    paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.SUCCESS);
                    this.paymentOrderRepository.save(paymentOrder);
                    return true;
                }
                paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
                this.paymentOrderRepository.save(paymentOrder);
                return false;

            } else if (paymentOrder.getPaymentMethod().equals(PAYMENT_METHOD.STRIPE)) {
                //TODO
                StripeClient stripeClient = new StripeClient(stripeApiKey);

            }else{
                //TODO
            }
        }else{
            //TODO
        }
        return null;
    }

    @Override
    public PaymentResponse createRazorPayPaymentLink(User user, Long amount) {
       Long money = amount*100;
       try{
           //Instantiate a razorpay client
           RazorpayClient razorpay = new RazorpayClient(apiKey, stripeApiKey);

           //Create a Json with a payment link request
           JSONObject paymentLinkRequest = new JSONObject();
           paymentLinkRequest.put("amount", amount);
           paymentLinkRequest.put("currency", "INR");

           //Create a json object with the customer details
           JSONObject customer = new JSONObject();
           customer.put("email", user.getEmail());
           paymentLinkRequest.put("customer", customer);

           //create s Json object with the notification setting
           JSONObject notify = new JSONObject();
           notify.put("email", true);
           paymentLinkRequest.put("notify", notify);

           //set the remainder setting
           paymentLinkRequest.put("reminder_enable", true);

           //set the callback url
           paymentLinkRequest.put("callback_url","http://localhost:5455/wallet");
           paymentLinkRequest.put("callback_method", "get");

           //Create the payment link using the paymentLink.create() method
           PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

           String paymentLinkId = paymentLink.get("id");
           String paymentLinkUrl = paymentLink.get("short_url");

           PaymentResponse paymentResponse = new PaymentResponse();
           paymentResponse.setPayment_url(paymentLinkUrl);
           return paymentResponse;

       } catch (RazorpayException e) {
           log.info("Error creating payment link: "+e.getMessage());
           throw new RuntimeException(e);
       }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5455/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5455/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Top up waller")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        log.info("session ------- "+ session);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment_url(session.getUrl());
        return paymentResponse;

    }
}
