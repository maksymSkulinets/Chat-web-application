package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;
import com.teamdev.javaclasses.dto.MemberChatDto;
import com.teamdev.javaclasses.dto.TokenIdDto;
import com.teamdev.javaclasses.dto.UserDto;
import com.teamdev.javaclasses.service.ChatMemberException;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.UserService;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        final Long chatId = Long.valueOf(request.getParameter(CHAT_ID));

        final Optional<UserDto> userByToken = userService.findUser(new TokenIdDto(tokenId));
        if (!userByToken.isPresent()) {
            content = new HandlerProcessingResult(SC_FORBIDDEN);
            content.setContent(WARNING_MESSAGE, NON_SIGN_UP_USER.getMessage());
            return content;
        }

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
