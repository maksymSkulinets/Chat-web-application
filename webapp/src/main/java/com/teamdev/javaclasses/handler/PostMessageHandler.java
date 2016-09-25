package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.PostMessageDto;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.PostMessageException;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class PostMessageHandler implements Handler {
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;
        final Long userId = Long.valueOf(request.getParameter(USER_ID));
        final Long chatId = Long.valueOf(request.getParameter(CHAT_ID));
        final String nickname = request.getParameter(NICKNAME);
        final String message = request.getParameter(MESSAGE);

        try {
            chatService.postMessage(new PostMessageDto(chatId, userId, nickname, message));
            content = new HandlerProcessingResult(SC_OK);
        } catch (PostMessageException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
        }

        return content;
    }
}
