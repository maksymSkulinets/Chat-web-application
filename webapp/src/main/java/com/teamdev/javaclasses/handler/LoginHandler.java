package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.LoginDto;
import com.teamdev.javaclasses.dto.TokenDto;
import com.teamdev.javaclasses.service.LoginException;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class LoginHandler implements Handler {
    private final UserService service = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;
        final String nickname = request.getParameter(NICKNAME);
        final String password = request.getParameter(PASSWORD);

        try {
            final TokenDto tokenDto = service.login(new LoginDto(nickname,password));
            content = new HandlerProcessingResult(SC_OK);
            content.setContent(TOKEN_ID, String.valueOf(tokenDto.getToken()));
            content.setContent(USER_ID, String.valueOf(tokenDto.getUserId()));

        } catch (LoginException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
        }

        return content;
    }
}
