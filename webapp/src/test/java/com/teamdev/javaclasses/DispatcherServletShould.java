package com.teamdev.javaclasses;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DispatcherServletShould {

    private final Logger log = LoggerFactory.getLogger(DispatcherServletShould.class);

    private final HttpClient client = HttpClientBuilder.create().build();

    private Long successfulRegisterUser(String nickname, String password) throws IOException, ParseException {
        final String url = "http://localhost:8080/registration";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        final Long userId = (Long) jsonObj.get("userId");

        return userId;
    }

    @Test
    public void signUpRequestSuccessfulSignUp() throws IOException, ParseException {

        final String url = "http://localhost:8080/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID() + "_endOfPassword";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        final int expectedStatus = 200;
        final String signUpUserNickname = (String) jsonObj.get("nickname");
        final Long signUpUserId = (Long) jsonObj.get("userId");

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", nickname, signUpUserNickname);
        assertNotNull("Post request failed", signUpUserId);
    }

    @Test
    public void signUpRequestDuplicateSignUpAttempt() throws IOException, ParseException {
        final String url = "http://localhost:8080/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID();
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = SignUpFailCases.EXIST_USER.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void signUpRequestInputWasEmpty() throws IOException, ParseException {
        final String url = "http://localhost:8080/registration";

        final String nickname = "";
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password));

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = SignUpFailCases.EMPTY_INPUT.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        /*TODO build expected in util method or chane fail messages*/
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void signUpRequestSignUpPasswordsNotMuch() throws IOException, ParseException {
        final String url = "http://localhost:8080/registration";

        final String nickname = "YuriGagarin" + UUID.randomUUID();
        final String password = "LETS_DRIVE!!!" + UUID.randomUUID();

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("nickname", nickname));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("verifyPassword", password + "NOT_MATCH_PASSWORD"));

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String actualMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;
        final String expectedMessage = SignUpFailCases.PASSWORDS_NOT_MATCH.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, actualMessage);
    }

    @Test
    public void loginRequestLoginSuccessful() throws IOException, ParseException {
        final String loginUrl = "http://localhost:8080/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        final Long actualUserId = successfulRegisterUser(nickname, password);

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("nickname", nickname));
        loginParameters.add(new BasicNameValuePair("password", password));
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(loginParameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        Long expectedUserId = (Long) jsonObj.get("userId");
        Long token = (Long) jsonObj.get("token");
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedUserId, actualUserId);
        assertNotNull("Post request failed", token);
    }

    @Test
    public void loginRequestLoginEmptyInput() throws IOException, ParseException {
        final String loginUrl = "http://localhost:8080/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        successfulRegisterUser(nickname, password);

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("nickname", nickname));
        loginParameters.add(new BasicNameValuePair("password", ""));
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(loginParameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, LoginFailCases.EMPTY_INPUT.getMessage());
    }

    @Test
    public void loginRequestLoginWithWrongPassword() throws IOException, ParseException {
        final String loginUrl = "http://localhost:8080/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        successfulRegisterUser(nickname, password);

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("nickname", nickname));
        loginParameters.add(new BasicNameValuePair("password", password + "WRONG_PASSWORD"));
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(loginParameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, LoginFailCases.NON_SIGN_UP_USER.getMessage());
    }

    @Test
    public void loginRequestLoginNonSignUpUser() throws IOException, ParseException {
        final String loginUrl = "http://localhost:8080/login";

        final String nickname = "YuriGagarin" + UUID.randomUUID() + "_endOfName";
        final String password = "LETS_GO!!!" + UUID.randomUUID() + "_endOfPassword";

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("nickname", nickname));
        loginParameters.add(new BasicNameValuePair("password", password));
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(loginParameters));

        client.execute(postRequest);

        HttpResponse postResponse = client.execute(postRequest);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new InputStreamReader(postResponse.getEntity().getContent()));
        JSONObject jsonObj = (JSONObject) obj;
        String expectedMessage = (String) jsonObj.get("message");
        final int expectedStatus = 500;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, LoginFailCases.NON_SIGN_UP_USER.getMessage());
    }


}