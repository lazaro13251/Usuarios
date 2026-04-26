# Usuarios
# Instrucciones para arrancar el proyecto Usuarios

## 1. Tecnologías y Dependencias Necesarias
El proyecto está construido con **Spring Boot** y requiere las siguientes tecnologías y dependencias principales para funcionar correctamente:
- **Java 17** o superior (definido en el pom.xml).
- **Maven** (incluye el wrapper `mvnw` para facilitar la compilación).
- **Docker y Docker Compose**: El proyecto utiliza `spring-boot-docker-compose` para levantar automáticamente una instancia de **MongoDB** (en el puerto 27017) al iniciar la aplicación.
- **Lombok**: Dependencia utilizada para reducir el código boilerplate (getters, setters, constructores, etc.).
- **Spring Boot Web / Data MongoDB**: Para la creación de la API REST y la persistencia de datos.
- **Spring Security**: Para proteger los endpoints mediante autenticación.
- **Springdoc OpenAPI (Swagger)**: Para documentar y probar los endpoints de la API.

## 2. Cómo Compilar y Arrancar el Proyecto
Puedes compilar y ejecutar el proyecto fácilmente utilizando el wrapper de Maven incluido en el proyecto. 

Para ejecutar las pruebas y compilar el proyecto, abre una terminal en la raíz del proyecto y ejecuta:
```bash
./mvnw clean install
```
*(Si estás en Windows, utiliza `mvnw.cmd clean install`)*

Para **arrancar la aplicación**, ejecuta:
```bash
docker compose up --build
```
Al ejecutar este comando, Spring Boot detectará el archivo `docker-compose.yaml` y automáticamente levantará el contenedor de MongoDB en segundo plano. La aplicación se iniciará por defecto en el puerto **8080**.

## 3. Documentación OpenAPI (Swagger)
La documentación interactiva de la API está disponible en la siguiente URL una vez que la aplicación esté corriendo:
- **URL de Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **URL de la especificación JSON:** `http://localhost:8080/v3/api-docs`

**Importante:** La aplicación está protegida por Spring Security. Para acceder a cualquier recurso de la API o a la documentación Swagger, debes autenticarte utilizando las siguientes credenciales:
- **Usuario:** `admin`
- **Contraseña:** `admin`

*(Nota: Actualmente la aplicación usa un inicio de sesión basado en formulario (Form Login), por lo que al acceder a los endpoints desde un navegador, serás redirigido a `/login` para ingresar estas credenciales).*

---

## 4. Set de Pruebas y Validación de la API

A continuación se presentan las pruebas realizadas para validar los endpoints, indicando el Path, Verbo HTTP, Request y Response.

### Prueba 1: Crear un Usuario
- **Path:** `/api/v1/users`
- **Verbo:** `POST`
- **Request (JSON):**
```json
{
  "email": "nuevo@example.com",
  "name": "Pedro",
  "lastName1": "Paramo",
  "lastName2": "Perez",
  "birthDate": "1990-01-01",
  "phone": "+1234567890",
  "password": "Password123!",
  "AddressRequest": [
    {
      "name": "Home",
      "street": "Main St 123",
      "countryCode": "US"
    }
  ]
}
```
- **Response HTTP:** `201 Created`
- **Response Body (JSON):**
```json
{
  "email": "nuevo@example.com",
  "name": "Pedro Paramo Perez",
  "phone": "+1234567890",
  "taxId": "PAPP900101",
  "created_at": "2026-04-26T23:26:48.105",
  "update_at": "2026-04-26T23:26:48.105",
  "addresses": [
    {
      "id": null,
      "name": "Home",
      "street": "Main St 123",
      "countryCode": "US"
    }
  ]
}
```

### Prueba 2: Obtener todos los Usuarios
- **Path:** `/api/v1/users`
- **Verbo:** `GET`
- **Request:** *(Ningún body requerido)*
- **Response HTTP:** `200 OK`
- **Response Body (JSON):**
```json
[
  {
    "id": "69ee9f38f09ec34a1c58d90b",
    "email": "nuevo@example.com",
    "name": "Pedro Paramo Perez",
    "phone": "+1234567890",
    "taxId": "PAPP900101",
    "created_at": "2026-04-26T23:26:48.105",
    "addresses": [
      {
        "id": null,
        "name": "Home",
        "street": "Main St 123",
        "countryCode": "US"
      }
    ],
    "active": true
  }
]
```

### Prueba 3: Obtener Usuario por ID
- **Path:** `/api/v1/users/{id}`
- **Verbo:** `GET`
- **Request:** *(Sustituir `{id}` por el ID devuelto en la creación)*
- **Response HTTP:** `200 OK`
- **Response Body (JSON):**
```json
{
  "id": "69ee9f38f09ec34a1c58d90b",
  "email": "nuevo@example.com",
  "name": "Pedro",
  "phone": "+1234567890",
  "taxId": "PAPP900101",
  "created_at": "2026-04-26T23:26:48.105",
  "addresses": [
    {
      "id": null,
      "name": "Home",
      "street": "Main St 123",
      "countryCode": "US"
    }
  ],
  "active": true
}
```

### Prueba 4: Actualizar un Usuario
- **Path:** `/api/v1/users/{id}`
- **Verbo:** `PATCH`
- **Request (JSON):**
```json
{
  "email": "testing.api.patched@example.com",
  "name": "Pedro",
  "lastName1": "Paramo",
  "lastName2": "Perez",
  "birthDate": "1990-01-01",
  "phone": "+1987654321"
}
```
- **Response HTTP:** `200 OK`
- **Response Body (JSON):**
```json
{
  "id": "69ee9f38f09ec34a1c58d90b",
  "email": "testing.api.patched@example.com",
  "name": "Pedro Paramo Perez",
  "phone": "+1987654321",
  "taxId": "PAPP900101",
  "created_at": "2026-04-26T23:26:48.105",
  "update_at": "2026-04-26T23:27:01.788",
  "addresses": [
    {
      "id": null,
      "name": "Home",
      "street": "Main St 123",
      "countryCode": "US"
    }
  ]
}
```

### Prueba 5: Eliminar un Usuario
- **Path:** `/api/v1/users/{id}`
- **Verbo:** `DELETE`
- **Request:** *(Ningún body requerido)*
- **Response HTTP:** `204 No Content`
- **Response Body:** *(Vacío)*
