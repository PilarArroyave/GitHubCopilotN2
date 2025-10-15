package com.sura.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación de autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
@SpringBootApplication
public class AuthServiceApplication {

    /**
     * Método principal para iniciar la aplicación Spring Boot
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
