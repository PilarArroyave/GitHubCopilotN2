package com.sura.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para el login de usuarios
 * 
 * @author SURA
 * @version 1.0.0
 */
public class LoginDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Constructor por defecto
     */
    public LoginDto() {
    }

    /**
     * Constructor con parámetros
     * 
     * @param username nombre de usuario
     * @param password contraseña
     */
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
