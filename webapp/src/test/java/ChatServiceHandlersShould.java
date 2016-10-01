import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUpUserAccount() throws Exception {
        String password = "correct_password";

        final HttpResponse registrationResponse = TestUtils.sendRegistrationRequest(nickname, password, password);
        TestUtils.getResponseContent(registrationResponse);

        final HttpResponse loginResponse = TestUtils.sendLoginRequest(nickname, password);
        final JSONObject loginResult = TestUtils.getResponseContent(loginResponse);
        userId = loginResult.optString(USER_ID);
        tokenId = loginResult.optString(TOKEN_ID);
    }

    @Test
    public void createChat() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(chatCreationResponse));
        assertNotNull("Chat id was not received.", result.optString(CHAT_ID));
    }

    @Test
    public void creationNewChatFailIfChatNameIsEmpty() throws Exception {
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest("", userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(chatCreationResponse));
        assertEquals("Unexpected message.", EMPTY_CHAT_NAME.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void creationNewChatFailIfChatNameIsNotUnique() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse firstResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(firstResponse);

        final HttpResponse secondResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        final JSONObject result = TestUtils.getResponseContent(secondResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(secondResponse));
        assertEquals("Unexpected message.", NON_UNIQUE_CHAT_NAME.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void creationNewChatFailIfSuchTokenNonExist() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        String nonExistToken = String.valueOf(new Random().nextLong());

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, nonExistToken);
        final JSONObject result = TestUtils.getResponseContent(chatCreationResponse);

        assertEquals("Unexpected response status.", SC_FORBIDDEN, getStatus(chatCreationResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), result.optString(WARNING_MESSAGE));
    }

    @Test
    public void joinChat() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        final JSONObject joinChatResult = TestUtils.getResponseContent(joinChatResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(joinChatResponse));
        assertEquals("Unexpected chat name", chatName, joinChatResult.optString(CHAT_NAME));
    }

    @Test
    public void failToJoinChatIfSuchTokenNonExist() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        String nonExistTokenId = String.valueOf(new Random().nextLong());

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinCreationResponse = TestUtils.sendJoinChatRequest(userId, chatName, nonExistTokenId);
        final JSONObject joinChatResult = TestUtils.getResponseContent(joinCreationResponse);

        assertEquals("Unexpected response status.", SC_FORBIDDEN, getStatus(joinCreationResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), joinChatResult.optString(WARNING_MESSAGE));
    }


    @Test
    public void failToJoinAlreadyJoinedChat() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse firstResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(firstResponse);
        final HttpResponse secondResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        final JSONObject secondResponseResult = TestUtils.getResponseContent(secondResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(secondResponse));
        assertEquals("Unexpected message.", CHAT_MEMBER_ALREADY_JOIN.getMessage(), secondResponseResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void leaveUserFromChat() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse leaveChatResponse = TestUtils.sendLeaveChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(leaveChatResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(chatCreationResponse));
    }

    @Test
    public void failToLeaveChatIfNotAChatMember() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse leaveChatResponse = TestUtils.sendLeaveChatRequest(userId, chatName, tokenId);
        final JSONObject leaveChatResult = TestUtils.getResponseContent(leaveChatResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(leaveChatResponse));
        assertEquals("Unexpected message.", NOT_A_CHAT_MEMBER.getMessage(), leaveChatResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void failToLeaveChatIfSuchTokenNonExist() throws Exception {
        String chatName = "newChat_" + UUID.randomUUID();
        String nonExistTokenId = String.valueOf(new Random().nextLong());

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse leaveChatResponse = TestUtils.sendLeaveChatRequest(userId, chatName, nonExistTokenId);
        final JSONObject leaveChatResult = TestUtils.getResponseContent(leaveChatResponse);

        assertEquals("Unexpected response status.", SC_FORBIDDEN, getStatus(leaveChatResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), leaveChatResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void postMessageToChat() throws Exception {
        String message = "Message content";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse postMessageResponse = TestUtils.
                sendPostMessageRequest(userId, chatName, tokenId, nickname, message);
        TestUtils.getResponseContent(postMessageResponse);

        assertEquals("Unexpected response status.", SC_OK, getStatus(postMessageResponse));
    }

    @Test
    public void failToPostEmptyMessage() throws Exception {
        String message = " ";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(userId, chatName, tokenId, nickname, message);
        final JSONObject postMessageResult = TestUtils.getResponseContent(postMessageResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postMessageResponse));
        assertEquals("Unexpected message.", EMPTY_MESSAGE.getMessage(), postMessageResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void failToPostMessageWithoutJoiningToChat() throws Exception {
        String message = "Message content";
        String chatName = "newChat_" + UUID.randomUUID();

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(userId, chatName, tokenId, nickname, message);
        final JSONObject postMessageResult = TestUtils.getResponseContent(postMessageResponse);

        assertEquals("Unexpected response status.", SC_INTERNAL_SERVER_ERROR, getStatus(postMessageResponse));
        assertEquals("Unexpected message.", NOT_A_CHAT_MEMBER.getMessage(), postMessageResult.optString(WARNING_MESSAGE));
    }

    @Test
    public void failToPostEmptyMessageWithNonExistToken() throws Exception {
        String message = "Message content";
        String chatName = "newChat_" + UUID.randomUUID();
        String nonExistTokenId = String.valueOf(new Random().nextLong());

        final HttpResponse chatCreationResponse = TestUtils.sendCreateChatRequest(chatName, userId, tokenId);
        TestUtils.getResponseContent(chatCreationResponse);

        final HttpResponse joinChatResponse = TestUtils.sendJoinChatRequest(userId, chatName, tokenId);
        TestUtils.getResponseContent(joinChatResponse);

        final HttpResponse postMessageResponse = TestUtils.sendPostMessageRequest(userId, chatName, nonExistTokenId, nickname, message);
        final JSONObject postMessageResponseResult = TestUtils.getResponseContent(postMessageResponse);

        assertEquals("Unexpected response status.", SC_FORBIDDEN, getStatus(postMessageResponse));
        assertEquals("Unexpected message.", NON_SIGN_UP_USER.getMessage(), postMessageResponseResult.optString(WARNING_MESSAGE));
    }

    private int getStatus(HttpResponse postResponse) {
        return postResponse.getStatusLine().getStatusCode();
    }

}
