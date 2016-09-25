import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.teamdev.javaclasses.constant.Parameters.*;
import static com.teamdev.javaclasses.constant.Uri.*;
import static org.apache.http.HttpHeaders.USER_AGENT;

class TestUtils {
    private final static String host = "http://localhost:8080";
    private final static HttpClient client = HttpClientBuilder.create().build();

    static HttpResponse sendRegistrationRequest(String nickname, String password, String verifyPassword) throws IOException {

        final String url = host + REGISTRATION_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(NICKNAME, nickname));
        parameters.add(new BasicNameValuePair(PASSWORD, password));
        parameters.add(new BasicNameValuePair(VERIFY_PASSWORD, verifyPassword));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }

    static HttpResponse sendLoginRequest(String nickname, String password) throws IOException {

        final String url = host + LOGIN_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(NICKNAME, nickname));
        parameters.add(new BasicNameValuePair(PASSWORD, password));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }


    static HttpResponse sendDeleteAccountRequest(String userIdValue) throws IOException {

        final String url = host + DELETE_ACCOUNT_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USER_ID, userIdValue));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }

    static HttpResponse sendCreateChatRequest(String chatName, String userId, String tokenId) throws IOException {

        final String url = host + CHAT_CREATION_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_NAME, chatName));
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }

    static HttpResponse sendJoinChatRequest(String userId, String chatId) throws IOException {
        final String url = host + JOIN_CHAT_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }

    static HttpResponse sendLeaveChatRequest(String userId, String chatId) throws IOException {
        final String url = host + LEAVE_CHAT_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);

    }

    static HttpResponse sendPostMessageRequest(String chatId, String userId, String nickname, String message)
            throws IOException {
        final String url = host + POST_MESSAGE_URI;

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(NICKNAME, nickname));
        parameters.add(new BasicNameValuePair(MESSAGE, message));

        HttpPost postRequest = new HttpPost(url);

        return sendRequest(postRequest, parameters);
    }

    static JSONObject getResponseContent(HttpResponse response) throws IOException {

        BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = buffer.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return new JSONObject(result.toString());
    }

    private static HttpResponse sendRequest(HttpPost request, List<NameValuePair> parameters) throws IOException {
        request.setHeader("User-Agent", USER_AGENT);
        request.setEntity(new UrlEncodedFormEntity(parameters));

        return client.execute(request);
    }
}
