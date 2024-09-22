package com.roushan.trading.service;

import com.roushan.trading.domain.ORDER_STATUS;
import com.roushan.trading.domain.ORDER_TYPE;
import com.roushan.trading.model.*;
import com.roushan.trading.repository.OrderItemRepository;
import com.roushan.trading.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AssetService assetService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, ORDER_TYPE orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimeStamp(LocalDateTime.now());
        order.setStatus(ORDER_STATUS.PENDING);
        order = this.orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        Optional<Order> optional = this.orderRepository.findById(orderId);
        if(optional.isPresent()){
            return optional.get();
        }
        throw new Exception("Order not found");
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, ORDER_TYPE orderType, String assetSymbol) {
        List<Order> orderList = this.orderRepository.findByUserId(userId);
        return orderList;
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, ORDER_TYPE orderType, User user) throws Exception {
        if(orderType.equals(ORDER_TYPE.BUY)){
            return this.buyAsset(coin, quantity, user);
        }else if(orderType.equals(ORDER_TYPE.SELL)){
            return this.sellAsset(coin, quantity, user);
        }
        throw new Exception("Invalid Order type");
    }

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        orderItem = this.orderItemRepository.save(orderItem);
        return orderItem;
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {

        if(quantity<=0){
            throw new Exception("Quantity should be greater than 0");
        }

        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = this.createOrder(user, orderItem, ORDER_TYPE.BUY);
        orderItem.setOrder(order);
        this.walletService.payOrderPayment(order, user);
        order.setStatus(ORDER_STATUS.SUCCESS);
        order.setOrderType(ORDER_TYPE.BUY);
        Order savedOrder = this.orderRepository.save(order);
        Asset oldAsset = this.assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
        if(oldAsset==null){
            oldAsset = this.assetService.createAsset(user, coin, quantity);
        }else{
            oldAsset = this.assetService.updateAsset(oldAsset.getId(), quantity);
        }
        return savedOrder;
    }


    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {

        if(quantity<=0){
            throw new Exception("Quantity should be greater than 0");
        }
        Asset assetToSell = this.assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        if(assetToSell!=null){
            double buyPrice = assetToSell.getBuyPrice();
            double sellPrice = coin.getCurrentPrice();
            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = this.createOrder(user, orderItem, ORDER_TYPE.SELL);
            orderItem.setOrder(order);
            order.setStatus(ORDER_STATUS.SUCCESS);
            order.setOrderType(ORDER_TYPE.SELL);
            Order savedOrder = this.orderRepository.save(order);
            if(assetToSell.getQuantity() >= quantity){
                this.walletService.payOrderPayment(order, user);
                Asset updatedAsset = this.assetService.updateAsset(assetToSell.getId(), -quantity);
                if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                    this.assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }
            throw new Exception("Insufficient quantity of asset you holds");
        }
        throw new Exception("Asset not found");
    }
}
