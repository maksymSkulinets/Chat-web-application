package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.teamdev.javaclasses.constant.Parameters.USER_ID;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class DeleteAccountHandler implements Handler {
    private final UserService service = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        final String userId = request.getParameter(USER_ID);
        service.deleteAccount(new UserIdDto(Long.valueOf(userId)));

        return new HandlerProcessingResult(SC_OK);
    }
}
