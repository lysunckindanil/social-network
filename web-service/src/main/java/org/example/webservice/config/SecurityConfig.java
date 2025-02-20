package org.example.webservice.config;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenCookieAuthenticationFilter tokenCookieAuthenticationFilter;
    private final TokenCookieLoginSuccessHandler tokenCookieLoginSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CookieAuthenticationProvider cookieAuthenticationProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        auth
                .authenticationProvider(daoAuthenticationProvider)
                .authenticationProvider(cookieAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling((exception)
                        -> exception.accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/profile/login")))
                .csrf(csrf -> csrf
                        //todo consider csrf token
//                        .csrfTokenRepository(new CookieCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy(
                                ((authentication, request, response) -> {
                                })))
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .addFilterBefore(usernamePasswordAuthenticationFilter(), RequestCacheAwareFilter.class)
                .addFilterBefore(tokenCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/profile/register", "/profile/login").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter(authenticationConfiguration.getAuthenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(tokenCookieLoginSuccessHandler);
        usernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/profile/login", "POST"));
        return usernamePasswordAuthenticationFilter;
    }
}