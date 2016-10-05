package com.teamdev.javaclasses.service;

import com.teamdev.javaclasses.dto.*;

import java.util.Optional;

/**
 * Public API of user service.
 */
public interface UserService {

    /**
     * Sign up new user.
     *
     * @param signUpData contain nickname, password and verify password
     * @return unique user id and unique nickname
     * @throws SignUpException throw if user sign up fail
     */
    UserDto signUp(SignUpDto signUpData) throws SignUpException;

    /**
     * Login user.
     *
     * @param loginData contain nickname and password.
     * @return TokenDto unique identifier for access
     * @throws LoginException throw if user login fail
     */
    TokenDto login(LoginDto loginData) throws LoginException;

    /**
     * Return user by id.
     *
     * @param userId user id
     * @return user data transfer object
     */
    Optional<UserDto> findUser(UserIdDto userId);

    /**
     * Return user by token.
     *
     * @param token unique identifier for access
     * @return user data transfer object
     */
    Optional<UserDto> findUser(TokenIdDto token);

    /**
     * Delete user account by id.
     *
     * @param userId user id
     */
    void deleteAccount(UserIdDto userId);

}
