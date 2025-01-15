package org.example.webservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/profile/register").permitAll())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().hasRole("USER"))
                .formLogin(form -> form.loginPage("/profile/login").loginProcessingUrl("/profile/login").
                        failureUrl("/profile/login?error=true").defaultSuccessUrl("/").permitAll())
                .logout(form -> form.logoutUrl("/profile/logout").logoutSuccessUrl("/profile/login"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}