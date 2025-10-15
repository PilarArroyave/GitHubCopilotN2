package com.sura.auth.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests unitarios para AuthResponseDto
 * 
 * @author SURA
 * @version 1.0.0
 */
@DisplayName("AuthResponseDto Tests")
class AuthResponseDtoTest {

    @Test
    @DisplayName("AuthResponseDto constructor con parámetros: Debe crear instancia con valores específicos")
    void testConstructorWithParametersCreatesInstanceCorrectly() {
        // Arrange: se preparan los datos de entrada
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String username = "testuser";
        String message = "Login exitoso";

        // Act: se crea la instancia con constructor parametrizado
        AuthResponseDto dto = new AuthResponseDto(token, username, message);

        // Assert: verificar que se asignan los valores correctamente
        assertNotNull(dto);
        assertEquals(token, dto.getToken());
        assertEquals(username, dto.getUsername());
        assertEquals(message, dto.getMessage());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getToken()).isEqualTo(token).isNotEmpty();
        assertThat(dto.getUsername()).isEqualTo(username).isNotEmpty();
        assertThat(dto.getMessage()).isEqualTo(message).isNotEmpty();
    }

    @Test
    @DisplayName("AuthResponseDto constructor por defecto: Debe crear instancia vacía")
    void testDefaultConstructorCreatesEmptyInstance() {
        // Arrange & Act: se crea una nueva instancia usando constructor por defecto
        AuthResponseDto dto = new AuthResponseDto();

        // Assert: verificar que se crea correctamente vacía
        assertNotNull(dto);
        assertThat(dto.getToken()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getMessage()).isNull();
    }

    @Test
    @DisplayName("AuthResponseDto setters y getters: Debe asignar y recuperar valores correctamente")
    void testSettersAndGettersWorkCorrectly() {
        // Arrange: se crea una nueva instancia y se preparan datos
        AuthResponseDto dto = new AuthResponseDto();
        String token = "newtoken123";
        String username = "newuser";
        String message = "Operación exitosa";

        // Act: se asignan valores usando setters
        dto.setToken(token);
        dto.setUsername(username);
        dto.setMessage(message);

        // Assert: verificar que los getters retornan los valores correctos
        assertEquals(token, dto.getToken());
        assertEquals(username, dto.getUsername());
        assertEquals(message, dto.getMessage());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getToken()).isEqualTo(token);
        assertThat(dto.getUsername()).isEqualTo(username);
        assertThat(dto.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("AuthResponseDto valores null: Debe manejar valores nulos correctamente")
    void testHandlesNullValuesCorrectly() {
        // Arrange: se crea una nueva instancia
        AuthResponseDto dto = new AuthResponseDto();

        // Act: se asignan valores nulos
        dto.setToken(null);
        dto.setUsername(null);
        dto.setMessage(null);

        // Assert: verificar que maneja valores nulos
        assertThat(dto.getToken()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getMessage()).isNull();
    }

    @Test
    @DisplayName("AuthResponseDto valores vacíos: Debe manejar strings vacíos correctamente")
    void testHandlesEmptyStringsCorrectly() {
        // Arrange: se crea una nueva instancia
        AuthResponseDto dto = new AuthResponseDto();

        // Act: se asignan strings vacíos
        dto.setToken("");
        dto.setUsername("");
        dto.setMessage("");

        // Assert: verificar que maneja strings vacíos
        assertEquals("", dto.getToken());
        assertEquals("", dto.getUsername());
        assertEquals("", dto.getMessage());

        // Assert con AssertJ: validaciones adicionales
        assertThat(dto.getToken()).isEmpty();
        assertThat(dto.getUsername()).isEmpty();
        assertThat(dto.getMessage()).isEmpty();
    }
}
