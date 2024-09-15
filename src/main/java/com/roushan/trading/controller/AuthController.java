package com.roushan.trading.controller;


import com.roushan.trading.model.User;
import com.roushan.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {


        User isEmailExit = this.userRepository.findByEmail(user.getEmail());
        if(isEmailExit!=null){
            throw new Exception("Email is already used with another user");
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        User savedUser = this.userRepository.save(newUser);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }
}
