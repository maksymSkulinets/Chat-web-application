package com.teamdev.javaclasses;

import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.ChatId;

/**
 * Public API of chat service.
 */
public interface ChatService {

    /**
     * Create new chat.
     *
     * @param chatCreationDto contain owner userId and chat name
     * @return chat id
     * @throws ChatCreationException throws if chat creation is fail
     */
    ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException;

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
    void sendMessage(MessageDTO postMessageDto) throws MessageException;

    /**
     * Get chat by id
     *
     * @param id - chat id
     * @return Chat dto
     */
    Chat getChat(ChatId id);
    /*TODO change method signature return data transfer object*/

}
