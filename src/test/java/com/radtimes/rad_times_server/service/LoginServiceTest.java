package com.radtimes.rad_times_server.service;

import com.radtimes.rad_times_server.jwt_authorization.JWTUtil;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.oauth.FacebookAuthenticationService;
import com.radtimes.rad_times_server.service.oauth.GoogleAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    private LoginService loginService;

    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private PersonService personService;
    @Mock
    private GoogleAuthenticationService googleAuthenticationService;
    @Mock
    private FacebookAuthenticationService facebookAuthenticationService;

    @BeforeEach
    public void setUp() {
        this.loginService = new LoginService(jwtUtil, personService, googleAuthenticationService, facebookAuthenticationService);
    }

    @Test
    public void getBearerTokenTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String expectedToken = "12345";
        request.addHeader("Authorization", "Bearer " + expectedToken);

        String token = loginService.getBearerToken(request);
        assertEquals(expectedToken, token);

        MockHttpServletRequest badRequest = new MockHttpServletRequest();
        badRequest.addHeader("Authorization",  expectedToken);

        String badRequestResp = loginService.getBearerToken(badRequest);
        assertNull(badRequestResp);
    }

    @Test
    public void createAuthTokenPairTest() {
        PersonModel mockPerson = new PersonModel();
        String email = "test@test.com";
        mockPerson.setEmail(email);
        mockPerson.setLanguage_code(PersonModel.LanguageLocale.EN);

        String expectedToken = "12345.6780.5678";
        Mockito.when(jwtUtil.createJWT(email, PersonModel.LanguageLocale.EN)).thenReturn(expectedToken);

        String expectedRefresh = "67890.123.45678";
        Mockito.when(jwtUtil.createRefreshToken(email)).thenReturn(expectedRefresh);

        LoginService.TokenPair tokenPair = loginService.createAuthTokenPair(mockPerson);
        assertEquals(String.class, tokenPair.getAccessToken().getClass());
        assertEquals(expectedToken, tokenPair.getAccessToken());
        assertEquals(String.class, tokenPair.getRefreshToken().getClass());
        assertEquals(expectedRefresh, tokenPair.getRefreshToken());
    }

    @Test
    public void getRefreshedTokenPairTest() {
        // Standard use case
        String refreshToken = "67890.123.45678";
        String email = "test@test.com";
        PersonModel mockPerson = new PersonModel();
        mockPerson.setEmail(email);
        mockPerson.setLanguage_code(PersonModel.LanguageLocale.EN);

        Mockito.when(jwtUtil.getUserEmailFromRefreshToken(refreshToken)).thenReturn(email);
        Mockito.when(personService.findPersonByEmail(email)).thenReturn(Optional.of(mockPerson));
        Mockito.when(personService.getRefreshToken(email)).thenReturn(Optional.of(refreshToken));

        String expectedToken = "12345.6780.5678";
        Mockito.when(jwtUtil.createJWT(email, PersonModel.LanguageLocale.EN)).thenReturn(expectedToken);

        String expectedRefresh = "67890.123.45678";
        Mockito.when(jwtUtil.createRefreshToken(email)).thenReturn(expectedRefresh);

        LoginService.TokenPair tokenPair = loginService.getRefreshedTokenPair(refreshToken);
        assertEquals(String.class, tokenPair.getAccessToken().getClass());
        assertEquals(expectedToken, tokenPair.getAccessToken());
        assertEquals(String.class, tokenPair.getRefreshToken().getClass());
        assertEquals(expectedRefresh, tokenPair.getRefreshToken());

        // Person is null
        Mockito.when(personService.findPersonByEmail(email)).thenReturn(Optional.empty());
        LoginService.TokenPair nullPersonResp = loginService.getRefreshedTokenPair(refreshToken);
        assertNull(nullPersonResp);
        Mockito.when(personService.findPersonByEmail(email)).thenReturn(Optional.of(mockPerson));

        // Saved Refresh token is null
        Mockito.when(personService.getRefreshToken(email)).thenReturn(Optional.empty());
        LoginService.TokenPair nullTokenResp = loginService.getRefreshedTokenPair(refreshToken);
        assertNull(nullTokenResp);
        Mockito.when(personService.getRefreshToken(email)).thenReturn(Optional.of(refreshToken));

        // Saved token does not match passed in token
        Mockito.when(personService.getRefreshToken(email)).thenReturn(Optional.of("NOT-MY_SAVED_TOKEN"));
        LoginService.TokenPair clearSavedTokenResp = loginService.getRefreshedTokenPair(refreshToken);
        verify(personService).clearRefreshToken(email);
        assertNull(clearSavedTokenResp);
    }
}
