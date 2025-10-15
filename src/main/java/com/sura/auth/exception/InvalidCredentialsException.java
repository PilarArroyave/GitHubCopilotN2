package com.sura.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepción personalizada para errores de autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
public class InvalidCredentialsException extends AuthenticationException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
