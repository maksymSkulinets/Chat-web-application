package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.service.SignUpException;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class SignUpHandler implements Handler {
    private final UserService service = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;
        final String nickname = request.getParameter(NICKNAME);
        final String password = request.getParameter(PASSWORD);
        final String verifyPassword = request.getParameter(VERIFY_PASSWORD);

        try {
            final UserDto userDto = service.signUp(new SignUpDto(nickname, password, verifyPassword));
            content = new HandlerProcessingResult(SC_OK);
            content.setContent(NICKNAME, userDto.getNickname());
            content.setContent(USER_ID, String.valueOf(userDto.getId()));

        } catch (SignUpException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
        }

        return content;
    }
}
