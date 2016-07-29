package com.teamdev.javaclasses.handler;

import com.teamdev.javaclasses.TransferContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
    TransferContent process(HttpServletRequest request, HttpServletResponse response);
}
