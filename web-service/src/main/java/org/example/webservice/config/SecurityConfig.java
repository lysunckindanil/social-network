package org.example.webservice.config;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.security.CookieAuthenticationProvider;
import org.example.webservice.service.security.filter.CookieAuthenticationFilter;
import org.example.webservice.service.security.filter.CsrfLoggerFilter;
import org.example.webservice.service.security.handler.CookieAuthenticationEntryPoint;
import org.example.webservice.service.security.handler.CustomAccessDeniedHandler;
import org.example.webservice.service.security.handler.TokenCookieLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CookieAuthenticationFilter cookieAuthenticationFilter;
    private final TokenCookieLoginSuccessHandler tokenCookieLoginSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CookieAuthenticationProvider cookieAuthenticationProvider;
    private final CookieAuthenticationEntryPoint cookieAuthenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers(HttpMethod.GET, "/js/**", "/css/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling((exception)
                        -> exception
                        .authenticationEntryPoint(cookieAuthenticationEntryPoint)
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .csrf(csrf -> csrf
                        //todo consider proper csrf token
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy(
                                ((authentication, request, response) -> {
                                })))
                .addFilterAfter(new CsrfLoggerFilter(), CsrfFilter.class)
                .addFilterBefore(usernamePasswordAuthenticationFilter(), RequestCacheAwareFilter.class)
                .addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/profile/register", "/profile/login").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/css/**", "/js/**").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().hasRole("USER"));
        return http.build();
    }

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
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter =
                new UsernamePasswordAuthenticationFilter(authenticationConfiguration.getAuthenticationManager());
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(tokenCookieLoginSuccessHandler);
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(
                (request, response, e)
                        -> response.sendRedirect("/profile/login?error=true"));
        usernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/profile/login", "POST"));
        return usernamePasswordAuthenticationFilter;
    }
}