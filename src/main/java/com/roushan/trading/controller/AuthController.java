package com.roushan.trading.controller;


import com.roushan.trading.CustomUserDetailsService;
import com.roushan.trading.config.JwtProvider;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.UserRepository;
import com.roushan.trading.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {


        User isEmailExit = this.userRepository.findByEmail(user.getEmail());
        if(isEmailExit!=null){
            throw new Exception("Email is already used with another user");
        }

        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        User savedUser = this.userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse response = AuthResponse.builder()
                .jwtToken(jwtToken)
                .status(true)
                .message("registration successful")
                .build();

        return new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user){
        Authentication auth = this.authenticate(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse response = AuthResponse.builder()
                .jwtToken(jwtToken)
                .status(true)
                .message("login successful")
                .build();

        return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);

    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

        if(username==null){
            throw new BadCredentialsException("Invalid Username");
        }

        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }


}
