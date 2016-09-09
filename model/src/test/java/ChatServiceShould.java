import com.teamdev.javaclasses.dto.*;
import com.teamdev.javaclasses.service.*;
import com.teamdev.javaclasses.service.impl.ChatServiceImpl;
import com.teamdev.javaclasses.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.teamdev.javaclasses.service.ChatServiceFailCases.EMPTY_CHAT_NAME;
import static com.teamdev.javaclasses.service.ChatServiceFailCases.NON_UNIQUE_CHAT_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.fail;

public class ChatServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private final String nickName = "Jimmy_Page";
    private final String password = "valid_password";
    private final String chatName = "guitars";
    private Long userId;

    @Before
    public void setUpUser() throws SignUpException, LoginException {
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

        assertTrue("Current chat entity does not keep in repository.", chatById.isPresent());

        assertEquals("Created chat name is not equal expected.",
                chatName, chatById.get().getChatName());

        chatService.removeChat(new ChatIdDto(chatId.getId()));
    }

    @Test
    public void creationNewChatFailIfArgumentIsEmpty() {

        try {
            chatService.create(new ChatCreationDto("", userId));
            fail("Chat creation exception was not thrown.");
        } catch (ChatCreationException e) {
            assertEquals("Chat creation fail messages are not match.",
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
            assertEquals("Chat creation fail message are not match.",
                    NON_UNIQUE_CHAT_NAME.getMessage(), e.getMessage());
        }

        chatService.removeChat(new ChatIdDto(chatId.getId()));
    }
}

