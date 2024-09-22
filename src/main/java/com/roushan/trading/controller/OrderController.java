package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.domain.ORDER_TYPE;
import com.roushan.trading.model.Coin;
import com.roushan.trading.model.Order;
import com.roushan.trading.model.User;
import com.roushan.trading.request.CreateOrderRequest;
import com.roushan.trading.service.CoinService;
import com.roushan.trading.service.OrderService;
import com.roushan.trading.service.UserService;
import com.roushan.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WalletService walletService;


    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @RequestBody CreateOrderRequest createOrderRequest) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Coin coin = this.coinService.findById(createOrderRequest.getCoinId());
        Order order = this.orderService.processOrder(coin, createOrderRequest.getQuantity(), createOrderRequest.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("orderId") Long orderId) throws Exception {
        if(jwt == null){
            throw new Exception("Authorization token is missing");
        }
        User user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.getOrderById(orderId);
        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @RequestParam(required = false)ORDER_TYPE orderType, @RequestParam(required = false) String asset_Symbol) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Order> orderList = this.orderService.getAllOrderOfUser(user.getId(), orderType, asset_Symbol);
        return ResponseEntity.ok(orderList);
    }


}
