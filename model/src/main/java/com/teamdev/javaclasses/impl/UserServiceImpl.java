package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.DTO.UserDTO;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.UserServiceFailCases;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.TokenRepository;
import com.teamdev.javaclasses.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.teamdev.javaclasses.UserServiceFailCases.*;

/**
 * Implementation {@link UserService}
 */
public class UserServiceImpl implements UserService {
    private static UserService userService = UserServiceImpl.getInstance();
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository = UserRepository.getInstance();
    private final TokenRepository tokenRepository = TokenRepository.getInstance();
    private final String lineSeparator = System.getProperty("line.separator");

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    @Override
    public UserDTO signUp(SignUpDTO signUpData) throws SignUpException {
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
            log.warn(EMPTY_INPUT.getMessage());
            throw new SignUpException(EMPTY_INPUT);
        }

        if (!password.equals(verifyPassword)) {
            log.warn(PASSWORDS_NOT_MATCH.getMessage());
            throw new SignUpException(PASSWORDS_NOT_MATCH);
        }

        final User user = userRepository.get(trimmedNickname);

        if (user != null) {
            log.warn(EXIST_USER.getMessage());
            throw new SignUpException(EXIST_USER);
        }

        final User currentUser = new User(trimmedNickname, password);
        userRepository.add(currentUser);

        final UserDTO result = new UserDTO(currentUser.getId().getValue(), currentUser.getNickname());

        if (log.isInfoEnabled()) {
            log.info("User sign up with nickname: " + currentUser.getNickname() + " is successful.");
        }

        return result;
    }

    @Override
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
            log.warn(EMPTY_INPUT.getMessage());
            throw new LoginException(EMPTY_INPUT);
        }

        final User currentUser = new User(trimmedNickname, password);
        final UserId userId = userRepository.get(currentUser);
        if (userId == null) {
            log.warn(UserServiceFailCases.NON_SIGN_UP_USER.getMessage());
            throw new LoginException(UserServiceFailCases.NON_SIGN_UP_USER);
        }

        SecurityTokenDTO currentDTOToken = new SecurityTokenDTO(userId);
        tokenRepository.add(currentDTOToken);

        if (log.isInfoEnabled()) {
            log.info("User login  with nickname: " + currentUser.getNickname() + "is successful.");
        }
        return currentDTOToken;
    }


    @Override
    public User getUser(UserId userId) {
        return userRepository.find(userId);
    }

    @Override
    public UserId findUserIdByToken(SecurityToken securityToken) {
        return tokenRepository.find(securityToken).getUserId();
    }

    @Override
    public void deleteUser(UserId id) {

        userRepository.remove(id);

        if (log.isInfoEnabled()) {
            log.info("Delete user user with id:" + id.getValue());
        }
    }

    @Override
    public void logout(SecurityTokenDTO token) {

        tokenRepository.remove(token);

        if (log.isInfoEnabled()) {
            log.info("Logged out user with id:" + token.getUserId().getValue());
        }

    }
}
