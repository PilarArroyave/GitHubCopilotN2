package com.sura.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests unitarios para UserRegistrationDto
 * Verifica las validaciones de entrada para TC001
 * 
 * @author SURA
 * @version 1.0.0
 */
class UserRegistrationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Arrange: Inicializar el validador antes de cada test
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("TC001 - DTO válido: Debe pasar todas las validaciones con datos correctos")
    void testValidUserRegistrationDto() {
        // Arrange: se configura un DTO con datos válidos
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("validuser");
        dto.setEmail("valid@example.com");
        dto.setPassword("validpassword123");

        // Act: se ejecuta la validación
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        // Assert con JUnit: validación básica
        assertEquals(0, violations.size());

        // Assert con AssertJ: validaciones adicionales
        assertThat(violations).isEmpty();
        assertThat(dto.getUsername()).isEqualTo("validuser").isNotBlank();
        assertThat(dto.getEmail()).isEqualTo("valid@example.com").contains("@");
        assertThat(dto.getPassword()).isEqualTo("validpassword123").isNotBlank();
    }

    @Test
    @DisplayName("TC001 - Verificar que el DTO acepta nombres de usuario válidos")
    void testValidUsernameAccepted() {
        // Arrange: se configura un DTO con username válido
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser123");
        dto.setEmail("test@example.com");
        dto.setPassword("password123");

        // Act: se ejecuta la validación
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        // Assert: verificar que el username válido no genera violaciones
        assertThat(violations).isEmpty();
        assertThat(dto.getUsername())
            .isEqualTo("testuser123")
            .isNotEmpty()
            .hasSize(11);
    }

    @Test
    @DisplayName("TC001 - Verificar que el DTO acepta emails válidos")
    void testValidEmailAccepted() {
        // Arrange: se configura un DTO con email válido
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("user@domain.com");
        dto.setPassword("password123");

        // Act: se ejecuta la validación
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        // Assert: verificar que el email válido no genera violaciones
        assertThat(violations).isEmpty();
        assertThat(dto.getEmail())
            .isEqualTo("user@domain.com")
            .contains("@")
            .contains(".");
    }

    @Test
    @DisplayName("TC001 - Verificar que el DTO acepta contraseñas válidas")
    void testValidPasswordAccepted() {
        // Arrange: se configura un DTO con contraseña válida
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("securepassword123");

        // Act: se ejecuta la validación
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(dto);

        // Assert: verificar que la contraseña válida no genera violaciones
        assertThat(violations).isEmpty();
        assertThat(dto.getPassword())
            .isEqualTo("securepassword123")
            .isNotEmpty()
            .hasSizeGreaterThan(8);
    }
}
