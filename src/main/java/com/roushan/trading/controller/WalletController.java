package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.*;
import com.roushan.trading.service.OrderService;
import com.roushan.trading.service.PaymentService;
import com.roushan.trading.service.UserService;
import com.roushan.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Wallet wallet = this.walletService.getUserWallet(user);
        return ResponseEntity.accepted().body(wallet);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("walletId") Long walletId, @RequestBody WalletTransaction walletTransaction) throws Exception {
        User senderUser = this.userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = this.walletService.findWalletById(walletId);
        Wallet wallet = this.walletService.walletToWalletTransaction(senderUser, receiverWallet, walletTransaction.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("orderId") Long orderId) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.getOrderById(orderId);
        Wallet wallet = this.walletService.payOrderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("orderId") Long orderId) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.getOrderById(orderId);
        Wallet wallet = this.walletService.payOrderPayment(order, user);
        return new ResponseEntity<Wallet>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @RequestParam(name = "order_id") Long orderId, @RequestParam(name = "payment_id") String paymentId) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Wallet wallet = this.walletService.getUserWallet(user);
        PaymentOrder paymentOrder = this.paymentService.getPaymentOrderById(orderId);
        Boolean status = this.paymentService.proceedPaymentOrder(paymentOrder, paymentId);
        if(wallet.getBalance() == null){
            wallet.setBalance(BigDecimal.valueOf(0));
        }
        if(status){
            wallet = this.walletService.addBalanceToWallet(wallet, paymentOrder.getAmount());
        }
        return new ResponseEntity<Wallet>(wallet, HttpStatus.ACCEPTED);
    }

}
