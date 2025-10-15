package com.sura.auth.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests unitarios para excepciones personalizadas
 * 
 * @author SURA
 * @version 1.0.0
 */
@DisplayName("Custom Exceptions Tests")
class ExceptionsTest {

    @Test
    @DisplayName("UserRegistrationException constructor con mensaje: Debe crear excepción con mensaje específico")
    void testUserRegistrationExceptionWithMessage() {
        // Arrange: se prepara el mensaje de excepción
        String errorMessage = "Usuario ya registrado";

        // Act: se crea la excepción
        UserRegistrationException exception = new UserRegistrationException(errorMessage);

        // Assert: verificar que se crea correctamente con mensaje
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertThat(exception).hasMessage(errorMessage);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("InvalidCredentialsException constructor con mensaje: Debe crear excepción con mensaje específico")
    void testInvalidCredentialsExceptionWithMessage() {
        // Arrange: se prepara el mensaje de excepción
        String errorMessage = "Credenciales inválidas";

        // Act: se crea la excepción
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);

        // Assert: verificar que se crea correctamente con mensaje
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertThat(exception).hasMessage(errorMessage);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("UserNotFoundException constructor con mensaje: Debe crear excepción con mensaje específico")
    void testUserNotFoundExceptionWithMessage() {
        // Arrange: se prepara el mensaje de excepción
        String errorMessage = "Usuario no encontrado";

        // Act: se crea la excepción
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Assert: verificar que se crea correctamente con mensaje
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertThat(exception).hasMessage(errorMessage);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("InvalidTokenException constructor con mensaje: Debe crear excepción con mensaje específico")
    void testInvalidTokenExceptionWithMessage() {
        // Arrange: se prepara el mensaje de excepción
        String errorMessage = "Token inválido";

        // Act: se crea la excepción
        InvalidTokenException exception = new InvalidTokenException(errorMessage);

        // Assert: verificar que se crea correctamente con mensaje
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertThat(exception).hasMessage(errorMessage);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Excepciones con mensaje null: Debe manejar mensaje nulo correctamente")
    void testExceptionsWithNullMessage() {
        // Arrange & Act: se crean excepciones con mensaje null
        UserRegistrationException userRegException = new UserRegistrationException(null);
        InvalidCredentialsException credentialsException = new InvalidCredentialsException(null);
        UserNotFoundException userNotFoundException = new UserNotFoundException(null);
        InvalidTokenException tokenException = new InvalidTokenException(null);

        // Assert: verificar que manejan mensaje null
        assertThat(userRegException.getMessage()).isNull();
        assertThat(credentialsException.getMessage()).isNull();
        assertThat(userNotFoundException.getMessage()).isNull();
        assertThat(tokenException.getMessage()).isNull();
    }

    @Test
    @DisplayName("Excepciones con mensaje vacío: Debe manejar string vacío correctamente")
    void testExceptionsWithEmptyMessage() {
        // Arrange: se prepara mensaje vacío
        String emptyMessage = "";

        // Act: se crean excepciones con mensaje vacío
        UserRegistrationException userRegException = new UserRegistrationException(emptyMessage);
        InvalidCredentialsException credentialsException = new InvalidCredentialsException(emptyMessage);
        UserNotFoundException userNotFoundException = new UserNotFoundException(emptyMessage);
        InvalidTokenException tokenException = new InvalidTokenException(emptyMessage);

        // Assert: verificar que manejan mensaje vacío
        assertEquals(emptyMessage, userRegException.getMessage());
        assertEquals(emptyMessage, credentialsException.getMessage());
        assertEquals(emptyMessage, userNotFoundException.getMessage());
        assertEquals(emptyMessage, tokenException.getMessage());

        // Assert con AssertJ: validaciones adicionales
        assertThat(userRegException.getMessage()).isEmpty();
        assertThat(credentialsException.getMessage()).isEmpty();
        assertThat(userNotFoundException.getMessage()).isEmpty();
        assertThat(tokenException.getMessage()).isEmpty();
    }
}
