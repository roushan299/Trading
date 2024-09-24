package com.roushan.trading.model;

import com.roushan.trading.domain.PAYMENT_METHOD;
import com.roushan.trading.domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PAYMENT_ORDER_STATUS paymentOrderStatus;

    private PAYMENT_METHOD paymentMethod;

    @ManyToOne
    private User user;
}
