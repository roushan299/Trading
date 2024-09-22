package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.User;
import com.roushan.trading.model.Wallet;
import com.roushan.trading.model.Withdrawal;
import com.roushan.trading.service.UserService;
import com.roushan.trading.service.WalletService;
import com.roushan.trading.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalService withdrawalService;


    @GetMapping("/{amount}")
    public ResponseEntity<?> withdrawalRequest(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("amount") Long amount) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Wallet wallet = this.walletService.getUserWallet(user);
        Withdrawal withdrawal = this.withdrawalService.requestWithdrawal(amount, user);
        wallet = this.walletService.addBalanceToWallet(wallet, -amount);
        return  new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("id") Long id, @PathVariable("accept") boolean accept) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = this.withdrawalService.proceedWithWithdrawal(id, accept);
        Wallet wallet = this.walletService.getUserWallet(user);
        if(!accept){
            wallet = this.walletService.addBalanceToWallet(wallet, withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawalList = this.withdrawalService.getUserWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawalList, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawalList = this.withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawalList, HttpStatus.OK);
    }

}
