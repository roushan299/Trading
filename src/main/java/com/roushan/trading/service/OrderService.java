package com.roushan.trading.service;

import com.roushan.trading.domain.ORDER_TYPE;
import com.roushan.trading.model.Coin;
import com.roushan.trading.model.Order;
import com.roushan.trading.model.OrderItem;
import com.roushan.trading.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, ORDER_TYPE orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrderOfUser(Long userId, ORDER_TYPE orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, ORDER_TYPE orderType, User user) throws Exception;

}
