package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.UserService;
import com.teamdev.javaclasses.UserServiceFailCases;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Token;
import com.teamdev.javaclasses.entities.User;
import com.teamdev.javaclasses.entities.tinyTypes.Password;
import com.teamdev.javaclasses.entities.tinyTypes.TokenId;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.entities.tinyTypes.UserName;
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
    public UserDto signUp(SignUpDto signUpData) throws SignUpException {
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

        final User userByNickname = userRepository.getUser(trimmedNickname);

        if (userByNickname != null) {
            log.warn(EXIST_USER.getMessage());
            throw new SignUpException(EXIST_USER);
        }

        final User currentUser = new User(new UserName(trimmedNickname), new Password(password));
        userRepository.add(currentUser);

        final UserDto result = new UserDto(currentUser.getNickname().getName(), currentUser.getId().getValue());

        if (log.isInfoEnabled()) {
            log.info("User sign up with nickname: " + currentUser.getNickname() + " is successful.");
        }

        return result;
    }

    @Override
    public TokenDto login(LoginDto loginData) throws LoginException {
        if (log.isInfoEnabled()) {
            log.info("Attempt to login.");
        }

        if (log.isDebugEnabled()) {
            log.debug("nickname: " + loginData.getNickname() + lineSeparator +
                    "password: " + loginData.getPassword() + lineSeparator);
        }

        final String trimmedNickname = loginData.getNickname().trim();
        final String password = loginData.getPassword();

        checkNotNull(trimmedNickname);
        checkNotNull(password);

        if (trimmedNickname.isEmpty() || password.isEmpty()) {
            log.warn(EMPTY_INPUT.getMessage());
            throw new LoginException(EMPTY_INPUT);
        }

        final User userByNickname = userRepository.getUser(trimmedNickname);

        if (userByNickname == null) {
            log.warn(UserServiceFailCases.NON_SIGN_UP_USER.getMessage());
            log.warn("Cause: User with nickname: " + trimmedNickname + " - not registered yet.");
            throw new LoginException(UserServiceFailCases.NON_SIGN_UP_USER);
        }

        final String existUserPassword = userByNickname.getPassword().getPassword();

        if (!password.equals(existUserPassword)) {
            log.warn(UserServiceFailCases.NON_SIGN_UP_USER.getMessage());
            log.warn("Cause: User with nickname: " + trimmedNickname + " - wrong password input.");
            throw new LoginException(UserServiceFailCases.NON_SIGN_UP_USER);
        }


        final Token currentUserToken = new Token(userByNickname.getId());
        tokenRepository.add(currentUserToken);

        final TokenDto result = new TokenDto(
                currentUserToken.getId().getValue(), currentUserToken.getUserId().getValue());

        if (log.isInfoEnabled()) {
            log.info("User login  with id: " + result.getUserId() + " is successful.");
        }

        return result;
    }


    @Override
    public UserDto findUser(UserIdDto userId) {
        final User user = userRepository.find(new UserId(userId.getId()));
        return new UserDto(user.getNickname().getName(), user.getId().getValue());
    }

    @Override
    public UserDto findUser(TokenIdDto token) {
        final Token userToken = tokenRepository.find(new TokenId(token.getId()));
        final User userByToken = userRepository.find(userToken.getUserId());
        return new UserDto(userByToken.getNickname().getName(), userToken.getUserId().getValue());
    }

    @Override
    public void deleteUser(UserIdDto userId) {
        final User user = userRepository.remove(new UserId(userId.getId()));
        /*TODO also delete owner chats, chats membership*/
        TokenId tokenByUserId = tokenRepository.findTokenId(user.getId());
        tokenRepository.remove(tokenByUserId);
        if (log.isInfoEnabled()) {
            log.info("Delete user entity with id:" + userId);
        }
    }

    @Override
    public void logout(TokenIdDto token) {

        final Token userToken = tokenRepository.remove(new TokenId(token.getId()));

        if (log.isInfoEnabled()) {
            log.info("Logged out user with id:" + userToken.getUserId());
        }

    }
}
