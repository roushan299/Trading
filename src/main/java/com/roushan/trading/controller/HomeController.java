package com.roushan.trading.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home(){
        return "Welcome to the trading platform";
    }


    @GetMapping("/api")
    public String secure(){
        return  "Secured connection";
    }
}
