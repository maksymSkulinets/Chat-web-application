package com.teamdev.javaclasses.service.impl;

import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.entities.Chat;
import com.teamdev.javaclasses.entities.ChatId;
import com.teamdev.javaclasses.entities.Message;
import com.teamdev.javaclasses.entities.tinyTypes.ChatName;
import com.teamdev.javaclasses.entities.tinyTypes.UserId;
import com.teamdev.javaclasses.repository.impl.ChatRepository;
import com.teamdev.javaclasses.service.ChatCreationException;
import com.teamdev.javaclasses.service.ChatMemberException;
import com.teamdev.javaclasses.service.ChatService;
import com.teamdev.javaclasses.service.MessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.teamdev.javaclasses.service.ChatServiceFailCases.*;

/**
 * Implementation of {@link ChatService}.
 */
public class ChatServiceImpl implements ChatService {
    private static ChatService chatService;
    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private ChatServiceImpl() {
    }

    public static ChatService getInstance() {
        if (chatService == null) {
            chatService = new ChatServiceImpl();
        }
        return chatService;
    }

    @Override
    public ChatIdDto create(ChatCreationDto chatCreationDto) throws ChatCreationException {
        /*TODO update logs, except warn level*/

        checkNotNull(chatCreationDto.getChatName());
        checkNotNull(chatCreationDto.getUserId());

        final String trimmedChatName = chatCreationDto.getChatName().trim();
        final Long chatOwnerId = chatCreationDto.getUserId();

        if (trimmedChatName.isEmpty()) {
            log.warn(EMPTY_CHAT_NAME.getMessage());
            throw new ChatCreationException(EMPTY_CHAT_NAME.getMessage());
        }

        final Optional<Chat> chatEntity = chatRepository.getChat(trimmedChatName);

        if (chatEntity.isPresent()) {
            log.warn(NON_UNIQUE_CHAT_NAME.getMessage());
            throw new ChatCreationException(NON_UNIQUE_CHAT_NAME.getMessage());
        }

        final Chat currentChat = new Chat(new ChatName(trimmedChatName), new UserId(chatOwnerId));

        chatRepository.add(currentChat);

        if (log.isDebugEnabled()) {
            log.debug("Chat name: " + trimmedChatName + " id: " + currentChat.getId().getValue());
        }

        if (log.isInfoEnabled()) {
            log.info("Chat with name: " + trimmedChatName + " successfully created.");
        }

        return new ChatIdDto(currentChat.getId().getValue());
    }

    @Override
    public void joinChat(MemberChatDto memberChatDto) throws ChatMemberException {
        /*TODO update logs*/

        checkNotNull(memberChatDto.getUserId());
        checkNotNull(memberChatDto.getChatId());

        final Chat chat = chatRepository.get(new ChatId(memberChatDto.getChatId()));

        for (UserId current : chat.getMembers()) {
            if (memberChatDto.getUserId().equals(current.getValue())) {
                log.warn(CHAT_MEMBER_ALREADY_JOIN.getMessage());
                throw new ChatMemberException(CHAT_MEMBER_ALREADY_JOIN.getMessage());
            }
        }

        chat.getMembers().add(new UserId(memberChatDto.getUserId()));

        if (log.isDebugEnabled()) {
            log.debug("Chat member was added." +
                    " Chat id:" + memberChatDto.getChatId() +
                    " Member id: " + memberChatDto.getUserId());
        }
    }

    @Override
    public void leaveChat(MemberChatDto memberChatDto) throws ChatMemberException {
        final Chat chat = chatRepository.get(new ChatId(memberChatDto.getChatId()));

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(memberChatDto.getUserId())) {
            log.warn("Remove chat member fail: user not a chat member.");
            throw new ChatMemberException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chatMembers.remove(memberChatDto.getUserId());

            if (log.isDebugEnabled()) {
                log.debug("Chat member was removed." +
                        " Chat id is: " + memberChatDto.getChatId() +
                        " Member id is : " + memberChatDto.getUserId());
            }

        }
    }

    @Override
    public void sendMessage(PostMessageDto postMessageDto) throws MessageException {
        final Chat chat = chatRepository.get(new ChatId(postMessageDto.getUserId()));

        final List<UserId> chatMembers = chat.getMembers();

        if (!chatMembers.contains(postMessageDto.getUserId())) {
            log.warn("Post message chat member fail: user not a chat member.");
            throw new MessageException(NOT_A_CHAT_MEMBER.getMessage());
        } else {
            chat.getMessages().add(new Message(postMessageDto.getNickName(), postMessageDto.getMessage()));

            if (log.isDebugEnabled()) {
                log.debug("Message was sent successfully." +
                        " To chat with id:" + postMessageDto.getChatId() +
                        " Message author:  " + postMessageDto.getNickName());
            }
        }


    }

    @Override
    public void removeChat(ChatIdDto chatIdDto) {
        /*TODO add logs*/
        chatRepository.remove(new ChatId(chatIdDto.getId()));
    }

    @Override
    public Optional<ChatDto> findChat(ChatIdDto chatIdDto) {
        /*TODO add logs*/
        final Chat chat = chatRepository.get(new ChatId(chatIdDto.getId()));

        if (chat == null) {
            return Optional.empty();
        }


        final List<MessageDto> messagesDto = new ArrayList<>();
        final List<Message> messages = chat.getMessages();
        for (Message current : messages) {
            messagesDto.add(new MessageDto(current.getAuthorName(), current.getContent()));
        }

        final List<Long> membersDto = new ArrayList<>();
        final List<UserId> members = chat.getMembers();
        for (UserId current : members) {
            membersDto.add(current.getValue());
        }

        final Long chatId = chat.getId().getValue();
        final Long ownerId = chat.getOwnerId().getValue();
        final String chatName = chat.getChatName().getValue();
        return Optional.of(new ChatDto(chatId, ownerId, chatName, membersDto, messagesDto));
    }

}
