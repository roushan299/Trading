package com.roushan.trading.request;


import com.roushan.trading.domain.ORDER_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private ORDER_TYPE orderType;
}
