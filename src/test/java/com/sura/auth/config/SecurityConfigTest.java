package com.sura.auth.config;

import com.sura.auth.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitarios para SecurityConfig
 * 
 * @author SURA
 * @version 1.0.0
 */
@SpringBootTest
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("PasswordEncoder bean: Debe estar configurado correctamente")
    void testPasswordEncoderBeanIsConfigured() {
        // Arrange & Act: se verifica que el bean existe
        
        // Assert: verificar que el PasswordEncoder está configurado
        assertNotNull(passwordEncoder);
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    @DisplayName("PasswordEncoder: Debe codificar contraseñas correctamente")
    void testPasswordEncoderEncodesPasswordsCorrectly() {
        // Arrange: se prepara una contraseña de prueba
        String rawPassword = "testPassword123";

        // Act: se codifica la contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert: verificar que se codifica correctamente
        assertNotNull(encodedPassword);
        // Assert with AssertJ: validaciones adicionales
        assertThat(encodedPassword)
                .isNotNull()
                .isNotEmpty();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        
        // Assert: verificar que se puede validar la contraseña
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertTrue(matches);
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("PasswordEncoder: Debe generar códigos únicos para misma contraseña")
    void testPasswordEncoderGeneratesUniqueEncodings() {
        // Arrange: se prepara una contraseña de prueba
        String password = "samePassword";

        // Act: se codifica la misma contraseña dos veces
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);

        // Assert: verificar que los códigos son diferentes pero válidos
        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(password, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(password, encoded2)).isTrue();
    }

    @Test
    @DisplayName("JwtService bean: Debe estar configurado correctamente")
    void testJwtServiceBeanIsConfigured() {
        // Arrange & Act: se verifica que el bean existe

        // Assert: verificar que el JwtService está configurado
        assertNotNull(jwtService);
        assertThat(jwtService).isNotNull();
    }

    @Test
    @DisplayName("JwtService: Debe generar tokens correctamente")
    void testJwtServiceGeneratesTokensCorrectly() {
        // Arrange: se prepara un username de prueba
        String username = "testuser";

        // Act: se genera un token
        String token = jwtService.generateToken(username);

        // Assert: verificar que se genera el token
        assertNotNull(token);
        assertThat(token)
                .isNotNull()
                .isNotEmpty()
                .contains(".");
    }

    @Test
    @DisplayName("JwtService: Debe extraer username del token correctamente")
    void testJwtServiceExtractsUsernameCorrectly() {
        // Arrange: se prepara un username y se genera un token
        String originalUsername = "testuser";
        String token = jwtService.generateToken(originalUsername);

        // Act: se extrae el username del token
        String extractedUsername = jwtService.extractUsername(token);

        // Assert: verificar que se extrae correctamente
        assertNotNull(extractedUsername);
        assertThat(extractedUsername)
                .isEqualTo(originalUsername)
                .isNotEmpty();
    }

    @Test
    @DisplayName("JwtService: Debe validar tokens correctamente")
    void testJwtServiceValidatesTokensCorrectly() {
        // Arrange: se prepara un username y se genera un token
        String username = "validuser";
        String token = jwtService.generateToken(username);

        // Act: se valida el token
        boolean isValid = jwtService.validateToken(token, username);

        // Assert: verificar que el token es válido
        assertTrue(isValid);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("JwtService: Debe rechazar tokens inválidos")
    void testJwtServiceRejectsInvalidTokens() {
        // Arrange: se prepara un token inválido
        String invalidToken = "invalid.token.here";
        String username = "testuser";

        // Act & Assert: verificar que lanza excepción o retorna false
        try {
            boolean isValid = jwtService.validateToken(invalidToken, username);
            assertThat(isValid).isFalse();
        } catch (Exception e) {
            // Es esperado que lance excepción para tokens inválidos
            assertThat(e).isNotNull();
        }
    }
}
