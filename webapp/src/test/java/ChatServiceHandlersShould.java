import com.teamdev.javaclasses.service.*;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static com.teamdev.javaclasses.service.ChatServiceFailCases.*;
import static com.teamdev.javaclasses.service.UserServiceFailCases.NON_SIGN_UP_USER;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChatServiceHandlersShould {
    private String userId;
    private String tokenId;
    private String nickname = "User_" + UUID.randomUUID();
    private String password = "correct_password";

    @Before
    public void setUpUserAccount() throws SignUpException, LoginException, IOException {
        final HttpResponse registrationResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        TestUtils.getResponseContent(registrationResponse);

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, password);
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);
        userId = loginResult.optString(USER_ID);
        tokenId = loginResult.optString(TOKEN_ID);
    }

    @Test
    public void createChat() throws IOException {
        /*TODO check chat with current name exist*/
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(chatCreationResponse));
        assertNotNull("Request failed.Token was not received.", result.optString(USER_ID));
    }

    @Test
    public void creationNewChatFailIfChatNameIsEmpty() throws IOException {
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest("", userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(chatCreationResponse));
        assertEquals("Unexpected message.", EMPTY_CHAT_NAME.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void creationNewChatFailIfChatNameIsNotUnique() throws IOException {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse firstResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(firstResponse);

        final HttpResponse secondResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(secondResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(secondResponse));
        assertEquals("Unexpected message.", NON_UNIQUE_CHAT_NAME.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void creationNewChatFailIfSuchTokenNotExist() throws IOException {
        String chatName = "newChat_" + UUID.randomUUID();
        String notExistToken = String.valueOf(new Random().nextLong());

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, notExistToken);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_FORBIDDEN, getStatus(chatCreationResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), result.optString(WARNING_MESSAGE));

    }

    @Test
    public void joinChat() throws IOException {
        /*TODO check is chat is joined*/
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse joinCreationResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(joinCreationResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(joinCreationResponse));
    }

    @Test
    public void failToJoinAlreadyJoinedChat() throws IOException {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse firstJoinChatResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(firstJoinChatResponse);
        final HttpResponse secondJoinChatResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        final JSONObject secondJoinResponseResult = TestUtils.getResponseContent(secondJoinChatResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(secondJoinChatResponse));
        assertEquals("Unexpected message.", CHAT_MEMBER_ALREADY_JOIN.getMessage(), secondJoinResponseResult.optString(WARNING_MESSAGE));

    }

    @Test
    public void leaveUserFromChat() throws IOException {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse leaveChatResponse = TestUtils.sendLeaveChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(leaveChatResponse);
        assertEquals("Unexpected response status.", SC_OK, getStatus(chatCreationResponse));
    }

    @Test
    public void failToLeaveChatIfNotAChatMember() throws IOException {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse leaveChatResponse = TestUtils.sendLeaveChatRequest(userId, chatId,tokenId);
        final JSONObject leaveChatResponseResult = TestUtils.getResponseContent(leaveChatResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(leaveChatResponse));
        assertEquals("Unexpected message.", NOT_A_CHAT_MEMBER.getMessage(), leaveChatResponseResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void postMessageToChat() throws ChatMemberException, ChatCreationException, PostMessageException, IOException {
        String message = "Message content";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(chatId, userId, nickname, message,tokenId);
        TestUtils.getResponseContent(postMessageResponse);
        assertEquals("Unexpected response status.", SC_OK, getStatus(postMessageResponse));
    }

    @Test
    public void failToPostEmptyMessage() throws ChatMemberException, ChatCreationException, PostMessageException, IOException {
        String message = " ";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatId,tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(chatId, userId, nickname, message,tokenId);
        final JSONObject postMessageResponseResult = TestUtils.getResponseContent(postMessageResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postMessageResponse));
        assertEquals("Unexpected message.", EMPTY_MESSAGE.getMessage(), postMessageResponseResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void failToPostMessageWithoutJoiningToChat() throws ChatMemberException, ChatCreationException, PostMessageException, IOException {
        String message = "Message content";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject chatCreationResult = TestUtils.getResponseContent(chatCreationResponse);
        String chatId = chatCreationResult.optString(CHAT_ID);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(chatId, userId, nickname, message,tokenId);
        final JSONObject postMessageResponseResult = TestUtils.getResponseContent(postMessageResponse);
        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postMessageResponse));
        assertEquals("Unexpected message.", NOT_A_CHAT_MEMBER.getMessage(), postMessageResponseResult.optString(WARNING_MESSAGE));
    }

    private int getStatus(HttpResponse postResponse) {
        return postResponse.getStatusLine().getStatusCode();
    }

}
