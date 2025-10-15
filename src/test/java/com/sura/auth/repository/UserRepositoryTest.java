package com.sura.auth.repository;

import com.sura.auth.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests unitarios para UserRepository
 * 
 * @author SURA
 * @version 1.0.0
 */
@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Arrange: se prepara un usuario de prueba
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setActive(true);
    }

    @Test
    @DisplayName("findByUsername: Debe encontrar usuario por nombre de usuario")
    void testFindByUsernameReturnsUser() {
        // Arrange: se guarda el usuario en la base de datos
        entityManager.persistAndFlush(testUser);

        // Act: se busca el usuario por username
        Optional<User> result = userRepository.findByUsername("testuser");

        // Assert: verificar que se encuentra el usuario
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        assertEquals("test@example.com", result.get().getEmail());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("findByUsername: Debe retornar vacío si usuario no existe")
    void testFindByUsernameReturnsEmptyWhenUserNotExists() {
        // Arrange: no se guarda ningún usuario

        // Act: se busca un usuario inexistente
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Assert: verificar que no se encuentra el usuario
        assertFalse(result.isPresent());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByEmail: Debe encontrar usuario por email")
    void testFindByEmailReturnsUser() {
        // Arrange: se guarda el usuario en la base de datos
        entityManager.persistAndFlush(testUser);

        // Act: se busca el usuario por email
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Assert: verificar que se encuentra el usuario
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        assertEquals("test@example.com", result.get().getEmail());

        // Assert con AssertJ: validaciones adicionales
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("findByEmail: Debe retornar vacío si email no existe")
    void testFindByEmailReturnsEmptyWhenEmailNotExists() {
        // Arrange: no se guarda ningún usuario

        // Act: se busca un email inexistente
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        // Assert: verificar que no se encuentra el usuario
        assertFalse(result.isPresent());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByUsername: Debe retornar true si usuario existe")
    void testExistsByUsernameReturnsTrueWhenUserExists() {
        // Arrange: se guarda el usuario en la base de datos
        entityManager.persistAndFlush(testUser);

        // Act: se verifica si existe el usuario
        boolean exists = userRepository.existsByUsername("testuser");

        // Assert: verificar que existe
        assertTrue(exists);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByUsername: Debe retornar false si usuario no existe")
    void testExistsByUsernameReturnsFalseWhenUserNotExists() {
        // Arrange: no se guarda ningún usuario

        // Act: se verifica si existe un usuario inexistente
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert: verificar que no existe
        assertFalse(exists);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsByEmail: Debe retornar true si email existe")
    void testExistsByEmailReturnsTrueWhenEmailExists() {
        // Arrange: se guarda el usuario en la base de datos
        entityManager.persistAndFlush(testUser);

        // Act: se verifica si existe el email
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert: verificar que existe
        assertTrue(exists);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByEmail: Debe retornar false si email no existe")
    void testExistsByEmailReturnsFalseWhenEmailNotExists() {
        // Arrange: no se guarda ningún usuario

        // Act: se verifica si existe un email inexistente
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert: verificar que no existe
        assertFalse(exists);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save: Debe guardar usuario correctamente")
    void testSaveUserCorrectly() {
        // Arrange: se prepara un nuevo usuario
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("newpassword");
        newUser.setActive(true);

        // Act: se guarda el usuario
        User savedUser = userRepository.save(newUser);

        // Assert: verificar que se guardó correctamente
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
        assertThat(savedUser.getEmail()).isEqualTo("new@example.com");
        assertThat(savedUser.isActive()).isTrue();

        // Assert: verificar que se puede encontrar en la base de datos
        Optional<User> foundUser = userRepository.findByUsername("newuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("new@example.com");
    }
}
