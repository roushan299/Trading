package com.roushan.trading.service;

import com.roushan.trading.domain.WITHDRAWAL_STATUS;
import com.roushan.trading.model.User;
import com.roushan.trading.model.Withdrawal;
import com.roushan.trading.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService{

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
       Withdrawal withdrawal = new Withdrawal();
       withdrawal.setAmount(amount);
       withdrawal.setUser(user);
       withdrawal.setWithdrawalStatus(WITHDRAWAL_STATUS.PENDING);
       withdrawal = this.withdrawalRepository.save(withdrawal);
       return withdrawal;
    }

    @Override
    public Withdrawal proceedWithWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawalOptional = this.withdrawalRepository.findById(withdrawalId);

        if(withdrawalOptional.isEmpty()){
            throw new Exception("Withdrwal is not found");
        }
        Withdrawal withdrawal = withdrawalOptional.get();
        withdrawal.setDate(LocalDateTime.now());
        WITHDRAWAL_STATUS withdrawalStatus = accept ? WITHDRAWAL_STATUS.SUCCESS : WITHDRAWAL_STATUS.DECLINED;
        withdrawal.setWithdrawalStatus(withdrawalStatus);
        withdrawal = this.withdrawalRepository.save(withdrawal);
        return withdrawal;
    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        List<Withdrawal> withdrawalList = this.withdrawalRepository.findByUserId(user.getId());
        return withdrawalList;
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        List<Withdrawal> withdrawalList = this.withdrawalRepository.findAll();
        return withdrawalList;
    }
}
