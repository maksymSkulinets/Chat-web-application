package com.teamdev.javaclasses.handler;


import com.teamdev.javaclasses.ChatCreationException;
import com.teamdev.javaclasses.DTO.ChatCreationDto;
import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation {@link Handler} for create chat.
 */
public class ChatCreateHandler implements Handler {
    private final ChatServiceImpl chatService = ChatServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();


    @Override
    public JSONObject process(HttpServletRequest request, HttpServletResponse response) {
        final JSONObject content = new JSONObject();

        final UserId chatOwnerId = new UserId(Long.valueOf(request.getParameter("userId")));
        final String chatName = request.getParameter("chatName");
        final String token = request.getParameter("token");

        final UserId userId = userService.findUserIdByToken(new SecurityToken(Long.valueOf(token)));

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            content.put("message", "User token is not valid.");
        } else {

            try {
                final ChatId chatId = chatService.createChat(new ChatCreationDto(chatOwnerId, chatName));
                content.put("chatId", chatId.getValue());

            } catch (ChatCreationException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                content.put("message", e.getMessage());
            }
        }
        return content;
    }
}
