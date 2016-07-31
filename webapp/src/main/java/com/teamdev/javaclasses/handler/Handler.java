package com.teamdev.javaclasses.handler;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
    JSONObject process(HttpServletRequest request, HttpServletResponse response);
}
