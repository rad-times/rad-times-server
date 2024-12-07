package com.radtimes.rad_times_server.jwt_authorization;

import com.radtimes.rad_times_server.config.SecurityConfig;
import com.radtimes.rad_times_server.service.LoginService;
import com.radtimes.rad_times_server.service.authorization.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LoginService loginService;

    public boolean requestURIMatchesWhiteList(HttpServletRequest request) {
        return Arrays.stream(SecurityConfig.WHITELIST_URLS).anyMatch(testUri -> {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(testUri, null, true, new UrlPathHelper());
            return matcher.matches(request);
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String requestURI = request.getRequestURI();
            String jwt = loginService.getBearerToken(request);
            boolean isValidToken = false;
            String email = null;

            // For /login request and whitelisted URLs,
            if (requestURI.equals(SecurityConfig.OAUTH_ENDPOINT) || requestURIMatchesWhiteList(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Refresh token update call
            if (requestURI.equals(SecurityConfig.REFRESH_TOKEN_ENDPOINT)) {
                isValidToken = jwtUtils.validateRefreshToken(jwt);
                email = isValidToken ? jwtUtils.getUserEmailFromRefreshToken(jwt) : null;

            // Everything else required standard JWT token auth
            } else {
                isValidToken = jwtUtils.validateJwtToken(jwt);
                email = isValidToken ? jwtUtils.getUserEmailFromJwtToken(jwt) : null;
            }

            if (isValidToken && email != null) {
                UserDetails userDetails = userDetailsService.loadUserByEmail(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
