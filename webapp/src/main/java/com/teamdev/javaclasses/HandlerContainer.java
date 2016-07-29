package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.Handler;
import com.teamdev.javaclasses.handler.SignUpHandler;

import java.util.HashMap;
import java.util.Map;

public class HandlerContainer {

    private static final Map<RequestContext, Handler> registry =
            new HashMap<RequestContext, Handler>() {{
                put(new RequestContext("/registration", "POST"), new SignUpHandler());
            }};


    private HandlerContainer() {
        /*TODO use singleton*/
    }

    public static Handler getHandler(RequestContext requestContext) {
        return registry.get(requestContext);
    }
}

