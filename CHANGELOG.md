# CHANGELOG.md

Este archivo registra automáticamente los cambios realizados por inteligencia artificial (IA) en el proyecto a partir del 14 de octubre de 2025.

---

## Cambios Registrados

- **Descripción:** Implementación completa de tests unitarios para caso de prueba TC001 (Registro exitoso)
- **Fecha:** 2025-10-14
- **Archivos modificados:**
  - src/test/java/com/sura/auth/service/UserServiceTest.java (nuevo)
  - src/test/java/com/sura/auth/controller/AuthControllerTest.java (nuevo)
  - src/test/java/com/sura/auth/dto/UserRegistrationDtoTest.java (nuevo)
- **Generado por:** IA

- **Descripción:** Corrección completa de todos los casos de prueba restantes (TC002, TC006, TC008, TC009)
- **Fecha:** 2025-10-14
- **Archivos modificados:** 
  - src/main/java/com/sura/auth/service/UserService.java
  - src/main/java/com/sura/auth/controller/AuthController.java
  - src/main/java/com/sura/auth/exception/GlobalExceptionHandler.java
  - src/main/java/com/sura/auth/exception/UserNotFoundException.java (nuevo)
  - src/main/java/com/sura/auth/exception/InvalidTokenException.java (nuevo)
  - docs/comparacion_tc.md
- **Generado por:** IA

- **Descripción:** Corrección TC001 - Ajuste de mensaje de registro exitoso para cumplir casos de prueba
- **Fecha:** 2025-10-14
- **Archivos modificados:** src/main/java/com/sura/auth/service/UserService.java, docs/comparacion_tc.md
- **Generado por:** IA

- **Descripción:** Organización del historial en commits atómicos por funcionalidad
- **Fecha:** 2025-10-14
- **Archivos modificados:** Todo el proyecto reorganizado en 12 commits temáticos
- **Generado por:** IA

- **Descripción:** Configuración de Swagger para header Authorization Bearer en endpoint logout
- **Fecha:** 2025-10-14
- **Archivos modificados:** src/main/java/com/sura/auth/controller/AuthController.java, src/main/java/com/sura/auth/config/SwaggerConfig.java
- **Generado por:** IA

- **Descripción:** Creación completa de aplicación Spring Boot con autenticación JWT, endpoints de registro, login y logout
- **Fecha:** 2025-10-14
- **Archivos modificados:** 
  - pom.xml
  - src/main/resources/application.properties
  - src/main/java/com/sura/auth/AuthServiceApplication.java
  - src/main/java/com/sura/auth/model/User.java
  - src/main/java/com/sura/auth/dto/UserRegistrationDto.java
  - src/main/java/com/sura/auth/dto/LoginDto.java
  - src/main/java/com/sura/auth/dto/AuthResponseDto.java
  - src/main/java/com/sura/auth/repository/UserRepository.java
  - src/main/java/com/sura/auth/service/UserService.java
  - src/main/java/com/sura/auth/service/JwtService.java
  - src/main/java/com/sura/auth/service/CustomUserDetailsService.java
  - src/main/java/com/sura/auth/controller/AuthController.java
  - src/main/java/com/sura/auth/config/SecurityConfig.java
  - src/main/java/com/sura/auth/exception/GlobalExceptionHandler.java
  - src/main/java/com/sura/auth/config/SwaggerConfig.java
  - README.md
- **Generado por:** IA

- **Descripción:** Implementación de Swagger/OpenAPI para documentación de endpoints
- **Fecha:** 2025-10-14
- **Archivos modificados:** pom.xml, src/main/java/com/sura/auth/config/SwaggerConfig.java, src/main/java/com/sura/auth/controller/AuthController.java
- **Generado por:** IA

- **Descripción:** Corrección de configuración de seguridad para permitir acceso a Swagger UI y endpoint de logout
- **Fecha:** 2025-10-14
- **Archivos modificados:** src/main/java/com/sura/auth/config/SecurityConfig.java
- **Generado por:** IA

- **Descripción:** Agregada documentación Swagger para endpoint de logout con header de autorización
- **Fecha:** 2025-10-14
- **Archivos modificados:** src/main/java/com/sura/auth/controller/AuthController.java
- **Generado por:** IA

- **Descripción:** Agregada instrucción para registro automático de cambios por IA en copilot-instructions.md
- **Fecha:** 2025-10-14
- **Archivos modificados:** .github/copilot-instructions.md, CHANGELOG.md
- **Generado por:** IA

- **Descripción:** Resumen de implementación de tests unitarios
- **Fecha:** 2025-10-14
- **Archivos modificados:** docs/resumen-tests.md (nuevo)
- **Generado por:** IA

- **Descripción:** Se agregaron tests unitarios para la clase JwtAuthenticationFilter, cubriendo escenarios de token válido, inválido, ausencia de token y manejo de excepciones. Esto mejora la cobertura y calidad de la autenticación JWT.
- **Fecha:** 2025-10-14
- **Archivos modificados:** src/test/java/com/sura/auth/config/JwtAuthenticationFilterTest.java
- **Generado por:** IA

- **Descripción:** Se creó script de verificación de calidad (check-release.ps1) que valida compilación, arranque de aplicación, tests unitarios y cobertura de código. El script ejecuta todos los checks necesarios antes de hacer push y reporta si la versión está lista o tiene problemas.
- **Fecha:** 2025-10-15
- **Archivos modificados:** check-release.ps1
- **Generado por:** IA

---

Cada nueva modificación realizada por IA debe agregarse siguiendo este formato.
