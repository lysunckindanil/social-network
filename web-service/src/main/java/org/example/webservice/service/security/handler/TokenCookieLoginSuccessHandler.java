package org.example.webservice.service.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webservice.service.security.CookieService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenCookieLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CookieService cookieService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.debug("onAuthenticationSuccess");
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            String username = authentication.getName();
            if (username != null) {
                Cookie cookie = cookieService.buildCookie(username);
                response.addCookie(cookie);
                response.sendRedirect("/");
            }
        }
    }
}
