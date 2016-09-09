import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.service.*;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.teamdev.javaclasses.service.ChatServiceFailCases.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.fail;

public class ChatServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private final String chatName = "guitars";
    private Long userId;

    @Before
    public void setUpUser() throws SignUpException, LoginException {
        final String nickName = "Jimmy_Page";
        final String password = "valid_password";
        userId = userService.signUp(new SignUpDto(nickName, password, password)).getId();
        userService.login(new LoginDto(nickName, password));
    }

    @After
    public void tearDownUser() {
        userService.deleteUser(new UserIdDto(userId));
    }

    @Test
    public void createNewChat() throws ChatCreationException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));
        final Optional<ChatDto> chatById = chatService.findChat(chatId);

        assertTrue("User was not created.", chatById.isPresent());

        assertEquals("Created chat name is not equal expected.",
                chatName, chatById.get().getChatName());

        chatService.removeChat(new ChatIdDto(chatId.getValue()));
    }

    @Test
    public void creationNewChatFailIfArgumentIsEmpty() {

        try {
            chatService.create(new ChatCreationDto("", userId));
            fail("Chat creation exception was not thrown.");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation exception messages are not match.",
                    EMPTY_CHAT_NAME.getMessage(), e.getMessage());
        }
    }

    @Test
    public void creationNewChatFailIfChatNameIsNotUnique() throws ChatCreationException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));

        try {
            chatService.create(new ChatCreationDto(chatName, userId));
            fail("Chat creation exception was not thrown.");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation exception messages are not match.",
                    NON_UNIQUE_CHAT_NAME.getMessage(), e.getMessage());
        } finally {
            chatService.removeChat(new ChatIdDto(chatId.getValue()));
        }
    }

    @Test
    public void joinUserToChat() throws ChatCreationException, ChatMemberException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));
        chatService.joinChat(new MemberChatDto(userId, chatId.getValue()));

        final Optional<ChatDto> chat = chatService.findChat(chatId);
        assertTrue("Chat was not created.", chat.isPresent());

        final Long chatMemberId = chat.get().getMembers().get(0);
        assertEquals("User is not member of current chat.", userId, chatMemberId);

        chatService.removeChat(new ChatIdDto(chatId.getValue()));
    }

    @Test
    public void failToJoinAlreadyJoinedChat() throws ChatCreationException, ChatMemberException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));
        chatService.joinChat(new MemberChatDto(userId, chatId.getValue()));

        try {
            chatService.joinChat(new MemberChatDto(userId, chatId.getValue()));
            fail("Chat member exception was not thrown.");
        } catch (ChatMemberException e) {
            assertEquals("Chat member exception messages are not match.",
                    CHAT_MEMBER_ALREADY_JOIN.getMessage(), e.getMessage());
        } finally {
            chatService.removeChat(new ChatIdDto(chatId.getValue()));
        }
    }

    @Test
    public void leaveUserFromChat() throws ChatCreationException, ChatMemberException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));
        chatService.joinChat(new MemberChatDto(userId, chatId.getValue()));

        List<UserIdDto> chatMembers = chatService.findChatMembers(chatId);
        assertEquals("User is not member of chat, but was joined.", userId, chatMembers.get(0).getId());

        chatService.leaveChat(new MemberChatDto(userId, chatId.getValue()));
        chatMembers = chatService.findChatMembers(chatId);
        assertTrue("User did not leave chat.", chatMembers.isEmpty());

        chatService.removeChat(new ChatIdDto(chatId.getValue()));
    }

    @Test
    public void failToLeaveChatIfNotAChatMember() throws ChatCreationException {

        final ChatIdDto chatId = chatService.create(new ChatCreationDto(chatName, userId));

        List<UserIdDto> chatMembers = chatService.findChatMembers(chatId);
        assertTrue("User is a member of chat, but was not joined.", chatMembers.isEmpty());

        try {
            chatService.leaveChat(new MemberChatDto(userId, chatId.getValue()));
            fail("Chat member exception was not thrown.");
        } catch (ChatMemberException e) {
            assertEquals("Chat creation exception messages are not match.",
                    NOT_A_CHAT_MEMBER.getMessage(), e.getMessage());
        } finally {
            chatService.removeChat(new ChatIdDto(chatId.getValue()));
        }
    }
}


