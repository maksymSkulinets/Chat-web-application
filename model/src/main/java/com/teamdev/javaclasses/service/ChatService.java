package com.teamdev.javaclasses.service;

import com.teamdev.javaclasses.dto.*;

import java.util.List;
import java.util.Optional;

/**
 * Public API of chat service.
 */
public interface ChatService {

    /**
     * Create new chat.
     *
     * @param chatCreationDto contain owner userId and chat name
     * @return chat id data transfer object
     * @throws ChatCreationException throws if chat creation is fail
     */

    ChatIdDto create(ChatCreationDto chatCreationDto) throws ChatCreationException;

    /**
     * Add member to chat.
     *
     * @param memberChatDto contain user id and chat id
     * @throws ChatMemberException if member cannot join the chat
     */
    void joinChat(MemberChatDto memberChatDto) throws ChatMemberException;

    /**
     * Removes member from chat.
     *
     * @param memberChatDto - contains user id and chat id
     * @throws ChatMemberException if member not join in chat
     */
    void leaveChat(MemberChatDto memberChatDto) throws ChatMemberException;

    /**
     * Post a message.
     *
     * @param postMessageDto - contains user id, chat id and message
     * @throws MessageException if sending message fails
     */

    void sendMessage(PostMessageDto postMessageDto) throws MessageException;

    /**
     * Find chat by chat id.
     *
     * @param chatIdDto - chat id data transfer object
     * @return chat data transfer object
     */
    Optional<ChatDto> findChat(ChatIdDto chatIdDto);

    /**
     *Find chat members by chat id.
     *
     * @param chatIdDto - chat id data transfer object
     * @return user id data transfer object
     */
    List<UserIdDto> findChatMembers(ChatIdDto chatIdDto);

    void removeChat(ChatIdDto chatIdDto);
}
