package org.example.webservice.service.security;

import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CookieService {
    @Getter
    @Setter
    @Value("${security.cookie.default-name}")
    private String DEFAULT_COOKIE_NAME;

    private final JwtService jwtService;

    public Optional<String> extractUsername(String token) {
        if (!jwtService.isTokenValid(token)) return Optional.empty();
        return Optional.ofNullable(jwtService.extractSubject(token));
    }

    public Optional<String> extractToken(Cookie[] cookies) {
        return Stream.of(Optional.ofNullable(cookies).orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName().equals(DEFAULT_COOKIE_NAME))
                .map(Cookie::getValue).findFirst();
    }

    public Cookie buildLogoutCookie(final String token) {
        final Cookie cookie = new Cookie(DEFAULT_COOKIE_NAME, token);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie buildCookie(String username) {
        String token = jwtService.generateToken(username, null);
        Cookie cookie = new Cookie(DEFAULT_COOKIE_NAME, token);
        cookie.setMaxAge((int) jwtService.getExpirationTime() / 1000);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
