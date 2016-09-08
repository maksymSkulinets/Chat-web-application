package com.teamdev.javaclasses.service.impl;

import com.teamdev.javaclasses.service.LoginException;
import com.teamdev.javaclasses.service.SignUpException;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.UserServiceFailCases;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.tinyTypes.Password;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;
import com.teamdev.javaclasses.repository.impl.TokenRepository;
import com.teamdev.javaclasses.repository.impl.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.teamdev.javaclasses.service.UserServiceFailCases.*;

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
    public UserDto signUp(SignUpDto signUpData) throws SignUpException {

        checkNotNull(signUpData.getNickname());
        checkNotNull(signUpData.getPassword());
        checkNotNull(signUpData.getVerifyPassword());

        if (log.isInfoEnabled()) {
            log.info("Sign up attempting.");
        }

        String trimmedNickname = signUpData.getNickname().trim();
        String password = signUpData.getPassword();
        String verifyPassword = signUpData.getVerifyPassword();

        final String failLogTemplate = "User with nickname: " + trimmedNickname +
                " registration failed." + lineSeparator +
                "Cause: ";

        if (log.isDebugEnabled()) {
            log.debug("Sign up attributes:" + lineSeparator +
                    "nickname: " + trimmedNickname + lineSeparator +
                    "password: " + password + lineSeparator +
                    "verify password: " + verifyPassword);
        }

        if (trimmedNickname.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            log.warn(failLogTemplate + EMPTY_INPUT.getMessage());
            throw new SignUpException(EMPTY_INPUT);
        }

        if (!password.equals(verifyPassword)) {
            log.warn(failLogTemplate + PASSWORDS_NOT_MATCH.getMessage());
            throw new SignUpException(PASSWORDS_NOT_MATCH);
        }

        final User userByNickname = userRepository.getUser(trimmedNickname);

        if (userByNickname != null) {
            log.warn(failLogTemplate + EXIST_USER.getMessage());
            throw new SignUpException(EXIST_USER);
        }

        final User currentUser = new User(new UserName(trimmedNickname), new Password(password));
        userRepository.add(currentUser);

        final UserDto result = new UserDto(currentUser.getNickname().getName(), currentUser.getId().getValue());

        if (log.isDebugEnabled()) {
            log.debug("User: " + trimmedNickname + " id: " + result.getId());
        }

        if (log.isInfoEnabled()) {
            log.info("User with nickname: " + trimmedNickname + " successfully registered.");
        }

        return result;
    }

    @Override
    public TokenDto login(LoginDto loginData) throws LoginException {

        checkNotNull(loginData.getNickname());
        checkNotNull(loginData.getPassword());

        if (log.isInfoEnabled()) {
            log.info("Login attempting.");
        }

        final String trimmedNickname = loginData.getNickname().trim();
        final String password = loginData.getPassword();

        final String failLogTemplate = "User with nickname: " + trimmedNickname +
                " login failed." + lineSeparator +
                "Cause: ";

        if (log.isDebugEnabled()) {
            log.debug("Login attributes:" + lineSeparator +
                    "nickname: " + trimmedNickname + lineSeparator +
                    "password: " + password);
        }

        if (trimmedNickname.isEmpty() || password.isEmpty()) {
            log.warn(failLogTemplate + EMPTY_INPUT.getMessage());
            throw new LoginException(EMPTY_INPUT);
        }

        final User userByNickname = userRepository.getUser(trimmedNickname);

        if (userByNickname == null) {
            log.warn(UserServiceFailCases.NON_SIGN_UP_USER.getMessage());
            log.warn(failLogTemplate + NON_SIGN_UP_USER.getMessage());
            throw new LoginException(UserServiceFailCases.NON_SIGN_UP_USER);
        }

        final String existUserPassword = userByNickname.getPassword().getPassword();

        if (!password.equals(existUserPassword)) {
            log.warn(UserServiceFailCases.NON_SIGN_UP_USER.getMessage());
            log.warn(failLogTemplate + NON_SIGN_UP_USER.getMessage());
            throw new LoginException(UserServiceFailCases.NON_SIGN_UP_USER);
        }


        final Token currentUserToken = new Token(userByNickname.getId());
        tokenRepository.add(currentUserToken);

        final TokenDto result = new TokenDto(
                currentUserToken.getId().getValue(), currentUserToken.getUserId().getValue());

        if (log.isDebugEnabled()) {
            log.debug("User: " + trimmedNickname + " token: " + result.getToken());
        }

        if (log.isInfoEnabled()) {
            log.info("User with nickname: " + trimmedNickname + " successfully logged.");
        }

        return result;
    }


    @Override
    public Optional<UserDto> findUser(UserIdDto userId) {
        final User user = userRepository.find(new UserId(userId.getId()));

        if (user == null) {
            return Optional.empty();
        }

        UserDto result = new UserDto(user.getNickname().getName(), user.getId().getValue());
        return Optional.of(result);
    }

    @Override
    public Optional<UserDto> findUser(TokenIdDto token) {
        final Token userToken = tokenRepository.find(new TokenId(token.getId()));

        if (userToken == null) {
            return Optional.empty();
        }

        final User userByToken = userRepository.find(userToken.getUserId());

        if (userByToken == null) {
            return Optional.empty();
        }

        final UserDto result = new UserDto(userByToken.getNickname().getName(), userToken.getUserId().getValue());
        return Optional.of(result);
    }

    @Override
    public void deleteUser(UserIdDto userId) {
        final User user = userRepository.remove(new UserId(userId.getId()));

        if (user == null) {
            return;
        }

        if (log.isInfoEnabled()) {
            log.info("Delete user entity with id: " + user.getId().getValue());
        }

        TokenId tokenByUserId = tokenRepository.findTokenId(user.getId());

        if (tokenByUserId == null) {
            return;
        }

        tokenRepository.remove(tokenByUserId);

        if (log.isInfoEnabled()) {
            log.info("Delete token entity with user id: " + user.getId().getValue());
        }

        /*TODO also delete owner chats, chats membership*/

    }

    @Override
    public void logout(TokenIdDto token) {

        final Token userToken = tokenRepository.remove(new TokenId(token.getId()));

        if (log.isInfoEnabled()) {
            log.info("Logged out user with id: " + userToken.getUserId());
        }

    }
}
