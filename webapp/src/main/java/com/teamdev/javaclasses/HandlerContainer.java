package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.Handler;

import java.util.HashMap;
import java.util.Map;

class HandlerContainer {

    private final Map<RequestContext, Handler> registry =

            new HashMap<RequestContext, Handler>() {{

            }};


    Handler getHandler(RequestContext requestContext) {
        return registry.get(requestContext);
    }
}

