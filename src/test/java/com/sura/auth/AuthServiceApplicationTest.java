package com.sura.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios para AuthServiceApplication
 * Siguiendo los lineamientos de testing del proyecto:
 * - JUnit para estructura
 * - AssertJ para aserciones avanzadas
 * - Cobertura de casos positivos, negativos y de borde
 * - Nomenclatura descriptiva en inglés
 * - Comentarios explicativos Arrange/Act/Assert
 */
class AuthServiceApplicationTest {

    @Test
    @DisplayName("Application Class: Debe tener la estructura correcta de clase principal")
    void testApplicationClassStructureIsCorrect() {
        // Arrange: obtenemos la clase AuthServiceApplication

        // Act: verificamos la estructura de la clase
        Class<?> clazz = AuthServiceApplication.class;

        // Assert con JUnit: validaciones básicas
        org.junit.jupiter.api.Assertions.assertTrue(java.lang.reflect.Modifier.isPublic(clazz.getModifiers()));
        org.junit.jupiter.api.Assertions.assertFalse(java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()));

        // Assert con AssertJ: validaciones adicionales
        assertThat(clazz.getPackage().getName())
                .isEqualTo("com.sura.auth");
        assertThat(clazz.getSimpleName())
                .isEqualTo("AuthServiceApplication");
        assertThat(clazz)
                .isNotNull()
                .hasAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
    }

    @Test
    @DisplayName("Main Method: Debe existir y ser accesible el método main")
    void testMainMethodExistsAndIsAccessible() {
        // Arrange: obtenemos la clase y buscamos el método main

        // Act: verificamos la existencia del método main
        java.lang.reflect.Method[] methods = AuthServiceApplication.class.getDeclaredMethods();
        java.lang.reflect.Method mainMethod = null;
        for (java.lang.reflect.Method method : methods) {
            if ("main".equals(method.getName())) {
                mainMethod = method;
                break;
            }
        }

        // Assert con JUnit: validación básica
        org.junit.jupiter.api.Assertions.assertNotNull(mainMethod);
        org.junit.jupiter.api.Assertions.assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        org.junit.jupiter.api.Assertions.assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));

        // Assert con AssertJ: validaciones adicionales
        assertThat(mainMethod.getParameterTypes())
                .hasSize(1)
                .containsExactly(String[].class);
        assertThat(mainMethod.getReturnType())
                .isEqualTo(void.class);
    }

    @Test
    @DisplayName("SpringBootApplication Annotation: Debe tener la anotación @SpringBootApplication")
    void testSpringBootApplicationAnnotationPresent() {
        // Arrange: obtenemos la clase AuthServiceApplication

        // Act: verificamos la presencia de la anotación
        boolean hasAnnotation = AuthServiceApplication.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class);

        // Assert con JUnit: validación básica
        org.junit.jupiter.api.Assertions.assertTrue(hasAnnotation);

        // Assert con AssertJ: validaciones adicionales
        assertThat(AuthServiceApplication.class.getAnnotations())
                .isNotEmpty()
                .anyMatch(annotation -> annotation.annotationType()
                        .equals(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    @DisplayName("Class Package: Debe estar en el paquete correcto")
    void testClassIsInCorrectPackage() {
        // Arrange: obtenemos información del paquete

        // Act: verificamos el paquete de la clase
        String packageName = AuthServiceApplication.class.getPackage().getName();
        String expectedPackage = "com.sura.auth";

        // Assert con JUnit: validación básica
        org.junit.jupiter.api.Assertions.assertEquals(expectedPackage, packageName);

        // Assert con AssertJ: validaciones adicionales
        assertThat(packageName)
                .isEqualTo(expectedPackage)
                .startsWith("com.sura")
                .endsWith("auth");
    }
}
