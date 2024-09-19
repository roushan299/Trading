package com.roushan.trading.service;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import com.roushan.trading.model.User;

public interface UserService {

    User findUserProfileByJwt(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;

    User findUserById(Long userId) throws Exception;

    User enableTwoFactorAuthentication(VERIFICATION_TYPE verificationType, String sendTo, User user);

    User updatePassword(User user, String password);


}
