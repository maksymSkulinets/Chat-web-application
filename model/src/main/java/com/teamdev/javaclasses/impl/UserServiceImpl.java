package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.TokenRepository;
import com.teamdev.javaclasses.repository.UserRepository;

/**
 * Implementation {@link UserService}
 */
public class UserServiceImpl implements UserService {

    private final static UserServiceImpl userServiceImpl = new UserServiceImpl();

    private final UserRepository userRepository = new UserRepository();
    private final TokenRepository tokenRepository = new TokenRepository();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return userServiceImpl;
    }

    public UserId signUp(String nickname, String password, String verifyPassword) throws SignUpException {

        final String trimmedNickname = nickname.trim();

        if (trimmedNickname.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            throw new SignUpException("All fields must be filled");
        }

        if (!password.equals(verifyPassword)) {
            throw new SignUpException("Passwords must match");
        }

        final User user = userRepository.get(trimmedNickname);

        if (user != null) {
            throw new SignUpException("Current nickname must be unique");
        }

        final User currentUser = new User(trimmedNickname, password);
        userRepository.add(currentUser);
        final UserId currentUserId = userRepository.get(currentUser);

        return currentUserId;
    }

    public SecurityToken login(String nickname, String password) throws LoginException {
        final String trimmedNickname = nickname.trim();

        if (trimmedNickname.isEmpty() || password.isEmpty()) {
            throw new LoginException("All fields must be filled");
        }

        final User currentUser = new User(nickname, password);
        final UserId userId = userRepository.get(currentUser);
        if (userId == null) {
            throw new LoginException("Such user must register before");
        }

        tokenRepository.add(new SecurityToken(userId));

        final SecurityToken accessToken = tokenRepository.get(userId);
        return accessToken;

    }

    public User getUser(UserId userId) {
        return userRepository.find(userId);
    }

    public UserId getUserId(SecurityToken accessToken) {
        return tokenRepository.get(accessToken);


    }
}
