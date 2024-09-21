package com.roushan.trading.service;

import com.roushan.trading.model.Order;
import com.roushan.trading.model.User;
import com.roushan.trading.model.Wallet;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalanceToWallet(Wallet wallet, Long amount);

    Wallet findWalletById(Long id) throws Exception;

    Wallet walletToWalletTransaction(User  sender, Wallet recciverWallet, Long amount) throws Exception;

    Wallet payOrderPayment(Order order,User user) throws Exception;

}
