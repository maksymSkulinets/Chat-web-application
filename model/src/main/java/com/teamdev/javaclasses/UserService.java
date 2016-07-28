package com.teamdev.javaclasses;

import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.entities.UserId;

/**
 * User service public API.
 */
public interface UserService {

    /**
     * Sign up new user.
     *
     * @param signUpData client sign up data
     * @return unique id
     * @throws SignUpException if sign up fail
     */
    UserId signUp(SignUpDTO signUpData) throws SignUpException;

    /**
     * Login already sign up users.
     *
     * @param loginData client login data
     * @return SecurityTokenDTO for access
     * @throws LoginException if login fail
     */
    SecurityTokenDTO login(LoginDTO loginData) throws LoginException;


}
