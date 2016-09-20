package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.Handler;
import com.teamdev.javaclasses.handler.SignUpHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.teamdev.javaclasses.constant.Uri.REGISTRATION_URI;

class HandlerContainer {
    private final Map<RequestContext, Handler> handlerContainer =
            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext(REGISTRATION_URI, "POST"), new SignUpHandler());
            }};

    Optional<Handler> getHandler(RequestContext context) {
        if (handlerContainer.containsKey(context)) {
            return Optional.of(handlerContainer.get(context));
        }
        return Optional.empty();
    }
}

