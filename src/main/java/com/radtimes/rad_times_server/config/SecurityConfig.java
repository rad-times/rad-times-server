package com.radtimes.rad_times_server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthEntryPoint unauthorizedHandler;

    public SecurityConfig(UserDetailsService userDetailsService, JWTAuthEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] WHITELIST_URLS = {
            "/",
            "/index.html",
            "/static/**",
            "/actuator/**",
            "/error/**",
    };

    private static final String[] WHITELIST_SOCKET_ENDPOINTS = {
            "/socket"
    };

    private static final String[] API_ENDPOINTS = {
            "/graphql",
            "/validateToken",
            "/api/**"
    };

    private static final String[] OAUTH_ENDPOINTS = {
            "/login/**"
    };

    @Bean
    @Order(2)
    public SecurityFilterChain oAuthFilterChain(HttpSecurity http) throws Exception {
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = JwtIssuerAuthenticationManagerResolver
                .fromTrustedIssuers("https://accounts.google.com", "https://www.facebook.com");

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(OAUTH_ENDPOINTS).authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));
        return http.build();
    }

}
