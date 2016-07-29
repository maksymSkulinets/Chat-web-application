package com.teamdev.javaclasses;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;

public class DispatcherServletShould {

    private final Logger log = LoggerFactory.getLogger(DispatcherServletShould.class);

    private final HttpClient client = HttpClientBuilder.create().build();

    @Test
    public void successfulRegistrationRequest() throws IOException {

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

        HttpResponse postResponse = client.execute(postRequest);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(postResponse.getEntity().getContent()));

        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", nickname, result.toString());

    }
}