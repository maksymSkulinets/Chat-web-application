package com.teamdev.javaclasses.service;

import com.teamdev.javaclasses.dto.*;

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

    ChatIdDto createChat(ChatCreationDto chatCreationDto) throws ChatCreationException;

    /**
     * Add member to chat.
     *
     * @param memberChatDto contain user id and chat id
     * @throws MemberException if member cannot join the chat
     */
    void addMember(MemberChatDto memberChatDto) throws MemberException;

    /**
     * Removes member from chat.
     *
     * @param memberChatDto - contains user id and chat id
     * @throws MemberException if member not join in chat
     */
    void removeMember(MemberChatDto memberChatDto) throws MemberException;

    /**
     * Post a message.
     *
     * @param postMessageDto - contains user id, chat id and message
     * @throws MessageException if sending message fails
     */
    /*TODO rename post message*/
    void sendMessage(MessageDto postMessageDto) throws MessageException;

    /**
     * Get chat by id
     *
     * @param id - chat id data transfer object
     * @return Chat data transfer object
     */
    ChatDto getChat(ChatIdDto id);

    void removeChat(ChatIdDto id);
}
