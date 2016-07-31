package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.DTO.UserDTO;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation {@link Handler} for signUp request.
 */
public class SignUpHandler implements Handler {

    @Override
    public JSONObject process(HttpServletRequest request, HttpServletResponse response) {
        final JSONObject content = new JSONObject();

        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String verifyPassword = request.getParameter("verifyPassword");

        UserServiceImpl userService = UserServiceImpl.getInstance();
        try {
            final UserDTO currentUserDTO = userService.signUp(new SignUpDTO(nickname, password, verifyPassword));
            content.put("nickname", currentUserDTO.getNickname());
            content.put("userId", currentUserDTO.getUserId());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SignUpException e) {
            content.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return content;
    }
}