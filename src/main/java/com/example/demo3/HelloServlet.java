package com.example.demo3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebServlet(name = "helloServlet", value = "/")
public class HelloServlet extends HttpServlet {
    private JakartaServletWebApplication application;
    private final TemplateEngine templateEngine = new TemplateEngine();

    public void init() {
        application = JakartaServletWebApplication.buildApplication(getServletContext());

        final var templateResolver = new WebApplicationTemplateResolver(application);

        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(3600000L);

        // Cache is set to true by default. Set to false if you want templates to be automatically updated when modified.
        templateResolver.setCacheable(true);

        templateEngine.setTemplateResolver(templateResolver);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var exchange = application.buildExchange(request, response);
        WebContext webContext = new WebContext(exchange, exchange.getLocale());

        List<User> users = List.of(
            new User(12, "a@email.com", "password"),
            new User(13, "b@email.com","abcdef")
        );

        webContext.setVariable("users", users);
        templateEngine.process("users", webContext, response.getWriter());
    }

    public void destroy() {
    }
}