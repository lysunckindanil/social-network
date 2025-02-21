package org.example.webservice.service.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieAuthenticationProvider implements AuthenticationProvider {
    private final CookieService cookieService;
    private final UserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getPrincipal().toString();
        String username;
        try {
            Optional<String> usernameOptional = cookieService.extractUsername(token);
            if (usernameOptional.isPresent()) {
                username = usernameOptional.get();
            } else throw new BadCredentialsException("Bad credentials");
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) throw new BadCredentialsException("User doesn't exist");
        return UsernamePasswordAuthenticationToken
                .authenticated(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
