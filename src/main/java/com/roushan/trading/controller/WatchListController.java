package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.model.Coin;
import com.roushan.trading.model.User;
import com.roushan.trading.model.WatchList;
import com.roushan.trading.service.CoinService;
import com.roushan.trading.service.UserService;
import com.roushan.trading.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchList")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        WatchList watchList = this.watchListService.findUserWatchList(user);
        return new ResponseEntity<WatchList>(watchList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        WatchList watchList = this.watchListService.createWatchList(user);
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(@PathVariable("watchListId") Long watchListId) throws Exception {
        WatchList watchList = this.watchListService.findWatchListById(watchListId);
        return ResponseEntity.ok().body(watchList);
    }

    @GetMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("coinId") String coinId) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Coin coin = this.coinService.findById(coinId);
        coin = this.watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok().body(coin);
    }


}
