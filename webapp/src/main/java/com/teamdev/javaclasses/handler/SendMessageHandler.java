package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.DTO.MessageDTO;
import com.teamdev.javaclasses.MessageException;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation {@link Handler} for send message to chat.
 */
public class SendMessageHandler implements Handler {
    private final ChatServiceImpl chatService = ChatServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    @Override
    public JSONObject process(HttpServletRequest request, HttpServletResponse response) {
        final JSONObject content = new JSONObject();

        final String token = request.getParameter("token");
        final String nickname = request.getParameter("nickname");
        final UserId messageAuthorId = new UserId(Long.valueOf(request.getParameter("userId")));
        final ChatId chatId = new ChatId(Long.valueOf(request.getParameter("chatId")));
        final String message = request.getParameter("message");

        final UserId userId = userService.findUserIdByToken(new SecurityToken(Long.valueOf(token)));

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            content.put("message", "User token is not valid.");
        } else {
            final MessageDTO messageDTO = new MessageDTO(messageAuthorId, chatId, message, nickname);
            try {
                chatService.sendMessage(messageDTO);
            } catch (MessageException e) {
                content.put("message", e.getMessage());
            }
        }
        return content;
    }
}
