package com.radtimes.rad_times_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITELIST_URLS = {
            "/",
            "/index.html",
            "/static/**",
            "/actuator/**"
    };

    private static final String[] SOCKET_ENDPOINTS = {
            "/socket"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = JwtIssuerAuthenticationManagerResolver
                .fromTrustedIssuers("https://radtimes.com", "https://accounts.google.com", "https://www.facebook.com");

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST_URLS).permitAll()
                        .requestMatchers(SOCKET_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));
        return http.build();
    }
}
