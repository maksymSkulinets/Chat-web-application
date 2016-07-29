package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.DTO.SignUpDTO;
import com.teamdev.javaclasses.SignUpException;
import com.teamdev.javaclasses.TransferContent;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUpHandler implements Handler {


    @Override
    public TransferContent process(HttpServletRequest request, HttpServletResponse response) {
        TransferContent content;
        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");
        final String verifyPassword = request.getParameter("verifyPassword");

        UserServiceImpl userService = UserServiceImpl.getInstance();
        /*TODO may be for test it better return username*/
        try {
            final UserId userId = userService.signUp(new SignUpDTO(nickname, password, verifyPassword));
            content = new TransferContent(userService.getUser(userId).getNickname());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SignUpException e) {
            content = new TransferContent(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
        return content;
    }
}