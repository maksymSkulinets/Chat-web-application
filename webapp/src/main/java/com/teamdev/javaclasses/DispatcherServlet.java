package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.Handler;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    /*TODO ADD LOGGING*/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        final RequestContext requestContext = new RequestContext(uri, method);

        final Handler handler = HandlerContainer.getHandler(requestContext);

        final JSONObject transferContent = handler.process(request, response);
        response.getWriter().write(transferContent.toJSONString());

    }
}
