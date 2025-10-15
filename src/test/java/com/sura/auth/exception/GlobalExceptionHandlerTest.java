package com.sura.auth.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GlobalExceptionHandler
 * Siguiendo los lineamientos de testing del proyecto:
 * - JUnit para estructura
 * - Mockito para mocks
 * - AssertJ para aserciones avanzadas
 * - Cobertura de casos positivos, negativos y de borde
 * - Nomenclatura descriptiva en inglés
 * - Comentarios explicativos Arrange/Act/Assert
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("UserRegistrationException: Debe manejar excepción de registro de usuario correctamente")
    void testHandleUserRegistrationExceptionReturnsConflictStatus() {
        // Arrange: se prepara la excepción de registro
        String errorMessage = "Nombre de usuario ya registrado";
        UserRegistrationException exception = new UserRegistrationException(errorMessage);

        // Act: se maneja la excepción
        ResponseEntity<Map<String, String>> result = globalExceptionHandler.handleUserRegistrationException(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNotNull(result.getBody());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsValue(errorMessage);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("InvalidCredentialsException: Debe manejar excepción de credenciales inválidas correctamente")
    void testHandleInvalidCredentialsExceptionReturnsUnauthorizedStatus() {
        // Arrange: se prepara la excepción de credenciales inválidas
        String errorMessage = "Credenciales inválidas";
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);

        // Act: se maneja la excepción
        ResponseEntity<Map<String, String>> result = globalExceptionHandler.handleInvalidCredentialsException(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsValue(errorMessage);
    }

    @Test
    @DisplayName("UserNotFoundException: Debe manejar excepción de usuario no encontrado correctamente")
    void testHandleUserNotFoundExceptionReturnsNotFoundStatus() {
        // Arrange: se prepara la excepción de usuario no encontrado
        String errorMessage = "Usuario no encontrado";
        UserNotFoundException exception = new UserNotFoundException(errorMessage);

        // Act: se maneja la excepción
        ResponseEntity<Map<String, String>> result = globalExceptionHandler.handleUserNotFoundException(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsValue(errorMessage);
    }

    @Test
    @DisplayName("InvalidTokenException: Debe manejar excepción de token inválido correctamente")
    void testHandleInvalidTokenExceptionReturnsUnauthorizedStatus() {
        // Arrange: se prepara la excepción de token inválido
        String errorMessage = "Token inválido";
        InvalidTokenException exception = new InvalidTokenException(errorMessage);

        // Act: se maneja la excepción
        ResponseEntity<Map<String, String>> result = globalExceptionHandler.handleInvalidTokenException(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsValue(errorMessage);
    }

    @Test
    @DisplayName("MethodArgumentNotValidException: Debe manejar errores de validación correctamente")
    void testHandleValidationExceptionsReturnsBadRequestWithFieldErrors() {
        // Arrange: se prepara una excepción de validación mockeada
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("userDto", "username", "Username is required");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        // Act: se maneja la excepción de validación
        ResponseEntity<Map<String, Object>> result = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsKey("validationErrors");
        
        Object validationErrors = result.getBody().get("validationErrors");
        assertThat(validationErrors).isInstanceOf(Map.class);
        
        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorsMap = (Map<String, String>) validationErrors;
        assertThat(fieldErrorsMap)
                .containsKey("username")
                .containsValue("Username is required");
    }

    @Test
    @DisplayName("Generic Exception: Debe manejar excepciones generales con error interno del servidor")
    void testHandleGenericExceptionReturnsInternalServerError() {
        // Arrange: se prepara una excepción general
        String errorMessage = "Something went wrong";
        Exception exception = new Exception(errorMessage);

        // Act: se maneja la excepción general
        ResponseEntity<Map<String, String>> result = globalExceptionHandler.handleGenericException(exception);

        // Assert con JUnit: validación básica
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result.getBody())
                .containsKey("error")
                .containsValue("Error interno del servidor");
    }

    @Test
    @DisplayName("Exception Messages: Debe preservar mensajes de error personalizados")
    void testExceptionHandlersPreserveCustomErrorMessages() {
        // Arrange: se preparan diferentes excepciones con mensajes personalizados
        String customMessage = "Mensaje personalizado de error";
        UserRegistrationException regEx = new UserRegistrationException(customMessage);
        InvalidCredentialsException credEx = new InvalidCredentialsException(customMessage);

        // Act: se manejan ambas excepciones
        ResponseEntity<Map<String, String>> regResult = globalExceptionHandler.handleUserRegistrationException(regEx);
        ResponseEntity<Map<String, String>> credResult = globalExceptionHandler.handleInvalidCredentialsException(credEx);

        // Assert con AssertJ: validaciones de preservación de mensajes
        assertThat(regResult.getBody()).containsEntry("error", customMessage);
        assertThat(credResult.getBody()).containsEntry("error", customMessage);
    }

    @Test
    @DisplayName("Response Structure: Debe mantener estructura consistente en todas las respuestas")
    void testAllHandlersReturnConsistentResponseStructure() {
        // Arrange: se preparan diferentes tipos de excepciones
        UserRegistrationException regEx = new UserRegistrationException("Error de registro");
        InvalidTokenException tokenEx = new InvalidTokenException("Token inválido");
        UserNotFoundException notFoundEx = new UserNotFoundException("Usuario no encontrado");

        // Act: se obtienen las respuestas de diferentes handlers
        ResponseEntity<Map<String, String>> regResponse = globalExceptionHandler.handleUserRegistrationException(regEx);
        ResponseEntity<Map<String, String>> tokenResponse = globalExceptionHandler.handleInvalidTokenException(tokenEx);
        ResponseEntity<Map<String, String>> notFoundResponse = globalExceptionHandler.handleUserNotFoundException(notFoundEx);

        // Assert con AssertJ: validaciones de estructura consistente
        assertThat(regResponse.getBody()).containsKey("error");
        assertThat(tokenResponse.getBody()).containsKey("error");
        assertThat(notFoundResponse.getBody()).containsKey("error");
        
        assertThat(regResponse.getBody()).hasSize(1);
        assertThat(tokenResponse.getBody()).hasSize(1);
        assertThat(notFoundResponse.getBody()).hasSize(1);
    }
}
