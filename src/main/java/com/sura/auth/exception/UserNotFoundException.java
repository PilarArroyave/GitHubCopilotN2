package com.sura.auth.exception;

/**
 * Excepción lanzada cuando un usuario no es encontrado en el sistema
 * 
 * @author SURA
 * @version 1.0.0
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
