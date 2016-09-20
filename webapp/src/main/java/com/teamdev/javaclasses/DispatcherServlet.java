package com.teamdev.javaclasses;

import com.teamdev.javaclasses.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

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

        final Optional<Handler> handler = new HandlerContainer().getHandler(requestContext);

        if (handler.isPresent()) {
            final HandlerProcessingResult processingResult = handler.get().process(request, response);
            response.setStatus(processingResult.getResponseStatus());
            response.getWriter().write(processingResult.getContent());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
