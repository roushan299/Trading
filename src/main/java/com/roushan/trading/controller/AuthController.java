package com.roushan.trading.controller;


import com.roushan.trading.model.TwoFactorOTP;
import com.roushan.trading.model.WatchList;
import com.roushan.trading.service.CustomUserDetailsService;
import com.roushan.trading.config.JwtProvider;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.UserRepository;
import com.roushan.trading.response.AuthResponse;
import com.roushan.trading.service.EmailService;
import com.roushan.trading.service.TwoFactorOTPService;
import com.roushan.trading.service.WatchListService;
import com.roushan.trading.util.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TwoFactorOTPService twoFactorOTPService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private WatchListService watchListService;


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
        WatchList watchList = this.watchListService.createWatchList(user);

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
        String email = user.getEmail();
        String password = user.getPassword();
        Authentication auth = this.authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);
        User authUser = this.userRepository.findByEmail(email);
        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);

            String otp = OtpUtils.generateOtp();

            TwoFactorOTP oldTwofactorOTP = this.twoFactorOTPService.findByUser(authUser.getId());
            if(oldTwofactorOTP!=null){
                this.twoFactorOTPService.deleteTwoFactorOTP(oldTwofactorOTP);
            }

            TwoFactorOTP newTwoFactorOtp = this.twoFactorOTPService.createTwoFactorOTP(authUser, otp, jwtToken);
            this.emailService.sendVerificationOtpMail(email, otp);
            res.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<AuthResponse>(res, HttpStatus.CREATED);
        }

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


    @PostMapping("/two-facror/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOTP(@PathVariable("otp") String otp, @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = this.twoFactorOTPService.findById(id);

        if(twoFactorOTP!=null && this.twoFactorOTPService.verifyTwoFactorOTP(twoFactorOTP, otp)){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two factor authentication verified");
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setJwtToken(twoFactorOTP.getJwt());
            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }


}
