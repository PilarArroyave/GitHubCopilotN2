package com.sura.auth.exception;

/**
 * Excepción personalizada para errores de registro de usuario
 * 
 * @author SURA
 * @version 1.0.0
 */
public class UserRegistrationException extends RuntimeException {

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
