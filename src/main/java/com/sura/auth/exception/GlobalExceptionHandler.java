package com.sura.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación
 * 
 * @author SURA
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ERROR_KEY = "error";

    /**
     * Maneja excepciones de registro de usuario
     * 
     * @param ex excepción de registro
     * @return respuesta de error
     */
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<Map<String, String>> handleUserRegistrationException(UserRegistrationException ex) {
        logger.warn("Error de registro: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Maneja excepciones de credenciales inválidas
     * 
     * @param ex excepción de credenciales
     * @return respuesta de error
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.warn("Error de autenticación: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Maneja excepciones de usuario no encontrado
     * 
     * @param ex excepción de usuario no encontrado
     * @return respuesta de error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("Usuario no encontrado: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja excepciones de token inválido
     * 
     * @param ex excepción de token inválido
     * @return respuesta de error
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException ex) {
        logger.warn("Token inválido: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Maneja errores de validación de campos
     * 
     * @param ex excepción de validación
     * @return respuesta con errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach( error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        errors.put("validationErrors", fieldErrors);
        errors.put(ERROR_KEY, "Errores de validación en los datos proporcionados");
        
        logger.warn("Errores de validación: {}", fieldErrors);
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Maneja excepciones generales no controladas
     * 
     * @param ex excepción general
     * @return respuesta de error interno
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Error interno del servidor: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
