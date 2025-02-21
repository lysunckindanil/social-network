package org.example.webservice.service.security.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

@Slf4j
public class CsrfLoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        CsrfToken token = (CsrfToken) servletRequest.getAttribute("_csrf");
        log.debug("CSRF token {}", token.getToken());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
