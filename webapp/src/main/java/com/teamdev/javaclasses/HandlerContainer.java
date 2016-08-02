package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.*;

import java.util.HashMap;
import java.util.Map;

class HandlerContainer {

    private static final Map<RequestContext, Handler> registry =

            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext("/registration", "POST"), new SignUpHandler());
                put(new RequestContext("/login", "POST"), new LoginHandler());
                put(new RequestContext("/create_chat", "POST"), new ChatCreationHandler());
                put(new RequestContext("/add_member", "POST"), new AddMemberToChatHandler());
                put(new RequestContext("/remove_member", "POST"), new RemoveMemberHandler());
                put(new RequestContext("/send_message", "POST"), new SendMessageHandler());
            }};


    private HandlerContainer() {
    }

    static Handler getHandler(RequestContext requestContext) {
        return registry.get(requestContext);
    }
}

