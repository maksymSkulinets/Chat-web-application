package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.PostMessageException;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static com.teamdev.javaclasses.service.UserServiceFailCases.NON_SIGN_UP_USER;
import static javax.servlet.http.HttpServletResponse.*;

public class PostMessageHandler implements Handler {
    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;
        final Long tokenId = Long.valueOf(request.getParameter(TOKEN_ID));
        final Long userId = Long.valueOf(request.getParameter(USER_ID));
        final String chatName = request.getParameter(CHAT_NAME);
        final String nickname = request.getParameter(NICKNAME);
        final String message = request.getParameter(MESSAGE);

        final Optional<UserDto> userByToken = userService.findUser(new TokenIdDto(tokenId));
        if (!userByToken.isPresent()) {
            content = new HandlerProcessingResult(SC_FORBIDDEN);
            content.setContent(WARNING_MESSAGE, NON_SIGN_UP_USER.getMessage());
            return content;
        }

        final ChatIdDto chatIdDto = chatService.findChatIdByName(new ChatNameDto(chatName)).get();
        Long chatId = chatIdDto.getValue();

        try {
            chatService.postMessage(new PostMessageDto(chatId, userId, nickname, message));
            final JSONArray messageList = convertMessagesToJSON(new ChatIdDto(chatId));

            content = new HandlerProcessingResult(SC_OK);
            content.setContent(CHAT_NAME, chatName);
            content.setContent(MESSAGES, messageList.toString());
        } catch (PostMessageException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
            content.setContent(CHAT_NAME, chatName);
        }

        return content;
    }

    private JSONArray convertMessagesToJSON(ChatIdDto chatId) {
        final List<String> messageList = new ArrayList<>();
        final List<MessageDto> messages = chatService.findChatMessages(chatId);

        for (MessageDto current : messages) {
            messageList.add(current.getAuthorName() + ": " + current.getContent());
        }

        return new JSONArray(messageList);
    }
}
