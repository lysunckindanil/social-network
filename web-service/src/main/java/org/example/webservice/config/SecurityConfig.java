package org.example.webservice.config;

import lombok.RequiredArgsConstructor;
import org.example.webservice.service.security.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling((exception)
                        -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/profile/register", "/profile/login").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/css/**", "/js/**").permitAll())
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/profile/login")
                        .loginProcessingUrl("/profile/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/profile/login?error")
                )
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/profile/logout")
                        .logoutSuccessUrl("/profile/login"));
        return http.build();
    }
}