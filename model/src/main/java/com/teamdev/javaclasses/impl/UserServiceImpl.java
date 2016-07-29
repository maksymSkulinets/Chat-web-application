package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.TokenRepository;
import com.teamdev.javaclasses.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation {@link UserService}
 */
public class UserServiceImpl implements UserService {

    private final static UserServiceImpl userServiceImpl = new UserServiceImpl();
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final String lineSeparator = System.getProperty("line.separator");
    private final UserRepository userRepository = new UserRepository();
    private final TokenRepository tokenRepository = new TokenRepository();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return userServiceImpl;
    }

    public UserId signUp(SignUpDTO signUpData) throws SignUpException {
        if (log.isInfoEnabled()) {
            log.info("Attempt to sign up.");
        }
        if (log.isDebugEnabled()) {
            log.debug("nickname: " + signUpData.getNickname() + lineSeparator +
                    "password: " + signUpData.getPassword() + lineSeparator +
                    "verify password: " + signUpData.getVerifyPassword());

        }
        String trimmedNickname = signUpData.getNickname().trim();
        String password = signUpData.getPassword();
        String verifyPassword = signUpData.getVerifyPassword();

        checkNotNull(trimmedNickname);
        checkNotNull(password);
        checkNotNull(verifyPassword);

        if (trimmedNickname.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            log.warn(SignUpFailCases.EMPTY_INPUT.getMessage());
            throw new SignUpException(SignUpFailCases.EMPTY_INPUT);
        }

        if (!password.equals(verifyPassword)) {
            log.warn(SignUpFailCases.PASSWORDS_NOT_MATCH.getMessage());
            throw new SignUpException(SignUpFailCases.PASSWORDS_NOT_MATCH);
        }

        final User user = userRepository.get(trimmedNickname);

        if (user != null) {
            log.warn(SignUpFailCases.EXIST_USER.getMessage());
            throw new SignUpException(SignUpFailCases.EXIST_USER);
        }

        final User currentUser = new User(trimmedNickname, password);
        userRepository.add(currentUser);

        if (log.isInfoEnabled()) {
            log.info("User sign up with nickname: " + currentUser.getNickname() + " is successful.");
        }

        return currentUser.getValue();
    }

    public SecurityTokenDTO login(LoginDTO loginData) throws LoginException {
        if (log.isInfoEnabled()) {
            log.info("Attempt to login.");
        }

        if (log.isDebugEnabled()) {
            log.debug("nickname: " + loginData.getNickname() + lineSeparator +
                    "password: " + loginData.getPassword() + lineSeparator);
        }

        final String trimmedNickname = loginData.getNickname().trim();
        final String password = loginData.getPassword();

        if (trimmedNickname.isEmpty() || password.isEmpty()) {
            log.warn(LoginFailCases.EMPTY_INPUT.getMessage());
            throw new LoginException(LoginFailCases.EMPTY_INPUT);
        }

        final User currentUser = new User(trimmedNickname, password);
        final UserId userId = userRepository.get(currentUser);
        if (userId == null) {
            log.warn(LoginFailCases.NON_SIGN_UP_USER.getMessage());
            throw new LoginException(LoginFailCases.NON_SIGN_UP_USER);
        }

        SecurityTokenDTO currentDTOToken = new SecurityTokenDTO(userId);
        tokenRepository.add(currentDTOToken);

        if (log.isInfoEnabled()) {
            log.info("User login  with nickname: " + currentUser.getNickname() + "is successful.");
        }
        return currentDTOToken;
    }

    public User getUser(UserId userId) {
        return userRepository.find(userId);
    }
}
