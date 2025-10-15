package com.sura.auth.service;

import com.sura.auth.dto.AuthResponseDto;
import com.sura.auth.dto.LoginDto;
import com.sura.auth.dto.UserRegistrationDto;
import com.sura.auth.exception.InvalidCredentialsException;
import com.sura.auth.exception.UserNotFoundException;
import com.sura.auth.exception.UserRegistrationException;
import com.sura.auth.model.User;
import com.sura.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de usuarios y autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario en el sistema
     * 
     * @param registrationDto datos del usuario a registrar
     * @return respuesta con información del registro
     * @throws RuntimeException si el usuario ya existe
     */
    public AuthResponseDto registerUser(UserRegistrationDto registrationDto) {
        logger.info("Iniciando registro de usuario: {}", registrationDto.getUsername());

        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            logger.warn("Intento de registro con nombre de usuario existente: {}", registrationDto.getUsername());
            throw new UserRegistrationException("Nombre de usuario ya registrado");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            logger.warn("Intento de registro con email existente: {}", registrationDto.getEmail());
            throw new UserRegistrationException("El email ya está registrado");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setActive(true);

        // Guardar usuario
        userRepository.save(user);
        logger.info("Usuario registrado exitosamente: {}", user.getUsername());

        // Generar token
        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponseDto(token, user.getUsername(), "Registro exitoso");
    }

    /**
     * Autentica un usuario en el sistema
     * 
     * @param loginDto credenciales del usuario
     * @return respuesta con token de autenticación
     * @throws RuntimeException si las credenciales son inválidas
     */
    public AuthResponseDto loginUser(LoginDto loginDto) {
        logger.info("Intento de login para usuario: {}", loginDto.getUsername());

        // Verificar si el usuario existe
        if (!userRepository.existsByUsername(loginDto.getUsername())) {
            logger.warn("Intento de login con usuario inexistente: {}", loginDto.getUsername());
            throw new UserNotFoundException("Usuario no encontrado");
        }

        try {
            // Autenticar usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Generar token
            String token = jwtService.generateToken(loginDto.getUsername());
            logger.info("Login exitoso para usuario: {}", loginDto.getUsername());

            return new AuthResponseDto(token, loginDto.getUsername(), "Login exitoso");

        } catch (Exception e) {
            logger.warn("Fallo en autenticación para usuario: {}", loginDto.getUsername());
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
    }

    /**
     * Cierra la sesión del usuario (logout)
     * 
     * @param username nombre del usuario
     * @return mensaje de confirmación
     */
    public String logoutUser(String username) {
        logger.info("Logout para usuario: {}", username);
        // En un sistema real, aquí se invalidaría el token
        // Por simplicidad, solo retornamos un mensaje
        return "Logout exitoso";
    }
}
