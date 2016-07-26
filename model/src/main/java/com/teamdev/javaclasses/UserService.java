package com.teamdev.javaclasses;


import com.teamdev.javaclasses.entities.AccessToken;
import com.teamdev.javaclasses.entities.UserId;

/**
 * User service public API.
 */
public interface UserService {

    /**
     * Sign up new user.
     *
     * @param nickname       user nickname
     * @param password       user password
     * @param verifyPassword password for confirm
     * @return unique id
     * @throws SignUpException if sign up fail
     */
    UserId signUp(String nickname, String password, String verifyPassword) throws SignUpException;

    /**
     * Login already sign up users.
     *
     * @param nickname user nickname
     * @param password user password
     * @return token for access
     * @throws LoginException if login fail
     */
    AccessToken login(String nickname, String password) throws LoginException;
}
