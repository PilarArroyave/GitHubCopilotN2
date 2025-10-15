# Comparación de Casos de Prueba vs Funcionalidad Actual

Este documento analiza cada caso de prueba definido en `test-cases.md` y compara la funcionalidad esperada con la implementación actual de la aplicación. Se indica qué funciones cumplen lo esperado y qué aspectos requieren corrección o mejora.

---

## TC001 - Registro exitoso ✅ RESUELTO
- **Funcionalidad:** `AuthController.registerUser` y `UserService.registerUser`
- **Cumple:** Sí. Si el usuario no existe y los datos son válidos, retorna HTTP 200 OK y mensaje "Registro exitoso".
- **Aspectos a corregir:** ~~El mensaje en el resultado esperado es "Registro exitoso"; la implementación retorna "Usuario registrado exitosamente". Si se requiere exactitud textual, ajustar el mensaje.~~ **CORREGIDO** - El mensaje ahora es exactamente "Registro exitoso".

---

## TC002 - Registro con nombre de usuario existente ✅ RESUELTO
- **Funcionalidad:** `UserService.registerUser` y `GlobalExceptionHandler.handleUserRegistrationException`
- **Cumple:** Sí. Si el usuario existe, lanza excepción y retorna HTTP 409 Conflict con mensaje "Nombre de usuario ya registrado".
- **Aspectos a corregir:** ~~El caso espera HTTP 409 Conflict y mensaje "Nombre de usuario ya registrado". Se debe cambiar el código de estado a 409 y ajustar el mensaje para coincidir exactamente.~~ **CORREGIDO** - Ahora retorna HTTP 409 y el mensaje exacto.

---

## TC003 - Registro con datos inválidos
- **Funcionalidad:** Validación en `AuthController.registerUser` y `GlobalExceptionHandler.handleValidationExceptions`
- **Cumple:** Sí. Si los datos son inválidos, retorna HTTP 400 Bad Request y error de validación.
- **Aspectos a corregir:** El mensaje de error es genérico, pero cumple con lo esperado.

---

## TC004 - Login exitoso
- **Funcionalidad:** `AuthController.loginUser` y `UserService.loginUser`
- **Cumple:** Sí. Si usuario y contraseña son correctos, retorna HTTP 200 OK y token de autenticación.
- **Aspectos a corregir:** El mensaje es "Login exitoso"; cumple con lo esperado.

---

## TC005 - Login con contraseña incorrecta
- **Funcionalidad:** `UserService.loginUser` y `GlobalExceptionHandler.handleInvalidCredentialsException`
- **Cumple:** Sí. Retorna HTTP 401 Unauthorized y mensaje "Credenciales inválidas".
- **Aspectos a corregir:** El mensaje coincide, cumple con lo esperado.

---

## TC006 - Login con usuario inexistente ✅ RESUELTO
- **Funcionalidad:** `UserService.loginUser` y `GlobalExceptionHandler.handleUserNotFoundException`
- **Cumple:** Sí. Si el usuario no existe, retorna HTTP 404 Not Found y mensaje "Usuario no encontrado".
- **Aspectos a corregir:** ~~El caso espera HTTP 404 Not Found y mensaje "Usuario no encontrado". Se debe diferenciar entre usuario inexistente y credenciales inválidas, y retornar 404 con el mensaje exacto.~~ **CORREGIDO** - Ahora verifica si el usuario existe antes de autenticar y retorna 404 con el mensaje exacto.

---

## TC007 - Logout exitoso
- **Funcionalidad:** `AuthController.logoutUser` y `UserService.logoutUser`
- **Cumple:** Sí. Retorna HTTP 200 OK y mensaje "Logout exitoso".
- **Aspectos a corregir:** El mensaje coincide, cumple con lo esperado.

---

## TC008 - Logout con token inválido ✅ RESUELTO
- **Funcionalidad:** `AuthController.logoutUser` y `GlobalExceptionHandler.handleInvalidTokenException`
- **Cumple:** Sí. Si el token es inválido, retorna HTTP 401 Unauthorized y mensaje "Token inválido".
- **Aspectos a corregir:** ~~El caso espera HTTP 401 Unauthorized y mensaje "Token inválido". Se debe capturar la excepción de token inválido y retornar 401 con el mensaje exacto.~~ **CORREGIDO** - Ahora captura excepciones de token inválido y retorna 401 con el mensaje exacto.

---

## TC009 - Logout sin token ✅ RESUELTO
- **Funcionalidad:** `AuthController.logoutUser`
- **Cumple:** Sí. Si no se envía el header Authorization, retorna HTTP 400 Bad Request y mensaje "Token requerido".
- **Aspectos a corregir:** ~~El caso espera mensaje "Token requerido". Se recomienda ajustar el mensaje para coincidir exactamente.~~ **CORREGIDO** - El mensaje ahora es exactamente "Token requerido".

---

## Resumen de aspectos a corregir
- ~~TC001: Ajustar mensaje a "Registro exitoso" si se requiere exactitud.~~ ✅ **RESUELTO**
- ~~TC002: Retornar HTTP 409 y mensaje "Nombre de usuario ya registrado".~~ ✅ **RESUELTO**
- ~~TC006: Retornar HTTP 404 y mensaje "Usuario no encontrado" para usuario inexistente.~~ ✅ **RESUELTO**
- ~~TC008: Retornar HTTP 401 y mensaje "Token inválido" para token mal formado/inexistente.~~ ✅ **RESUELTO**
- ~~TC009: Ajustar mensaje a "Token requerido".~~ ✅ **RESUELTO**

**Estado:** ✅ **TODAS LAS CORRECCIONES COMPLETADAS** - 5 de 5 casos resueltos. Todos los casos de prueba ahora cumplen exactamente con la funcionalidad esperada.
