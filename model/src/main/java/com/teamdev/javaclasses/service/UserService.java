package com.teamdev.javaclasses.service;

import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.dto.*;

import java.util.Optional;

/**
 * User service public API.
 */
public interface UserService {

    /**
     * Sign up new user.
     *
     * @param signUpData client sign up data
     * @return unique user id and unique nickname
     * @throws SignUpException if sign up fail
     */
    UserDto signUp(SignUpDto signUpData) throws SignUpException;

    /**
     * Login already sign up users.
     *
     * @param loginData client login data
     * @return TokenDto for access
     * @throws LoginException if login fail
     */
    TokenDto login(LoginDto loginData) throws LoginException;

    /**
     * Get user.
     *
     * @param userId user id
     * @return user.
     */
    Optional<UserDto> findUser(UserIdDto userId);

    Optional<UserDto> findUser(TokenIdDto token);

    void deleteUser(UserIdDto userId);

    void logout(TokenIdDto token);

}
