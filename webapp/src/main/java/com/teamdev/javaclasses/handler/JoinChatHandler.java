package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.MemberChatDto;
import com.teamdev.javaclasses.service.ChatMemberException;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.teamdev.javaclasses.constant.Parameters.CHAT_ID;
import static com.teamdev.javaclasses.constant.Parameters.USER_ID;
import static com.teamdev.javaclasses.constant.Parameters.WARNING_MESSAGE;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class JoinChatHandler implements Handler {
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;
        final Long userId = Long.valueOf(request.getParameter(USER_ID));
        final Long chatId = Long.valueOf(request.getParameter(CHAT_ID));

        try {
            chatService.joinChat(new MemberChatDto(userId, chatId));
            content = new HandlerProcessingResult(SC_OK);

        } catch (ChatMemberException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
        }

        return content;
    }
}
