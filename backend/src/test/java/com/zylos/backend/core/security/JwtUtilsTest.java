package com.zylos.backend.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.entity.User;
import jakarta.servlet.http.Cookie;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String jwtSecret 
            = "einSehrLangerUndSichererGeheimerSchluesselMitMindestens32Zeichen";
    private final long jwtExpirationMs = 3600000;
    private final String jwtTokenName = "jwt";

    @BeforeEach 
    public void setup() {
        this.jwtUtils = new JwtUtils(jwtSecret, jwtExpirationMs, jwtTokenName);  
    }

    private User createUser() {
        User user = User.create(
                "John", 
                "Doe", 
                "john.doe@example.com", 
                "johndoe",
                "password",
                SystemRole.TEACHER
        );
        user.setId(new UUID(0, 1));

        return user;
    }

    @Test 
    public void shouldGenerateAndValidateTokenSuccesfully() {
        User user = createUser();
        
        CustomUserDetails userDetails = new CustomUserDetails(user);

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        String token = cookie.getValue();

        assertNotNull(token);
        assertNotEquals(token, "");

        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals(jwtUtils.getUsernameFromJwtToken(token), "johndoe");
        // If we need more from the token, add more assertions
    }

    @Test
    public void shouldReturnFalse_whenTokenIsInvalid() {
        String invalidToken = "invalid.jwt.token";
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);
        assertFalse(isValid);
    }

    @Test 
    public void shouldReturnFalse_whenTokenIsManipulated() {
        User user = createUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        String token = cookie.getValue();

        String[] tokenParts = token.split("\\.");
        tokenParts[1] += "invalid";
        token = String.join(".", tokenParts);
        
        boolean isValid = jwtUtils.validateJwtToken(token);
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnFalsed_whenTokenIsGeneratedWithAnotherSecret() {
        JwtUtils differenJwtUtils = new JwtUtils(
                "einAndererSehrLangerUndSichererGeheimerSchluesselMitMindestens32Zeichen", 
                jwtExpirationMs, 
                jwtTokenName
        );
        User user = createUser();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        ResponseCookie cookie = differenJwtUtils.generateJwtCookie(userDetails);
        String token = cookie.getValue();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void shouldReturnFalse_whenTokenIsExpired() throws InterruptedException {
        JwtUtils localJwtUtils = new JwtUtils(jwtSecret, 1, jwtTokenName);

        User user = createUser();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        ResponseCookie cookie = localJwtUtils.generateJwtCookie(customUserDetails);
        String token = cookie.getValue();

        Thread.sleep(10);

        boolean isValid = localJwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void shouldExtractJwtFromCookie_whenCookieIsPresent() {
        Cookie[] cookies = {new Cookie("jwt", "sample-jwt-token-value")};

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(cookies);

        String token = jwtUtils.getJwtFromCookie(request);
        assertEquals("sample-jwt-token-value", token);
    }
    
    
    @Test 
    public void shouldReturnNullFromCookie_whenCookieIsMissingOrHasWrongName() {
        MockHttpServletRequest requestWithOutCookies = new MockHttpServletRequest();
        MockHttpServletRequest requestWithWrongCookie = new MockHttpServletRequest();

        Cookie[] cookies = {new Cookie("other_cookie", "other-string")};
        requestWithWrongCookie.setCookies(cookies);

        String tokenA = jwtUtils.getJwtFromCookie(requestWithOutCookies);
        String tokenB = jwtUtils.getJwtFromCookie(requestWithWrongCookie);

        assertNull(tokenA);
        assertNull(tokenB);
    }

    @Test 
    public void shouldGenerateCleanJwtCookieForLogout() {
        ResponseCookie cleanCookie = jwtUtils.getCleanJwtCookie();

        assertEquals("jwt", cleanCookie.getName());
        assertEquals("", cleanCookie.getValue());
        assertEquals(0, cleanCookie.getMaxAge().getSeconds());
        assertEquals("/", cleanCookie.getPath());
        assertTrue(cleanCookie.isHttpOnly());
    }

    @Test 
    public void cookieSecurityFlags() {
        User user = createUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        
        assertTrue(cookie.isHttpOnly());
        assertFalse(cookie.isSecure()); // Local is not secure
        assertEquals("lax", cookie.getSameSite()); // or "Lax" depending on your configuration
    }
}
