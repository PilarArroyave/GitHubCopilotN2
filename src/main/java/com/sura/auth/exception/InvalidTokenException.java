package com.sura.auth.exception;

/**
 * Excepción lanzada cuando un token JWT es inválido
 * 
 * @author SURA
 * @version 1.0.0
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
