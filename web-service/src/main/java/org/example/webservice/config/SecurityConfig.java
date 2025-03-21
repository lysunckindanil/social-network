package org.example.webservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${security.remember-me.secret-key}")
    private String REMEMBER_ME_SECRET_KEY;
    @Value("${security.remember-me.expiration-time}")
    private int REMEMBER_ME_SECRET_KEY_EXPIRE;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .exceptionHandling((exception)
                        -> exception
                        .accessDeniedHandler((req, resp, accessDeniedException) -> resp.sendRedirect("/home")))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/register", "/login").anonymous()
                                .requestMatchers("/css/**", "/js/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login?error")
                )
                .sessionManagement(
                        session -> session
                                .maximumSessions(2)
                                .maxSessionsPreventsLogin(false)
                )
                .rememberMe(remember -> remember
                        .key(REMEMBER_ME_SECRET_KEY)
                        .tokenValiditySeconds(REMEMBER_ME_SECRET_KEY_EXPIRE)
                        .rememberMeParameter("remember-me"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"));
        return http.build();
    }
}