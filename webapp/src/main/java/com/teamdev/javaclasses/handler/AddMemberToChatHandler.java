package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.DTO.MemberChatDto;
import com.teamdev.javaclasses.MemberException;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.impl.ChatServiceImpl;
import com.teamdev.javaclasses.impl.UserServiceImpl;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of{@link Handler} for addition members to chat.
 */
public class AddMemberToChatHandler implements Handler {
    private final ChatServiceImpl chatService = ChatServiceImpl.getInstance();
    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    @Override
    public JSONObject process(HttpServletRequest request, HttpServletResponse response) {

        final JSONObject content = new JSONObject();

        final String token = request.getParameter("token");
        final UserId memberId = new UserId(Long.valueOf(request.getParameter("userId")));
        final ChatId chatId = new ChatId(Long.valueOf(request.getParameter("chatId")));
        final UserId userId = userService.findUserIdByToken(new SecurityToken(Long.valueOf(token)));

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            content.put("message", "User token is not valid.");
        } else {

            final MemberChatDto memberChatDto = new MemberChatDto(memberId, chatId);
            try {
                chatService.addMember(memberChatDto);
            } catch (MemberException e) {
                content.put("message", e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        return content;
    }
}
