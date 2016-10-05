package com.teamdev.javaclasses.service.impl;

import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.tinyTypes.Password;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;
import com.teamdev.javaclasses.repository.impl.TokenRepository;
import com.teamdev.javaclasses.repository.impl.UserRepository;
import com.teamdev.javaclasses.service.LoginException;
import com.teamdev.javaclasses.service.SignUpException;
import com.teamdev.javaclasses.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.teamdev.javaclasses.service.UserServiceFailCases.*;

/**
 * Implementation {@link UserService}
 */
public class UserServiceImpl implements UserService {

    private static UserService userService;
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

        final Optional<User> userByNickname = userRepository.getUser(trimmedNickname);

        if (userByNickname.isPresent()) {
            log.warn(failLogTemplate + EXIST_USER.getMessage());
            throw new SignUpException(EXIST_USER);
        }

        final User currentUser =
                new User(new UserName(trimmedNickname), new Password(password));

        userRepository.add(currentUser);

        final String nicknameValue = currentUser.getNickname().getValue();
        final long userIdValue = currentUser.getId().getValue();
        final UserDto result = new UserDto(nicknameValue, userIdValue);

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

        final Optional<User> userByNickname = userRepository.getUser(trimmedNickname);

        if (!userByNickname.isPresent()) {
            log.warn(failLogTemplate + NON_SIGN_UP_USER.getMessage());
            throw new LoginException(NON_SIGN_UP_USER);
        }

        final String existUserPassword = userByNickname.get().getPassword().getValue();

        if (!password.equals(existUserPassword)) {
            log.warn(failLogTemplate + NON_SIGN_UP_USER.getMessage());
            throw new LoginException(NON_SIGN_UP_USER);
        }

        final Token currentUserToken = new Token(userByNickname.get().getId());
        tokenRepository.add(currentUserToken);

        final long tokenValue = currentUserToken.getId().getValue();
        final long userIdValue = currentUserToken.getUserId().getValue();
        final TokenDto result = new TokenDto(tokenValue, userIdValue);

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
        final User user = userRepository.get(new UserId(userId.getId()));

        if (user == null) {
            return Optional.empty();
        }

        final String nicknameValue = user.getNickname().getValue();
        final long userIdValue = user.getId().getValue();
        UserDto result = new UserDto(nicknameValue, userIdValue);
        return Optional.of(result);
    }

    @Override
    public Optional<UserDto> findUser(TokenIdDto token) {
        final Token userToken = tokenRepository.get(new TokenId(token.getId()));

        if (userToken == null) {
            return Optional.empty();
        }

        final User userByToken = userRepository.get(userToken.getUserId());

        if (userByToken == null) {
            return Optional.empty();
        }

        final String nicknameValue = userByToken.getNickname().getValue();
        final long userIdValue = userToken.getUserId().getValue();
        final UserDto result = new UserDto(nicknameValue, userIdValue);
        return Optional.of(result);
    }

    @Override
    public void deleteAccount(UserIdDto userId) {
        final User user = userRepository.remove(new UserId(userId.getId()));

        if (user == null) {
            return;
        }

        if (log.isInfoEnabled()) {
            log.info("User entity with id: " + user.getId().getValue() + " was deleted.");
        }

        Optional<TokenId> tokenByUserId = tokenRepository.findTokenId(user.getId());

        if (!tokenByUserId.isPresent()) {
            return;
        }

        tokenRepository.remove(tokenByUserId.get());

        if (log.isInfoEnabled()) {
            log.info("Token entity with id: " + user.getId().getValue() + " was deleted.");
        }

    }

}
