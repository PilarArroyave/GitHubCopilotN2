package com.sura.auth.dto;

/**
 * DTO para la respuesta de autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
public class AuthResponseDto {

    private String token;
    private String type = "Bearer";
    private String username;
    private String message;

    /**
     * Constructor por defecto
     */
    public AuthResponseDto() {
    }

    /**
     * Constructor con parámetros
     * 
     * @param token token de acceso
     * @param username nombre de usuario
     * @param message mensaje de respuesta
     */
    public AuthResponseDto(String token, String username, String message) {
        this.token = token;
        this.username = username;
        this.message = message;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
