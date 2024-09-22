package com.roushan.trading.service;

import com.roushan.trading.model.Coin;
import com.roushan.trading.model.User;
import com.roushan.trading.model.WatchList;
import com.roushan.trading.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WatchListImpl implements WatchListService{

    @Autowired
    private WatchListRepository watchListRepository;

    @Override
    public WatchList findUserWatchList(Long userId) throws Exception {
       WatchList watchList = this.watchListRepository.findByUserId(userId);
       if(watchList == null){
           throw new Exception("WatchList not found");
       }
       return  watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
       WatchList watchList = new WatchList();
       watchList.setUser(user);
       watchList = this.watchListRepository.save(watchList);
       return watchList;
    }

    @Override
    public WatchList findWatchListById(Long watchListId) throws Exception {
        Optional<WatchList> watchListOptional = this.watchListRepository.findById(watchListId);
        if(watchListOptional.isPresent()){
            return watchListOptional.get();
        }
        throw new Exception("WatchList doesn't exits");
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        WatchList watchList = this.findUserWatchList(user.getId());
        List<Coin> coinList = watchList.getCoins();

        if(coinList.contains(coin)){
            coinList.remove(coin);
        }else {
            coinList.add(coin);
        }

        watchList.setCoins(coinList);
        watchList = this.watchListRepository.save(watchList);
        return coin;
    }
}
