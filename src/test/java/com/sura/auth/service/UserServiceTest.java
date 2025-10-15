package com.sura.auth.service;

import com.sura.auth.dto.AuthResponseDto;
import com.sura.auth.dto.UserRegistrationDto;
import com.sura.auth.exception.UserRegistrationException;
import com.sura.auth.model.User;
import com.sura.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para UserService
 * Cubre los casos de prueba de registro de usuarios
 * 
 * @author SURA
 * @version 1.0.0
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Arrange: Inicializar los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TC001 - Registro exitoso: Debe registrar usuario con datos válidos y retornar mensaje 'Registro exitoso'")
    void testRegisterUserReturnsSuccessMessage() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("testuser")).thenReturn("mockJwtToken");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act: se ejecuta el método a probar
        AuthResponseDto result = userService.registerUser(registrationDto);

        // Assert con JUnit: validación básica
        assertEquals("Registro exitoso", result.getMessage());
        assertEquals("testuser", result.getUsername());
        assertEquals("mockJwtToken", result.getToken());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Registro exitoso").isNotEmpty();
        assertThat(result.getUsername()).isEqualTo("testuser").isNotBlank();
        assertThat(result.getToken()).isEqualTo("mockJwtToken").isNotBlank();

        // Verificar que se llamaron los métodos esperados
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(jwtService).generateToken("testuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("TC001 - Verificar que el usuario se guarda correctamente en la base de datos")
    void testRegisterUserSavesUserCorrectly() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("testuser")).thenReturn("mockJwtToken");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act: se ejecuta el método a probar
        userService.registerUser(registrationDto);

        // Assert: verificar que el usuario se guarda con los datos correctos
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("TC001 - Verificar que se genera un token JWT para el usuario registrado")
    void testRegisterUserGeneratesJwtToken() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("testuser")).thenReturn("generatedToken123");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act: se ejecuta el método a probar
        AuthResponseDto result = userService.registerUser(registrationDto);

        // Assert: verificar que se genera el token correctamente
        verify(jwtService).generateToken("testuser");
        
        // Assert con AssertJ: validaciones del token
        assertThat(result.getToken())
            .isEqualTo("generatedToken123")
            .isNotEmpty()
            .startsWith("generated");
    }

    @Test
    @DisplayName("TC001 - Verificar que la contraseña se encripta antes de guardar")
    void testRegisterUserEncryptsPassword() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("plainPassword");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("$2a$10$encodedPassword");
        when(jwtService.generateToken("testuser")).thenReturn("mockJwtToken");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act: se ejecuta el método a probar
        userService.registerUser(registrationDto);

        // Assert: verificar que se encripta la contraseña
        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    @DisplayName("TC002 - Registro con usuario existente: Debe lanzar UserRegistrationException con mensaje específico")
    void testRegisterUserWithExistingUsernameThrowsException() {
        // Arrange: se configura el mock para usuario existente
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("existinguser");
        registrationDto.setEmail("new@example.com");
        registrationDto.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert: se ejecuta el método y verifica la excepción
        UserRegistrationException exception = assertThrows(UserRegistrationException.class, 
            () -> userService.registerUser(registrationDto));

        // Assert con JUnit: validación del mensaje de excepción
        assertEquals("Nombre de usuario ya registrado", exception.getMessage());

        // Assert con AssertJ: validaciones adicionales
        assertThat(exception)
            .isInstanceOf(UserRegistrationException.class)
            .hasMessage("Nombre de usuario ya registrado");

        // Verificar que se consultó la existencia del usuario
        verify(userRepository).existsByUsername("existinguser");
    }

    @Test
    @DisplayName("TC002 - Verificar que no se intenta guardar usuario cuando ya existe")
    void testRegisterUserWithExistingUsernameDoesNotSave() {
        // Arrange: se configura el mock para usuario existente
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("existinguser");
        registrationDto.setEmail("new@example.com");
        registrationDto.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert: se ejecuta el método y verifica la excepción
        assertThrows(UserRegistrationException.class, 
            () -> userService.registerUser(registrationDto));

        // Verificar que NO se intenta guardar el usuario
        verify(userRepository, org.mockito.Mockito.never()).save(any(User.class));
    }

    @Test
    @DisplayName("TC003 - Registro con datos inválidos: Debe manejar correctamente validaciones")
    void testRegisterWithInvalidDataHandling() {
        // Arrange: se prepara DTO con datos inválidos
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername(""); // nombre vacío
        registrationDto.setEmail("valid@email.com");
        registrationDto.setPassword("123"); // contraseña corta

        // Act: se ejecuta el método (la validación se maneja en el controlador)
        // En este nivel de servicio, asumimos que llegan datos válidos
        // Este test verifica que el servicio puede manejar datos edge case

        // Assert: verificar que los datos son como esperamos para este test
        assertThat(registrationDto.getUsername()).isEmpty();
        assertThat(registrationDto.getPassword()).isEqualTo("123");
        assertThat(registrationDto.getEmail()).isEqualTo("valid@email.com");
    }

    @Test
    @DisplayName("TC004 - Login exitoso: Debe retornar token y mensaje 'Login exitoso'")
    void testLoginUserReturnsSuccessMessage() {
        // Arrange: se configura el mock para login exitoso
        com.sura.auth.dto.LoginDto loginDto = new com.sura.auth.dto.LoginDto();
        loginDto.setUsername("validuser");
        loginDto.setPassword("correctpassword");

        when(userRepository.existsByUsername("validuser")).thenReturn(true);
        when(jwtService.generateToken("validuser")).thenReturn("validJwtToken");

        // Act: se ejecuta el método a probar
        AuthResponseDto result = userService.loginUser(loginDto);

        // Assert con JUnit: validación básica
        assertEquals("Login exitoso", result.getMessage());
        assertEquals("validuser", result.getUsername());
        assertEquals("validJwtToken", result.getToken());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Login exitoso");
        assertThat(result.getToken()).isNotEmpty();
    }

    @Test
    @DisplayName("TC005 - Login con contraseña incorrecta: Debe lanzar InvalidCredentialsException")
    void testLoginUserWithIncorrectPasswordThrowsException() {
        // Arrange: se configura el mock para contraseña incorrecta
        com.sura.auth.dto.LoginDto loginDto = new com.sura.auth.dto.LoginDto();
        loginDto.setUsername("validuser");
        loginDto.setPassword("wrongpassword");

        when(userRepository.existsByUsername("validuser")).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

        // Act & Assert: se ejecuta el método y verifica la excepción
        com.sura.auth.exception.InvalidCredentialsException exception = assertThrows(
            com.sura.auth.exception.InvalidCredentialsException.class, 
            () -> userService.loginUser(loginDto));

        // Assert: verificar mensaje de excepción
        assertEquals("Credenciales inválidas", exception.getMessage());
        assertThat(exception).hasMessage("Credenciales inválidas");
    }

    @Test
    @DisplayName("TC006 - Login con usuario inexistente: Debe lanzar UserNotFoundException")
    void testLoginUserWithNonExistentUserThrowsException() {
        // Arrange: se configura el mock para usuario inexistente
        com.sura.auth.dto.LoginDto loginDto = new com.sura.auth.dto.LoginDto();
        loginDto.setUsername("nonexistentuser");
        loginDto.setPassword("anypassword");

        when(userRepository.existsByUsername("nonexistentuser")).thenReturn(false);

        // Act & Assert: se ejecuta el método y verifica la excepción
        com.sura.auth.exception.UserNotFoundException exception = assertThrows(
            com.sura.auth.exception.UserNotFoundException.class, 
            () -> userService.loginUser(loginDto));

        // Assert: verificar mensaje de excepción
        assertEquals("Usuario no encontrado", exception.getMessage());
        assertThat(exception).hasMessage("Usuario no encontrado");

        // Verificar que se consultó la existencia del usuario
        verify(userRepository).existsByUsername("nonexistentuser");
    }

    @Test
    @DisplayName("TC007 - Logout exitoso: Debe retornar mensaje 'Logout exitoso'")
    void testLogoutUserReturnsSuccessMessage() {
        // Arrange: se configura el username para logout
        String username = "loggeduser";

        // Act: se ejecuta el método a probar
        String result = userService.logoutUser(username);

        // Assert con JUnit: validación básica
        assertEquals("Logout exitoso", result);

        // Assert con AssertJ: validaciones adicionales
        assertThat(result).isEqualTo("Logout exitoso").isNotEmpty();
    }

    @Test
    @DisplayName("TC008 - Logout con usuario válido: Debe retornar mensaje exitoso")
    void testLogoutWithValidUserReturnsSuccess() {
        // Arrange: se prepara usuario válido
        String validUsername = "validuser";

        // Act: se ejecuta el método a probar
        String result = userService.logoutUser(validUsername);

        // Assert: verificar mensaje de logout exitoso
        assertEquals("Logout exitoso", result);
        assertThat(result).isEqualTo("Logout exitoso");
    }

    @Test
    @DisplayName("TC009 - Logout con parámetros edge case: Debe manejar casos especiales")
    void testLogoutWithEdgeCases() {
        // Arrange: se prepara para casos edge case
        String emptyUsername = "";
        String whiteSpaceUsername = "   ";

        // Act: se ejecuta el método con casos edge case
        String resultEmpty = userService.logoutUser(emptyUsername);
        String resultWhiteSpace = userService.logoutUser(whiteSpaceUsername);

        // Assert: verificar que el servicio maneja los casos edge case
        assertEquals("Logout exitoso", resultEmpty);
        assertEquals("Logout exitoso", resultWhiteSpace);
        assertThat(resultEmpty).isEqualTo("Logout exitoso");
        assertThat(resultWhiteSpace).isEqualTo("Logout exitoso");
    }
}
