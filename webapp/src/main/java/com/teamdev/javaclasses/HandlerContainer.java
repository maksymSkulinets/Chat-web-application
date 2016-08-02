package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.*;

import java.util.HashMap;
import java.util.Map;

class HandlerContainer {

    private final Map<RequestContext, Handler> registry =

            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext("/api/registration", "POST"), new SignUpHandler());
                put(new RequestContext("/api/login", "POST"), new LoginHandler());
                put(new RequestContext("/api/create_chat", "POST"), new ChatCreationHandler());
                put(new RequestContext("/api/add_member", "POST"), new AddMemberToChatHandler());
                put(new RequestContext("/api/remove_member", "POST"), new RemoveMemberHandler());
                put(new RequestContext("/api/send_message", "POST"), new SendMessageHandler());
            }};


    public HandlerContainer() {
    }

    public Handler getHandler(RequestContext requestContext) {
        return registry.get(requestContext);
    }
}

