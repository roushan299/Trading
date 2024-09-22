package com.roushan.trading.service;

import com.roushan.trading.model.User;
import com.roushan.trading.model.Withdrawal;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, User user);

    Withdrawal proceedWithWithdrawal(Long withdrawalId,boolean accept) throws Exception;

    List<Withdrawal> getUserWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();
}
