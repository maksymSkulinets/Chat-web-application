package com.teamdev.javaclasses.impl;

import com.teamdev.javaclasses.*;
import com.teamdev.javaclasses.DTO.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.UserId;
import com.teamdev.javaclasses.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.teamdev.javaclasses.ChatFailCases.CHAT_MEMBER_ALREADY_JOIN;
import static com.teamdev.javaclasses.ChatFailCases.NOT_A_CHAT_MEMBER;

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
        final Chat chat = chatRepository.find(memberChatDto.getChatId());

        for (UserId memberId : chat.getMembers()) {
            if (memberChatDto.getChatId().equals(memberId)) {
                log.warn("Add chat member fail: chat member already join.");
                throw new MemberException(CHAT_MEMBER_ALREADY_JOIN.getMessage());
            }
        }

        chat.getMembers().add(memberChatDto.getUserId());
    }

    @Override
    public void removeMember(MemberChatDto memberChatDto) throws MemberException {
        final Chat chat = chatRepository.find(memberChatDto.getChatId());

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(memberChatDto.getUserId())) {
            log.warn("Remove chat member fail: user not a chat member.");
            throw new MemberException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chatMembers.remove(memberChatDto.getUserId());
        }
    }

    @Override
    public void sendMessage(MessageDto messageDto) throws MessageException {
        final Chat chat = chatRepository.find(messageDto.getChatId());

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(messageDto.getUserId())) {
            log.warn("Post message chat member fail: user not a chat member.");
            throw new MessageException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chat.getMessages().add(new Message(messageDto.getNickName(), messageDto.getMessage()));
        }
    }

    @Override
    public ChatDto getChat(ChatId id) {
        /*TODO implement for nest testing*/
        return null;
    }

}
