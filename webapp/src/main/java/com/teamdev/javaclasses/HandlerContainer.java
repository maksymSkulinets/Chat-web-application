package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.ChatCreateHandler;
import com.teamdev.javaclasses.handler.Handler;
import com.teamdev.javaclasses.handler.LoginHandler;
import com.teamdev.javaclasses.handler.SignUpHandler;

import java.util.HashMap;
import java.util.Map;

class HandlerContainer {

    private static final Map<RequestContext, Handler> registry =
            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext("/registration", "POST"), new SignUpHandler());
                put(new RequestContext("/login", "POST"), new LoginHandler());
                put(new RequestContext("createChat","POST"),new ChatCreateHandler());
            }};


    private HandlerContainer() {
    }

    static Handler getHandler(RequestContext requestContext) {
        return registry.get(requestContext);
    }
}

