package com.roushan.trading.model;

import com.roushan.trading.domain.WALLET_TRANSACTION_TYPE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    private WALLET_TRANSACTION_TYPE walletTransactionType;

    private LocalDate date;

    private String transferId;

    private String purpose;

    private Long amount;
}
