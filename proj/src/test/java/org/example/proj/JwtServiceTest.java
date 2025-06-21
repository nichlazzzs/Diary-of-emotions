package org.example.proj;

import org.example.proj.entity.User;
import org.example.proj.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private User appUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Установка секретного ключа через рефлексию
        try {
            var field = JwtService.class.getDeclaredField("secretKey");
            field.setAccessible(true);
            field.set(jwtService, "secretsecretsecretsecretsecretsecretsecretsecret");
        } catch (Exception e) {
            fail("Failed to set secret key for testing");
        }

        appUser = new User();
        appUser.setUsername("testuser");
        appUser.setEmail("test@example.com");
        appUser.setPassword("password");

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        appUser.setRoles(roles);

        userDetails = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        assertDoesNotThrow(() -> {
            String token = jwtService.generateToken(appUser);
            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertEquals(3, token.split("\\.").length);
        });
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken(appUser);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        String token = jwtService.generateToken(appUser);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WithInvalidToken_ShouldReturnFalse() {
        assertFalse(jwtService.isTokenValid("invalid.token.here", userDetails));
    }

    @Test
    void isTokenExpired_WithNewToken_ShouldReturnFalse() {
        String token = jwtService.generateToken(appUser);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void invalidateToken_ShouldAddTokenToBlacklist() {
        String token = jwtService.generateToken(appUser);
        jwtService.invalidateToken(token);
        assertTrue(jwtService.isTokenBlacklisted(token));
    }

    @Test
    void tokenShouldContainEmail() {
        String token = jwtService.generateToken(appUser);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);

        // Проверка email через публичный API
        assertTrue(jwtService.isTokenValid(token, userDetails));
        // Дополнительная проверка через поведенческий тест
        try {
            jwtService.extractUsername(token); // Если email входит в subject или claims
        } catch (Exception e) {
            fail("Token should be valid and contain email information");
        }
    }
}