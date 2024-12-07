package com.radtimes.rad_times_server.jwt_authorization;

import com.radtimes.rad_times_server.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTokenFilterTest {
    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

    @Test
    public void requestURIMatchesWhiteListTest() {
        boolean allValidWhiteListURIPass = Arrays.stream(SecurityConfig.WHITELIST_URLS).anyMatch(uri -> {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
            return authTokenFilter.requestURIMatchesWhiteList(request);
        });
        assertTrue(allValidWhiteListURIPass);

        String[] INVALID_URI = {
                "/login",
                "/graphql",
                "/invalid/**",
                "/admin",
                "/**",
                "/validateToken",
                "/refreshAccessToken",
        };

        boolean allInvalidURIFail = Arrays.stream(INVALID_URI).anyMatch(uri -> {
            MockHttpServletRequest request = new MockHttpServletRequest("*", uri);
            return !authTokenFilter.requestURIMatchesWhiteList(request);
        });
        assertTrue(allInvalidURIFail);
    }
}
