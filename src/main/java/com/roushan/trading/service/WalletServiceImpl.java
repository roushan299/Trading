package com.roushan.trading.service;

import com.roushan.trading.domain.ORDER_TYPE;
import com.roushan.trading.model.Order;
import com.roushan.trading.model.User;
import com.roushan.trading.model.Wallet;
import com.roushan.trading.repository.OrderRepository;
import com.roushan.trading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = this.walletRepository.findByUserId(user.getId());

        if(wallet==null){
            Wallet newWallet = new Wallet();
            newWallet.setUser(user);
            this.walletRepository.save(newWallet);
            wallet = newWallet;
        }
        return wallet;
    }

    @Override
    public Wallet addBalanceToWallet(Wallet wallet, Long amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);
        wallet = this.walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> optional = this.walletRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransaction(User sender, Wallet recciverWallet, Long amount) throws Exception {
       Wallet senderwallet = this.getUserWallet(sender);

       if(senderwallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
           throw new Exception("Insufficient balance");
       }
       BigDecimal senderBalance = senderwallet.getBalance().subtract(BigDecimal.valueOf(amount));
       senderwallet.setBalance(senderBalance);
       this.walletRepository.save(senderwallet);

       BigDecimal recciverBalance = recciverWallet.getBalance().add(BigDecimal.valueOf(amount));
       recciverWallet.setBalance(recciverBalance);
       this.walletRepository.save(recciverWallet);
        return senderwallet;
    }


    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
       Wallet wallet = this.getUserWallet(user);

       if(order.getOrderType().equals(ORDER_TYPE.BUY)){
           BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
           if(newBalance.compareTo(order.getPrice())<0){
               throw new Exception("Insufficient funds for this transaction");
           }
           wallet.setBalance(newBalance);

       }else {
           BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
           wallet.setBalance(newBalance);
       }
        wallet = this.walletRepository.save(wallet);
       return wallet;
    }


}
