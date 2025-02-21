package org.example.webservice.service.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.webservice.service.security.CookieService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final CookieService cookieService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Optional<String> token = cookieService.extractToken(request.getCookies());
        if (token.isPresent()) {
            Cookie cookie = cookieService.buildLogoutCookie(token.get());
            response.addCookie(cookie);
            response.sendRedirect("/profile/login");
        }
    }
}
