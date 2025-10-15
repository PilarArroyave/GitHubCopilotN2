package com.sura.auth.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests unitarios para LoginDto
 * 
 * @author SURA
 * @version 1.0.0
 */
@DisplayName("LoginDto Tests")
class LoginDtoTest {

    @Test
    @DisplayName("LoginDto constructor por defecto: Debe crear instancia vacía")
    void testDefaultConstructorCreatesEmptyInstance() {
        // Arrange & Act: se crea una nueva instancia usando constructor por defecto
        LoginDto dto = new LoginDto();

        // Assert: verificar que se crea correctamente vacía
        assertNotNull(dto);
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getPassword()).isNull();
    }

    @Test
    @DisplayName("LoginDto setters y getters: Debe asignar y recuperar valores correctamente")
    void testSettersAndGettersWorkCorrectly() {
        // Arrange: se crea una nueva instancia y se preparan datos
        LoginDto dto = new LoginDto();
        String username = "testuser";
        String password = "password123";

        // Act: se asignan valores usando setters
        dto.setUsername(username);
        dto.setPassword(password);

        // Assert: verificar que los getters retornan los valores correctos
        assertEquals(username, dto.getUsername());
        assertEquals(password, dto.getPassword());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getUsername()).isEqualTo(username).isNotEmpty();
        assertThat(dto.getPassword()).isEqualTo(password).hasSize(11);
    }

    @Test
    @DisplayName("LoginDto valores null: Debe manejar valores nulos correctamente")
    void testHandlesNullValuesCorrectly() {
        // Arrange: se crea una nueva instancia
        LoginDto dto = new LoginDto();

        // Act: se asignan valores nulos
        dto.setUsername(null);
        dto.setPassword(null);

        // Assert: verificar que maneja valores nulos
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getPassword()).isNull();
    }

    @Test
    @DisplayName("LoginDto valores vacíos: Debe manejar strings vacíos correctamente")
    void testHandlesEmptyStringsCorrectly() {
        // Arrange: se crea una nueva instancia
        LoginDto dto = new LoginDto();

        // Act: se asignan strings vacíos
        dto.setUsername("");
        dto.setPassword("");

        // Assert: verificar que maneja strings vacíos
        assertEquals("", dto.getUsername());
        assertEquals("", dto.getPassword());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getUsername()).isEmpty();
        assertThat(dto.getPassword()).isEmpty();
    }

    @Test
    @DisplayName("LoginDto valores especiales: Debe manejar caracteres especiales correctamente")
    void testHandlesSpecialCharactersCorrectly() {
        // Arrange: se crea una nueva instancia y se preparan datos con caracteres especiales
        LoginDto dto = new LoginDto();
        String usernameWithSpecialChars = "user@domain.com";
        String passwordWithSpecialChars = "P@ssw0rd!#$";

        // Act: se asignan valores con caracteres especiales
        dto.setUsername(usernameWithSpecialChars);
        dto.setPassword(passwordWithSpecialChars);

        // Assert: verificar que maneja caracteres especiales
        assertEquals(usernameWithSpecialChars, dto.getUsername());
        assertEquals(passwordWithSpecialChars, dto.getPassword());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getUsername()).contains("@").contains(".");
        assertThat(dto.getPassword()).contains("@").contains("!");
    }
}
