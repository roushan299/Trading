package com.roushan.trading.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roushan.trading.model.Coin;
import com.roushan.trading.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {
    @Autowired
    private CoinService coinService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false, name = "page") Integer page) throws Exception {
        int actualPage = (page != null) ? page.intValue() : 1;
        List<Coin> coinList = this.coinService.getCoinList(actualPage);
        return new ResponseEntity<List<Coin>>(coinList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<String> getCoinChart(@PathVariable("coinId") String coinId, @RequestParam("days") int days) throws Exception {
        String coinChart = this.coinService.getMarketChart(coinId, days);
        return new ResponseEntity<String>(coinChart, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoinByKeyWord(@RequestParam("q") String keyword) throws Exception {
        String coin = this.coinService.searchCoin(keyword);
        JsonNode jsonNode = this.objectMapper.readTree(coin);
        return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50() throws Exception {
        String coin = this.coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = this.objectMapper.readTree(coin);
        return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<JsonNode> getTrendingCoins() throws Exception {
        String coin = this.coinService.getTrendingCoins();
        JsonNode jsonNode = this.objectMapper.readTree(coin);
        return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable("coinId") String coinId) throws Exception {
        String coin = this.coinService.getCoinDetails(coinId);
        JsonNode jsonNode = this.objectMapper.readTree(coin);
        return new ResponseEntity<JsonNode>(jsonNode,HttpStatus.OK);
    }




}
