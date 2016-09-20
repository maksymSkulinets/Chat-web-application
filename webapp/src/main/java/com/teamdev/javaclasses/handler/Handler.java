package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.HandlerProcessingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Public API of request handlers
 */
public interface Handler {
    /*
     * Parse http request and convert it to JSON format
     *
     * @param request http request
     * @param response http response
     * @return already parse JSON entity
     */
    HandlerProcessingResult process(HttpServletRequest request, HttpServletResponse response);
}
