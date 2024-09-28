package com.roushan.trading.service;

import com.roushan.trading.model.Coin;
import com.roushan.trading.model.User;
import com.roushan.trading.model.WatchList;
import org.springframework.stereotype.Service;

@Service
public interface WatchListService {

    WatchList findUserWatchList(User user) throws Exception;

    WatchList createWatchList(User user);

    WatchList findWatchListById(Long watchListId) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;


}
