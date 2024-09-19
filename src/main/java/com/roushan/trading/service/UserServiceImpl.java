package com.roushan.trading.service;

import com.roushan.trading.config.JwtProvider;
import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.TwoFactorAuth;
import com.roushan.trading.model.User;
import com.roushan.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
       User user = this.userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> opt = this.userRepository.findById(userId);
        if(opt.isEmpty()){
            throw new Exception("User not found");
        }
        return opt.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VERIFICATION_TYPE verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return this.userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String password) {
        user.setPassword(password);
        return this.userRepository.save(user);
    }
}
