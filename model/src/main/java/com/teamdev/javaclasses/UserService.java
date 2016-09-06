package com.teamdev.javaclasses;

import com.teamdev.javaclasses.dto.*;

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

    UserIdDTO findUserId(TokenIdDTO token);

    void deleteUser(UserIdDTO id);

    void logout(TokenIdDTO token);

}
