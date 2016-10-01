package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.service.ChatMemberException;
import com.teamdev.javaclasses.service.ChatService;
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

public class JoinChatHandler implements Handler {
    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response) {
        HandlerProcessingResult content;

        final Long tokenId = Long.valueOf(request.getParameter(TOKEN_ID));
        final Long userId = Long.valueOf(request.getParameter(USER_ID));
        final String chatName = request.getParameter(CHAT_NAME);

        final Optional<UserDto> userByToken = userService.findUser(new TokenIdDto(tokenId));
        if (!userByToken.isPresent()) {
            content = new HandlerProcessingResult(SC_FORBIDDEN);
            content.setContent(WARNING_MESSAGE, NON_SIGN_UP_USER.getMessage());
            return content;
        }

        final Optional<ChatIdDto> chatId = chatService.findChatIdByName(new ChatNameDto(chatName));

        try {
            final Long chatIdValue = chatId.get().getValue();
            chatService.joinChat(new MemberChatDto(userId, chatIdValue));

            final JSONArray messageList = convertMessageToJSON(chatId.get());
            content = new HandlerProcessingResult(SC_OK);
            content.setContent(MESSAGES, String.valueOf(messageList));
            content.setContent(CHAT_NAME, chatName);
        } catch (ChatMemberException e) {
            content = new HandlerProcessingResult(SC_INTERNAL_SERVER_ERROR);
            content.setContent(WARNING_MESSAGE, e.getMessage());
        }

        return content;
    }

    private JSONArray convertMessageToJSON(ChatIdDto chatId) {
        final List<String> messageList = new ArrayList<>();
        final List<MessageDto> messages = chatService.findChatMessages(chatId);

        for (MessageDto current : messages) {
            messageList.add(current.getAuthorName() + ": " + current.getContent());
        }

        return new JSONArray(messageList);
    }
}
