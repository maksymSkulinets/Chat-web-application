package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.DTO.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link ChatService}.
 */
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository = new ChatRepository();

    private Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Override
    public ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException {

        String trimmedChatName = chatCreationDto.getChatName().trim();

        if (trimmedChatName.isEmpty()) {
            log.warn("Fail chat creation: chat name input is empty");
            throw new ChatCreationException(ChatFailCases.EMPTY_CHAT_NAME.getMessage());
        }

        for (Chat current : chatRepository.findAll()) {

            if (current.getChatName().equals(trimmedChatName)) {
                log.warn("Fail chat creation: " + trimmedChatName + " - chat name is exist.");
                throw new ChatCreationException(ChatFailCases.NON_UNIQUE_CHAT_NAME.getMessage());
            }
        }

        chatRepository.add(new Chat(trimmedChatName, chatCreationDto.getUserId()));

        ChatId newChatId = null;
        for (Chat current : chatRepository.findAll()) {

            if (current.getChatName().equals(trimmedChatName)) {
                newChatId = current.getChatId();
            }
        }

        return newChatId;
    }

    @Override
    public void addMember(MemberChatDto memberChatDto) throws MemberException {

    }

    @Override
    public void removeMember(MemberChatDto memberChatDto) throws MemberException {

    }

    @Override
    public void postMessage(MessageDto postMessageDto) throws MessageException {

    }

    @Override
    public ChatDto getChat(ChatId id) {
        return null;
    }

    @Override
    public void deleteChat(ChatId chatId) {

    }
}
