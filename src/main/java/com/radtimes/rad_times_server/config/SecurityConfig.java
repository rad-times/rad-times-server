package com.radtimes.rad_times_server.config;

import com.radtimes.rad_times_server.jwt_authorization.AuthTokenFilter;
import com.radtimes.rad_times_server.jwt_authorization.JWTAuthEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.SecurityFilterChain;

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

    public static final String[] WHITELIST_URLS = {
            "/",
            "/index.html",
            "/static/**",
            "/actuator/**",
            "/error/**",
            "/socket"
    };

    public static final String OAUTH_ENDPOINT = "/login";
    public static final String REFRESH_TOKEN_ENDPOINT = "/refreshAccessToken";

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain whiteListFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(WHITELIST_URLS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST_URLS).permitAll()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain oAuthFilterChain(HttpSecurity http) throws Exception {
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = JwtIssuerAuthenticationManagerResolver
                .fromTrustedIssuers("https://accounts.google.com", "https://www.facebook.com");

        http
                .securityMatcher(OAUTH_ENDPOINT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(OAUTH_ENDPOINT).authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain refreshTokenChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(REFRESH_TOKEN_ENDPOINT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(REFRESH_TOKEN_ENDPOINT).authenticated()
                )
                .authorizeHttpRequests(req -> req
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(req -> req
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
