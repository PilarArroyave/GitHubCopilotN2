package com.sura.auth.controller;

import com.sura.auth.dto.AuthResponseDto;
import com.sura.auth.dto.UserRegistrationDto;
import com.sura.auth.service.JwtService;
import com.sura.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para AuthController
 * Cubre los casos de prueba de endpoints de autenticación
 * 
 * @author SURA
 * @version 1.0.0
 */
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Arrange: Inicializar los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TC001 - Endpoint registro exitoso: Debe retornar HTTP 200 OK y mensaje 'Registro exitoso'")
    void testRegisterUserReturnsHttp200WithSuccessMessage() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        AuthResponseDto mockResponse = new AuthResponseDto("mockToken", "testuser", "Registro exitoso");
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(mockResponse);

        // Act: se ejecuta el método a probar
        ResponseEntity<Object> result = authController.registerUser(registrationDto);

        // Assert con JUnit: validación básica del código de estado
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // Assert con AssertJ: validaciones adicionales del response
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isInstanceOf(AuthResponseDto.class);

        AuthResponseDto responseBody = (AuthResponseDto) result.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Registro exitoso");
        assertThat(responseBody.getUsername()).isEqualTo("testuser");
        assertThat(responseBody.getToken()).isEqualTo("mockToken").isNotEmpty();
    }

    @Test
    @DisplayName("TC001 - Verificar que el endpoint llama al servicio correctamente")
    void testRegisterUserCallsServiceCorrectly() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        AuthResponseDto mockResponse = new AuthResponseDto("mockToken", "testuser", "Registro exitoso");
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(mockResponse);

        // Act: se ejecuta el método a probar
        authController.registerUser(registrationDto);

        // Assert: verificar que se llama al servicio
        org.mockito.Mockito.verify(userService).registerUser(registrationDto);
    }

    @Test
    @DisplayName("TC001 - Verificar estructura completa del response de registro exitoso")
    void testRegisterUserResponseStructure() {
        // Arrange: se configura el mock y los datos de entrada
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("validuser");
        registrationDto.setEmail("valid@example.com");
        registrationDto.setPassword("validpassword");

        AuthResponseDto mockResponse = new AuthResponseDto("jwt.token.here", "validuser", "Registro exitoso");
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(mockResponse);

        // Act: se ejecuta el método a probar
        ResponseEntity<Object> result = authController.registerUser(registrationDto);

        // Assert con AssertJ: validaciones detalladas de la estructura
        assertThat(result)
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.OK);

        AuthResponseDto responseBody = (AuthResponseDto) result.getBody();
        assertThat(responseBody)
            .isNotNull()
            .satisfies(response -> {
                assertThat(response.getMessage()).isEqualTo("Registro exitoso");
                assertThat(response.getUsername()).isEqualTo("validuser");
                assertThat(response.getToken()).isEqualTo("jwt.token.here");
            });
    }

    @Test
    @DisplayName("TC002 - Endpoint registro usuario existente: Debe retornar HTTP 409 Conflict")
    void testRegisterExistingUserReturnsHttp409() {
        // Arrange: se configura el mock para usuario existente
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("existinguser");
        registrationDto.setEmail("existing@example.com");
        registrationDto.setPassword("password123");

        when(userService.registerUser(any(UserRegistrationDto.class)))
            .thenThrow(new com.sura.auth.exception.UserRegistrationException("Nombre de usuario ya registrado"));

        // Act: se ejecuta el método a probar
        ResponseEntity<Object> result = authController.registerUser(registrationDto);

        // Assert: verificar código de estado y mensaje de error
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        
        assertThat(result.getBody()).isInstanceOf(java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> errorBody = (java.util.Map<String, String>) result.getBody();
        assertThat(errorBody.get("error")).isEqualTo("Nombre de usuario ya registrado");
    }

    @Test
    @DisplayName("TC008 - Logout con token inválido: Debe lanzar InvalidTokenException")
    void testLogoutWithInvalidTokenThrowsException() {
        // Arrange: se configura el mock para token inválido
        jakarta.servlet.http.HttpServletRequest request = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtService.extractUsername("invalidtoken")).thenThrow(new RuntimeException("Invalid JWT"));

        // Act & Assert: se ejecuta el método y verifica la excepción
        com.sura.auth.exception.InvalidTokenException exception = assertThrows(
            com.sura.auth.exception.InvalidTokenException.class,
            () -> authController.logoutUser(request));

        // Assert: verificar mensaje de excepción
        assertEquals("Token inválido", exception.getMessage());
        assertThat(exception).hasMessage("Token inválido");
    }

    @Test
    @DisplayName("TC009 - Logout sin token: Debe retornar HTTP 400 Bad Request con mensaje 'Token requerido'")
    void testLogoutWithoutTokenReturnsBadRequest() {
        // Arrange: se configura el mock sin header Authorization
        jakarta.servlet.http.HttpServletRequest request = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act: se ejecuta el método a probar
        ResponseEntity<Object> result = authController.logoutUser(request);

        // Assert: verificar código de estado y mensaje
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        
        assertThat(result.getBody()).isInstanceOf(java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> errorBody = (java.util.Map<String, String>) result.getBody();
        assertThat(errorBody.get("error")).isEqualTo("Token requerido");
    }

    @Test
    @DisplayName("TC007 - Logout exitoso: Debe retornar HTTP 200 OK con mensaje 'Logout exitoso'")
    void testLogoutSuccessfulReturnsOk() {
        // Arrange: se configura el mock para logout exitoso
        jakarta.servlet.http.HttpServletRequest request = org.mockito.Mockito.mock(jakarta.servlet.http.HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtService.extractUsername("validtoken")).thenReturn("validuser");
        when(userService.logoutUser("validuser")).thenReturn("Logout exitoso");

        // Act: se ejecuta el método a probar
        ResponseEntity<Object> result = authController.logoutUser(request);

        // Assert: verificar código de estado y mensaje
        assertEquals(HttpStatus.OK, result.getStatusCode());
        
        assertThat(result.getBody()).isInstanceOf(java.util.Map.class);
        @SuppressWarnings("unchecked")
        java.util.Map<String, String> responseBody = (java.util.Map<String, String>) result.getBody();
        assertThat(responseBody.get("message")).isEqualTo("Logout exitoso");
    }
}
