package com.teamdev.javaclasses.service;

import com.teamdev.javaclasses.dto.*;

import java.util.Collection;
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
     * @param memberChatDto contains user id and chat id
     * @throws ChatMemberException if member not join in chat
     */
    void leaveChat(MemberChatDto memberChatDto) throws ChatMemberException;

    /**
     * Post message to chat.
     *
     * @param postMessageDto contains chat id, owner id and message content.
     * @throws PostMessageException throws if message is empty
     */
    void postMessage(PostMessageDto postMessageDto) throws PostMessageException;

    /**
     * Find chat by chat id.
     *
     * @param chatIdDto chat id data transfer object.
     * @return chat data transfer object.
     */
    Optional<ChatDto> findChat(ChatIdDto chatIdDto);

    /**
     * Find chat members by chat id.
     *
     * @param chatIdDto chat id data transfer object
     * @return user id data transfer object
     */
    List<UserIdDto> findChatMembers(ChatIdDto chatIdDto);

    /**
     * Remove chat with current id.
     *
     * @param chatIdDto id of chat which will be removed
     */
    void removeChat(ChatIdDto chatIdDto);

    /**
     * Return all chat messages by chat id.
     *
     * @param chatId id of chat which messages will be return
     * @return messages data transfer object
     */
    List<MessageDto> findChatMessages(ChatIdDto chatId);

    /**
     * Return all available chats.
     *
     * @return data transfer object of all available chats
     */
    Collection<ChatDto> findAllChats();

    /**
     * Return chat by chat name.
     *
     * @param chatName chat name of chat which will be return
     * @return chat data transfer object
     */
    Optional<ChatIdDto> findChatIdByName(ChatNameDto chatName);
}
