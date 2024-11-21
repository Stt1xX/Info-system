package com.example.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        response.setContentType("application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("responseText", "Invalid username or password");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
