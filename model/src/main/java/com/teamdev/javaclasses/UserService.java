package com.teamdev.javaclasses;

import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.dto.UserIdDTO;

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
    UserDTO signUp(SignUpDTO signUpData) throws SignUpException;

    /**
     * Login already sign up users.
     *
     * @param loginData client login data
     * @return SecurityTokenDTO for access
     * @throws LoginException if login fail
     */
    SecurityTokenDTO login(LoginDTO loginData) throws LoginException;

    /**
     * Get user.
     *
     * @param userId user id
     * @return user.
     */
    UserDTO findUser(UserIdDTO userId);

    UserDTO findUserId(TokenIdDTO token);

    void deleteUser(UserIdDTO userId);

    void logout(TokenIdDTO token);

}
