package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.DTO.LoginDTO;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.LoginException;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation {@link Handler} for login requests.
 */
public class LoginHandler implements Handler {
    @Override
    public JSONObject process(HttpServletRequest request, HttpServletResponse response) {
        JSONObject content = new JSONObject();
        final String nickname = request.getParameter("nickname");
        final String password = request.getParameter("password");

        UserServiceImpl userService = UserServiceImpl.getInstance();
        try {
            final SecurityTokenDTO currentTokenDTO = userService.login(new LoginDTO(nickname, password));
            content.put("token", currentTokenDTO.getToken().getValue());
            content.put("userId", currentTokenDTO.getUserId().getValue());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (LoginException e) {
            content.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return content;
    }
}
