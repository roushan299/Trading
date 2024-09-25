package com.roushan.trading.controller;

import com.roushan.trading.config.JwtConstant;
import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.ForgetPasswordToken;
import com.roushan.trading.model.User;
import com.roushan.trading.model.VerificationCode;
import com.roushan.trading.request.ResetPasswordRequest;
import com.roushan.trading.response.ApiResponse;
import com.roushan.trading.response.AuthResponse;
import com.roushan.trading.request.ForgetPasswordTokenRequest;
import com.roushan.trading.service.EmailService;
import com.roushan.trading.service.ForgetPasswordService;
import com.roushan.trading.service.UserService;
import com.roushan.trading.service.VerificationCodeService;
import com.roushan.trading.util.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private ForgetPasswordService forgetPasswordService;


    @GetMapping("/profile")
     public ResponseEntity<User> getUserProfile(@RequestHeader(JwtConstant.JWT_HEADER) String jwt) throws Exception {
         User user = userService.findUserProfileByJwt(jwt);
         return new ResponseEntity<>(user, HttpStatus.OK);
     }

     @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerification(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("verificationType") VERIFICATION_TYPE verificationType) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
         VerificationCode verificationCode = this.verificationCodeService.getVerificationCodeByUser(user.getId());

         if(verificationCode==null){
           verificationCode = this.verificationCodeService.sendVerificationOtp(user, verificationType);
         }

         if(verificationType.equals(VERIFICATION_TYPE.EMAIL)){
             this.emailService.sendVerificationOtpMail(user.getEmail(), verificationCode.getOtp());
         }
         return new ResponseEntity<>("Verification otp sent successfully", HttpStatus.OK);
     }

     @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader(JwtConstant.JWT_HEADER) String jwt, @PathVariable("otp") String otp) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = this.verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VERIFICATION_TYPE.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();
        boolean isVerified = this.verificationCodeService.verifyOtp(otp, verificationCode);
        if(isVerified){
            User updatedUser = this.userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            this.verificationCodeService.deleteVerificationCode(verificationCode);
            return  new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong otp");
     }


    @PostMapping("/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgetPasswordOTP(@RequestBody ForgetPasswordTokenRequest forgetPasswordTokenRequest) throws Exception {
        User user = this.userService.findUserByEmail(forgetPasswordTokenRequest.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        ForgetPasswordToken forgetPasswordToken = this.forgetPasswordService.findByUser(user.getId());
        if(forgetPasswordToken==null){
            forgetPasswordToken = this.forgetPasswordService.createToken(user,id, otp, forgetPasswordTokenRequest.getVerificationType(), forgetPasswordTokenRequest.getSendTo());
        }

        if(forgetPasswordTokenRequest.getVerificationType().equals(VERIFICATION_TYPE.EMAIL)){
            this.emailService.sendVerificationOtpMail(forgetPasswordToken.getSendTo(), otp);
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setSession(id);
        authResponse.setMessage("Password reset otp sent successfully");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam("otp") String id, @RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {
        ForgetPasswordToken forgetPasswordToken = this.forgetPasswordService.findById(id);

        boolean isVerified = this.forgetPasswordService.verifyToken(resetPasswordRequest.getOtp(), forgetPasswordToken);
        if(isVerified){
            this.userService.updatePassword(forgetPasswordToken.getUser(), resetPasswordRequest.getPassword());
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password updated successfully");
            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong Otp");
    }








}
