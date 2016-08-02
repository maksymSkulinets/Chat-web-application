package com.teamdev.javaclasses;

import com.teamdev.javaclasses.DTO.ChatId;
import com.teamdev.javaclasses.DTO.SecurityTokenDTO;
import com.teamdev.javaclasses.entities.SecurityToken;
import com.teamdev.javaclasses.entities.UserId;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.teamdev.javaclasses.ChatServiceFailCases.*;
import static com.teamdev.javaclasses.UserServiceFailCases.*;
import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DispatcherServletShould {

    private final HttpClient client = HttpClientBuilder.create().build();

    private HttpResponse getPostResponse(String url, List<NameValuePair> parameters) throws IOException {
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);
        postRequest.setEntity(new UrlEncodedFormEntity(parameters));
        return client.execute(postRequest);
    }

    private JSONObject getJsonObject(HttpResponse postResponse) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        return (JSONObject) obj;
    }

    private Long successfullyRegisterUser(String nickname, String password) throws IOException, ParseException {
        final String url = "http://localhost:8080/api/registration";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        return (Long) jsonObj.get("userId");
    }

    private SecurityTokenDTO successfulLoginUser(String nickname, String password) throws IOException, ParseException {
        final String url = "http://localhost:8080/api/login";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        Long userIdValue = (Long) jsonObj.get("userId");
        Long tokenValue = (Long) jsonObj.get("token");
        final SecurityTokenDTO securityTokenDTO = new SecurityTokenDTO(new UserId(userIdValue));
        securityTokenDTO.setId(new SecurityToken(tokenValue));
        return securityTokenDTO;
    }

    private ChatId successfullyCreateChat(SecurityTokenDTO securityTokenDTO, String chatName) throws IOException, ParseException {
        final String url = "http://localhost:8080/api/create_chat";


        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("userId", String.valueOf(securityTokenDTO.getUserId().getValue())));
        parameters.add(new BasicNameValuePair("chatName", chatName));
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId().getValue())));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String chatId = String.valueOf(jsonObj.get("chatId"));
        return new ChatId(Long.valueOf(chatId));
    }

    private void successfullyAddMemberToCHat(Long userId, ChatId chatId, SecurityTokenDTO securityTokenDTO)
            throws IOException, ParseException {
        final String url = "http://localhost:8080/api/add_member";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));

        HttpResponse postResponse = getPostResponse(url, parameters);
        getJsonObject(postResponse);
    }

    @Test
    public void signUpRequestSuccessfulSignUp() throws IOException, ParseException {

        final String url = "http://localhost:8080/api/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID() + "_endOfPassword";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        final int expectedStatus = 200;
        final String signUpUserNickname = (String) jsonObj.get("nickname");
        final Long signUpUserId = (Long) jsonObj.get("userId");

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", nickname, signUpUserNickname);
        assertNotNull("Post request failed", signUpUserId);
    }

    @Test
    public void signUpRequestDuplicateSignUpAttempt() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID();
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        getPostResponse(url, parameters);
        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = EXIST_USER.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void signUpRequestInputWasEmpty() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/registration";

        final String nickname = "";
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = EMPTY_INPUT.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void signUpRequestSignUpPasswordsNotMuch() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID();
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password + "NOT_MATCH_PASSWORD"));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = PASSWORDS_NOT_MATCH.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void loginRequestLoginSuccessful() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        final Long actualUserId = successfullyRegisterUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        Long expectedUserId = (Long) jsonObj.get("userId");
        Long token = (Long) jsonObj.get("token");
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedUserId, actualUserId);
        assertNotNull("Post request failed", token);
    }

    @Test
    public void loginRequestLoginEmptyInput() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        successfullyRegisterUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", ""));
        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, EMPTY_INPUT.getMessage());
    }

    @Test
    public void loginRequestLoginWithWrongPassword() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        successfullyRegisterUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password + "WRONG_PASSWORD"));
        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, NON_SIGN_UP_USER.getMessage());
    }

    @Test
    public void loginRequestLoginNonSignUpUser() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, NON_SIGN_UP_USER.getMessage());
    }

    @Test
    public void chatRequestChatCreation() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/create_chat";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "SPACE_" + random + "endOfChatName";
        successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("userId", String.valueOf(securityTokenDTO.getUserId().getValue())));
        parameters.add(new BasicNameValuePair("chatName", chatName));
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId().getValue())));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String chatId = String.valueOf(jsonObj.get("chatId"));
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertNotNull("Post request failed", chatId);
    }

    @Test
    public void chatRequestEmptyChatNameRequest() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/create_chat";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "";

        successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("userId", String.valueOf(securityTokenDTO.getUserId().getValue())));
        parameters.add(new BasicNameValuePair("chatName", chatName));
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId().getValue())));

        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String expectedMessage = String.valueOf(jsonObj.get("message"));
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, EMPTY_CHAT_NAME.getMessage());
    }

    @Test
    public void chatRequestChatNameAlreadyExist() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/create_chat";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;

        successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("userId", String.valueOf(securityTokenDTO.getUserId().getValue())));
        parameters.add(new BasicNameValuePair("chatName", chatName));
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId().getValue())));

        getPostResponse(url, parameters);
        HttpResponse postResponse = getPostResponse(url, parameters);
        JSONObject jsonObj = getJsonObject(postResponse);
        String expectedMessage = String.valueOf(jsonObj.get("message"));
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, NON_UNIQUE_CHAT_NAME.getMessage());
    }

    @Test
    public void chatRequestAddMemberToChat() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/add_member";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));

        HttpResponse postResponse = getPostResponse(url, parameters);
        getJsonObject(postResponse);
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void chatRequestAddMemberToChatFail() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/add_member";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);
        successfullyAddMemberToCHat(userId, chatId, securityTokenDTO);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));

        HttpResponse postResponse = getPostResponse(url, parameters);
        final JSONObject jsonObj = getJsonObject(postResponse);
        final int expectedStatus = 500;
        String expectedMessage = String.valueOf(jsonObj.get("message"));

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, CHAT_MEMBER_ALREADY_JOIN.getMessage());

    }

    @Test
    public void chatRequestRemoveMemberFromChat() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/remove_member";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);
        successfullyAddMemberToCHat(userId, chatId, securityTokenDTO);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));

        HttpResponse postResponse = getPostResponse(url, parameters);
        getJsonObject(postResponse);
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void chatRequestRemoveMemberFromChatFail() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/remove_member";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));

        HttpResponse postResponse = getPostResponse(url, parameters);
        final JSONObject jsonObject = getJsonObject(postResponse);
        final int expectedStatus = 500;
        String expectedMessage = String.valueOf(jsonObject.get("message"));


        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, NOT_A_CHAT_MEMBER.getMessage());
    }

    @Test
    public void chatRequestSendMessageToChat() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/send_message";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;
        final String message = "Hello my name is Yuri.";

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);
        successfullyAddMemberToCHat(userId, chatId, securityTokenDTO);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("message", message));
        parameters.add(new BasicNameValuePair("nickname", nickname));

        final HttpResponse postResponse = getPostResponse(url, parameters);
        getJsonObject(postResponse);
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void chatRequestSendMessageToChatFail() throws IOException, ParseException {
        final String url = "http://localhost:8080/api/send_message";

        final UUID random = UUID.randomUUID();

        final String nickname = "YuriGagarin" + random + "_endOfName";
        final String password = "LETS_DRIVE!!!" + random + "_endOfPassword";
        final String chatName = "space_" + random;
        final String message = "Hello my name is Yuri.";

        final Long userId = successfullyRegisterUser(nickname, password);
        final SecurityTokenDTO securityTokenDTO = successfulLoginUser(nickname, password);
        final ChatId chatId = successfullyCreateChat(securityTokenDTO, chatName);

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("token", String.valueOf(securityTokenDTO.getId())));
        parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("chatId", String.valueOf(chatId.getValue())));
        parameters.add(new BasicNameValuePair("message", message));
        parameters.add(new BasicNameValuePair("nickname", nickname));

        final HttpResponse postResponse = getPostResponse(url, parameters);
        final JSONObject jsonObj = getJsonObject(postResponse);
        final int expectedStatus = 500;
        String expectedMessage = String.valueOf(jsonObj.get("message"));

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, NOT_A_CHAT_MEMBER.getMessage());
    }

}