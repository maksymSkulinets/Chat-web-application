package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.DeleteAccountHandler;
import com.teamdev.javaclasses.handler.Handler;
import com.teamdev.javaclasses.handler.LoginHandler;
import com.teamdev.javaclasses.handler.SignUpHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.teamdev.javaclasses.constant.Uri.*;

class HandlerContainer {
    private final Map<RequestContext, Handler> handlerContainer =
            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext(REGISTRATION_URI, "POST"), new SignUpHandler());
                put(new RequestContext(DELETE_ACCOUNT_URI, "POST"), new DeleteAccountHandler());
                put(new RequestContext(LOGIN_URI, "POST"), new LoginHandler());
            }};

    Optional<Handler> getHandler(RequestContext context) {
        if (handlerContainer.containsKey(context)) {
            return Optional.of(handlerContainer.get(context));
        }
        return Optional.empty();
    }
}

