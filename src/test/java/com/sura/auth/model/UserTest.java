package com.sura.auth.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests unitarios para User model
 * 
 * @author SURA  
 * @version 1.0.0
 */
@DisplayName("User Model Tests")
class UserTest {

    @Test
    @DisplayName("User constructor por defecto: Debe crear instancia con valores por defecto")
    void testDefaultConstructorCreatesEmptyUser() {
        // Arrange & Act: se crea una nueva instancia de User
        User user = new User();

        // Assert: verificar que se crea correctamente
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.isActive()).isTrue(); // por defecto es true
    }

    @Test
    @DisplayName("User setters y getters: Debe asignar y recuperar valores correctamente")
    void testSettersAndGettersWorkCorrectly() {
        // Arrange: se crea una nueva instancia de User
        User user = new User();

        // Act: se asignan valores usando setters
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setActive(true);

        // Assert: verificar que los getters retornan los valores correctos
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isActive());

        // Assert con AssertJ: validaciones adicionales
        assertThat(user.getUsername()).isEqualTo("testuser").isNotEmpty();
        assertThat(user.getEmail()).isEqualTo("test@example.com").contains("@");
        assertThat(user.getPassword()).isEqualTo("password123").hasSize(11);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    @DisplayName("User setActive: Debe alternar estado activo/inactivo")
    void testSetActiveTogglesCorrectly() {
        // Arrange: se crea una nueva instancia de User
        User user = new User();

        // Act: se alterna el estado activo
        user.setActive(true);

        // Assert: verificar estado activo
        assertTrue(user.isActive());
        assertThat(user.isActive()).isTrue();

        // Act: se cambia a inactivo
        user.setActive(false);

        // Assert: verificar estado inactivo
        assertFalse(user.isActive());
        assertThat(user.isActive()).isFalse();
    }

    @Test
    @DisplayName("User campos null: Debe manejar valores nulos correctamente")
    void testHandlesNullValuesCorrectly() {
        // Arrange: se crea una nueva instancia de User
        User user = new User();

        // Act: se asignan valores nulos
        user.setUsername(null);
        user.setEmail(null);
        user.setPassword(null);

        // Assert: verificar que maneja valores nulos
        assertThat(user.getUsername()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.isActive()).isTrue(); // por defecto permanece true
    }

    @Test
    @DisplayName("User campos vacíos: Debe manejar strings vacíos correctamente")
    void testHandlesEmptyStringsCorrectly() {
        // Arrange: se crea una nueva instancia de User
        User user = new User();

        // Act: se asignan strings vacíos
        user.setUsername("");
        user.setEmail("");
        user.setPassword("");

        // Assert: verificar que maneja strings vacíos
        assertEquals("", user.getUsername());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());

        // Assert con AssertJ: validaciones adicionales
        assertThat(user.getUsername()).isEmpty();
        assertThat(user.getEmail()).isEmpty();
        assertThat(user.getPassword()).isEmpty();
    }
}
