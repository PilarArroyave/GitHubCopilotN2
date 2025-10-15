# Auth Service - Servicio de Autenticación

Aplicación Java con Spring Boot que proporciona un servicio de autenticación simple con manejo de datos en memoria y documentación interactiva con Swagger/OpenAPI.

## Tecnologías Utilizadas

- **Java 17** (LTS)
- **Spring Boot 3.2.12**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **H2 Database** (base de datos en memoria)
- **Maven** (gestión de dependencias)
- **Swagger/OpenAPI** (documentación interactiva)

## Características

✅ Registro de usuarios  
✅ Autenticación (login)  
✅ Cierre de sesión (logout)  
✅ Seguridad basada en JWT  
✅ Base de datos en memoria  
✅ Validación de datos  
✅ Documentación interactiva con Swagger  

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com.sura.auth/
│   │       ├── controller/       # Controladores REST
│   │       ├── service/          # Lógica de negocio
│   │       ├── dto/              # Objetos de transferencia
│   │       ├── model/            # Entidades del dominio
│   │       ├── repository/       # Acceso a datos
│   │       └── config/           # Configuraciones de seguridad y Swagger
│   └── resources/
│       └── application.properties
```

## Endpoints Disponibles

### 1. Registro de Usuario
- **URL:** `POST /api/auth/register`
- **Descripción:** Registra un nuevo usuario en el sistema
- **Body:**
```json
{
    "username": "usuario123",
    "email": "usuario@email.com",
    "password": "password123"
}
```
- **Respuesta exitosa:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "usuario123",
    "message": "Usuario registrado exitosamente"
}
```

### 2. Login
- **URL:** `POST /api/auth/login`
- **Descripción:** Autentica un usuario existente
- **Body:**
```json
{
    "username": "usuario123",
    "password": "password123"
}
```
- **Respuesta exitosa:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "usuario123",
    "message": "Login exitoso"
}
```

### 3. Logout
- **URL:** `POST /api/auth/logout`
- **Descripción:** Cierra la sesión del usuario
- **Headers:** `Authorization: Bearer <token>`
- **Respuesta exitosa:**
```json
{
    "message": "Logout exitoso"
}
```

### 4. Health Check
- **URL:** `GET /api/auth/health`
- **Descripción:** Verifica el estado del servicio
- **Respuesta:**
```json
{
    "status": "UP",
    "service": "auth-service"
}
```

## Documentación Interactiva

La API cuenta con documentación interactiva generada automáticamente por Swagger/OpenAPI. Puedes acceder a la documentación y probar los endpoints desde el navegador en:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior

### Pasos para ejecutar

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd SURA-GHC-2
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

4. **La aplicación estará disponible en:**
- API: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: (vacía)
- Swagger UI: `http://localhost:8080/swagger-ui.html` o `http://localhost:8080/swagger-ui/index.html`

## Configuración

### Variables de Entorno Importantes

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `server.port` | Puerto del servidor | 8080 |
| `jwt.secret` | Clave secreta para JWT | (configurada) |
| `jwt.expiration` | Tiempo de expiración del token (ms) | 86400000 (24 horas) |

### Base de Datos

La aplicación utiliza H2 como base de datos en memoria. Los datos se pierden al reiniciar la aplicación.

## Seguridad

- **Encriptación de contraseñas:** BCrypt
- **Autenticación:** JWT (JSON Web Tokens)
- **Validaciones:** Bean Validation con anotaciones
- **CORS:** Configurado para permitir todos los orígenes (desarrollo)

## Validaciones

### Registro de Usuario
- Username: 3-50 caracteres, único
- Email: formato válido, único
- Password: mínimo 6 caracteres

### Login
- Username y password son obligatorios

## Logs

La aplicación registra eventos importantes:
- Intentos de registro
- Intentos de login
- Errores de autenticación
- Operaciones de logout

## Testing

Para probar los endpoints, puedes usar herramientas como:
- **Postman**
- **curl**
- **Thunder Client** (VS Code)

### Ejemplo con curl:

```bash
# Registro
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@email.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password123"}'

# Logout (reemplazar <TOKEN> con el token obtenido)
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <TOKEN>"
```

## Arquitectura

La aplicación sigue los patrones de diseño establecidos:

- **Controller:** Maneja las peticiones HTTP
- **Service:** Contiene la lógica de negocio
- **Repository:** Acceso a datos
- **DTO:** Transferencia de datos
- **Model:** Entidades del dominio
- **Config:** Seguridad y Swagger

## Notas de Desarrollo

- La aplicación está configurada para desarrollo local
- Los datos se almacenan en memoria (H2)
- No incluye tests unitarios (según requerimientos)
- Configuración básica de seguridad implementada
- Documentación interactiva disponible

## Versión

**1.0.0** - Versión inicial con funcionalidades básicas de autenticación y documentación Swagger
