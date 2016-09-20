package com.teamdev.javaclasses;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HandlerProcessingResult {
    private final int responseStatus;
    private final Map<String, String> content;

    public HandlerProcessingResult(int responseStatus) {
        this.responseStatus = responseStatus;
        this.content = new HashMap<>();
    }

    public void setContent(String key, String value) {
        content.put(key, value);
    }

    int getResponseStatus() {
        return responseStatus;
    }

    String getContent() {
        return new JSONObject(content).toString();
    }
}
