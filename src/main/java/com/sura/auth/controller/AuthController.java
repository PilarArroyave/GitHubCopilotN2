package com.sura.auth.controller;

import com.sura.auth.dto.AuthResponseDto;
import com.sura.auth.dto.LoginDto;
import com.sura.auth.dto.UserRegistrationDto;
import com.sura.auth.exception.InvalidTokenException;
import com.sura.auth.service.JwtService;
import com.sura.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para la gestión de autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String ERROR_KEY = "error";

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para registrar un nuevo usuario
     * 
     * @param registrationDto datos del usuario a registrar
     * @return respuesta con información del registro
     */
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        logger.info("Solicitud de registro recibida para usuario: {}", registrationDto.getUsername());
        
        try {
            AuthResponseDto response = userService.registerUser(registrationDto);
            logger.info("Registro exitoso para usuario: {}", registrationDto.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.error("Error en registro para usuario {}: {}", registrationDto.getUsername(), e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put(ERROR_KEY, e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Endpoint para autenticar un usuario (login)
     * 
     * @param loginDto credenciales del usuario
     * @return respuesta con token de autenticación
     */
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginDto loginDto) {
        logger.info("Solicitud de login recibida para usuario: {}", loginDto.getUsername());
        
        try {
            AuthResponseDto response = userService.loginUser(loginDto);
            logger.info("Login exitoso para usuario: {}", loginDto.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.error("Error en login para usuario {}: {}", loginDto.getUsername(), e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put(ERROR_KEY, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Endpoint para cerrar sesión (logout)
     * 
     * @param request petición HTTP para extraer el token
     * @return respuesta de confirmación
     */
    @Operation(
        summary = "Logout",
        description = "Cierra la sesión del usuario. Requiere autenticación Bearer token.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout exitoso"),
        @ApiResponse(responseCode = "400", description = "Token no proporcionado"),
        @ApiResponse(responseCode = "401", description = "Token inválido"),
        @ApiResponse(responseCode = "500", description = "Error al procesar logout")
    })
    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Intento de logout sin token válido");
            Map<String, String> error = new HashMap<>();
            error.put(ERROR_KEY, "Token requerido");
            return ResponseEntity.badRequest().body(error);
        }
        
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            String message = userService.logoutUser(username);
            logger.info("Logout exitoso para usuario: {}", username);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error en logout: {}", e.getMessage());
            throw new InvalidTokenException("Token inválido");
        }
    }

    /**
     * Endpoint de verificación de estado del servicio
     * 
     * @return estado del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "auth-service");
        return ResponseEntity.ok(response);
    }
}
