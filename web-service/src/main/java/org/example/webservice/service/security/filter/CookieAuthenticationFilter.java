package org.example.webservice.service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webservice.service.security.CookieService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {
    private final CookieService cookieService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = cookieService.extractToken(request.getCookies());
        if (token.isEmpty()) {
            if (request.getMethod().equals("GET") && !Set.of("/profile/login", "/profile/register").contains(request.getServletPath())) {
                response.sendRedirect("/profile/login");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        PreAuthenticatedAuthenticationToken preAuthenticated = new PreAuthenticatedAuthenticationToken(token.get(), null);

        try {
            Authentication authentication = authenticationConfiguration.getAuthenticationManager().authenticate(preAuthenticated);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
