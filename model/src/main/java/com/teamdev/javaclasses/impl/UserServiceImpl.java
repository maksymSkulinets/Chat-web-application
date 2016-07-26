package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.entities.AccessToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    long lastIdValue = 0;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId signUp(String nickname, String password, String verifyPassword) throws SignUpException {

        String trimmedNickname = nickname.trim();

        if (trimmedNickname.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            throw new SignUpException("All fields must be filled");
        }

        if (!password.equals(verifyPassword)) {
            throw new SignUpException("Passwords must match");
        }

        UserId userId = (UserId) userRepository.readId(new User(nickname, password));
        if (userId != null) {
            throw new SignUpException("Current nickname must be unique");
        }

        User user = new User(nickname, password);
        userId = new UserId(lastIdValue++);
        userRepository.create(user, userId);

        return userId;
    }

    public AccessToken login(String nickname, String password) throws LoginException {
        return null;
    }
}
