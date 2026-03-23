# Clase 2: CRUD Completo y Gestión de Errores en Spring Boot

---

## Tabla de Contenidos

1. [Bienvenida y Repaso](#bienvenida)
2. [Operaciones PUT y DELETE](#put-delete)
3. [Capa de Servicio (@Service)](#capa-service)
4. [Manejo de Errores](#manejo-errores)
5. [Validación de Datos](#validacion)
6. [Mejores Prácticas](#mejores-practicas)
7. [Conclusiones](#conclusiones)

---

<a name="bienvenida"></a>
## 1. Bienvenida y Repaso

### Objetivos de Aprendizaje

Al finalizar esta clase, serás capaz de:

- Implementar operaciones PUT y DELETE en tu API REST
- Comprender y aplicar la capa de Servicio (@Service)
- Manejar errores de forma elegante con @ControllerAdvice
- Validar datos de entrada con @Valid
- Estructurar tu código siguiendo mejores prácticas

### Repaso Rápido de la Clase Anterior

En la Clase 1 aprendimos:

```
+-----------------------------------------------------------------+
|                    CLASE 1 - LO QUE APRENDIMOS                  |
+-----------------------------------------------------------------+
|                                                                 |
|  * Que es Spring Boot?                                         |
|  * Crear proyecto con Spring Initializr                        |
|  * Estructura por capas (MVC)                                  |
|  * Endpoint GET y POST                                         |
|  * Base de datos H2                                            |
|                                                                 |
|  Endpoints actuales:                                           |
|  +-- GET    /api/productos       -> Listar todos               |
|  +-- GET    /api/productos/{id}  -> Obtener por ID             |
|  +-- POST   /api/productos       -> Crear nuevo                |
|                                                                 |
+-----------------------------------------------------------------+
```

### Estado Actual del Proyecto

```
src/main/java/com/product/app/
+-- controller/
|   +-- ProductoController.java  <- 3 endpoints (GET, POST)
+-- model/
|   +-- Producto.java           <- Entidad
+-- repository/
|   +-- ProductoRepository.java <- JpaRepository
+-- service/                    <- Aun no existe!
    +-- ???
```

---

<a name="put-delete"></a>
## 2. Operaciones PUT y DELETE

### Que es CRUD?

**CRUD** son las cuatro operaciones fundamentales de persistencia:

```
+-----------------------------------------------------------------+
|                         CRUD                                    |
+-----------------------------------------------------------------+
|                                                                 |
|  C -> Create    -> POST    -> Crear un nuevo recurso           |
|  R -> Read      -> GET     -> Leer uno o varios recursos       |
|  U -> Update    -> PUT     -> Actualizar recurso existente     |
|  D -> Delete    -> DELETE  -> Eliminar un recurso               |
|                                                                 |
+-----------------------------------------------------------------+
```

### Endpoint PUT - Actualizar

#### Que es PUT?

El verbo **PUT** reemplaza completamente un recurso existente.

**Características:**
- Es **idempotente** (multiples llamadas = mismo resultado)
- Requiere todos los campos del recurso
- Si el recurso no existe, puede crearlo o retornar error

#### Implementacion en ProductoController

```java
package com.product.app.controller;

import org.springframework.web.bind.annotation.*;
import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ... GET y POST anteriores ...

    // ================================================================
    // NUEVO: PUT - Actualizar producto
    // ================================================================
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado) {
        
        // Buscamos el producto existente
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        // Si existe, actualizamos sus datos
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            
            // Actualizamos todos los campos
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            
            // Guardamos y retornamos
            Producto guardado = productoRepository.save(producto);
            return ResponseEntity.ok(guardado);
        }
        
        // Si no existe, retornamos 404
        return ResponseEntity.notFound().build();
    }
}
```

### Flujo del Endpoint PUT

```
+-----------------------------------------------------------------+
|                    FLUJO: PUT /api/productos/{id}              |
+-----------------------------------------------------------------+
|                                                                 |
|  Cliente                                                        |
|     |                                                           |
|     | PUT /api/productos/1                                      |
|     | Content-Type: application/json                           |
|     | { "nombre": "Nuevo", "precio": 99.99 }                   |
|     v                                                           |
|  +---------------------------------------------------------+   |
|  |  Controller                                            |   |
|  |  1. Recibe id = 1                                      |   |
|  |  2. Recibe producto del body                           |   |
|  |  3. Busca en repository                                |   |
|  +------------------------+-------------------------------+   |
|                          |                                    |
|                          v                                    |
|  +---------------------------------------------------------+   |
|  |  Repository                                             |   |
|  |  findById(1) -> Optional<Producto>                      |   |
|  +------------------------+-------------------------------+   |
|                          |                                    |
|                   +------+------+                            |
|                   v             v                             |
|              Existe         No existe                          |
|                   |             |                             |
|                   v             v                             |
|             Actualizar      404 Not Found                     |
|             y guardar                                                |
|                   |                                                     |
|                   v                                                     |
|             200 OK + producto actualizado                              |
|                                                                 |
+-----------------------------------------------------------------+
```

### Endpoint DELETE - Eliminar

#### Que es DELETE?

El verbo **DELETE** elimina un recurso del servidor.

**Características:**
- Es **idempotente** (multiples llamadas = mismo efecto)
- Retorna tipicamente **204 No Content** en exito
- Retorna **404 Not Found** si no existe

#### Implementacion en ProductoController

```java
    // ================================================================
    // NUEVO: DELETE - Eliminar producto
    // ================================================================
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        
        // Verificamos si el producto existe
        if (productoRepository.existsById(id)) {
            // Si existe, lo eliminamos
            productoRepository.deleteById(id);
            // Retornamos 204 No Content (exito sin cuerpo)
            return ResponseEntity.noContent().build();
        }
        
        // Si no existe, retornamos 404
        return ResponseEntity.notFound().build();
    }
```

### Tabla de Codigos de Estado

```
+-----------------------------------------------------------------+
|              CODIGOS DE ESTADO PARA CRUD                        |
+-----------------------------------------------------------------+
|                                                                 |
|  GET (Leer)                                                     |
|  +-- 200 OK              -> Lectura exitosa                    |
|  +-- 404 Not Found       -> Recurso no existe                  |
|                                                                 |
|  POST (Crear)                                                   |
|  +-- 201 Created         -> Recurso creado                     |
|                                                                 |
|  PUT (Actualizar)                                               |
|  +-- 200 OK              -> Actualizacion exitosa               |
|  +-- 404 Not Found       -> Recurso no existe                  |
|                                                                 |
|  DELETE (Eliminar)                                              |
|  +-- 204 No Content      -> Eliminacion exitosa                 |
|  +-- 404 Not Found       -> Recurso no existe                  |
|                                                                 |
+-----------------------------------------------------------------+
```

### Probar con curl

```bash
# ================================================================
# ACTUALIZAR PRODUCTO (PUT)
# ================================================================

# Actualizar producto con ID 1
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Pro Max","descripcion":"16GB RAM, 512GB SSD","precio":1499.99}'

# Respuesta esperada (200 OK):
# {
#   "id": 1,
#   "nombre": "Laptop Pro Max",
#   "descripcion": "16GB RAM, 512GB SSD",
#   "precio": 1499.99
# }


# ================================================================
# ELIMINAR PRODUCTO (DELETE)
# ================================================================

# Eliminar producto con ID 3
curl -X DELETE http://localhost:8080/api/productos/3

# Respuesta esperada: 204 No Content (sin cuerpo)


# Verificar que se elimino
curl http://localhost:8080/api/productos

# El producto con ID 3 ya no aparecera
```

---

<a name="capa-service"></a>
## 3. Capa de Servicio (@Service)

### Por que usar una Capa de Servicio?

```
+-----------------------------------------------------------------+
|            PROBLEMA: Controller haciendo demasiado               |
+-----------------------------------------------------------------+
|                                                                 |
|  * Sin Service Layer:                                          |
|                                                                 |
|  Controller                                                     |
|  +-- Recibe peticion                                           |
|  +-- Logica de negocio (mucha)                                 |
|  +-- Validaciones                                               |
|  +-- Acceso a BD                                                |
|  +-- Formatear respuesta                                       |
|                                                                 |
|  Problemas:                                                    |
|  +-- Codigo duplicado en otros controllers                     |
|  +-- Dificil de probar                                         |
|  +-- Violacion de responsabilidad unica                        |
|  +-- Dificil de mantener                                       |
|                                                                 |
+-----------------------------------------------------------------+

+-----------------------------------------------------------------+
|            SOLUCION: Separar en capas                          |
+-----------------------------------------------------------------+
|                                                                 |
|  * Con Service Layer:                                          |
|                                                                 |
|  Controller          Service           Repository               |
|  +-- Recibir   ------> Logica   ------> Acceso BD             |
|  |   peticion        de negocio      (JPA)                     |
|  +-- Formatear       +-- Validar                               |
|     respuesta        +-- Calcular                              |
|                      +-- Orquestar                             |
|                                                                 |
|  Beneficios:                                                   |
|  +-- Separacion de responsabilidades                           |
|  +-- Reutilizacion de logica                                  |
|  +-- Facil de probar (mocking)                                 |
|  +-- Codigo mas limpio                                         |
|                                                                 |
+-----------------------------------------------------------------+
```

### Crear ProductoService

Crea el archivo `ProductoService.java` en el paquete `service`:

```java
package com.product.app.service;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service  // <- Anotacion que marca esta clase como servicio
public class ProductoService {

    // Inyeccion del repositorio
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ================================================================
    // METODOS DEL SERVICIO
    // ================================================================

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    return productoRepository.save(producto);
                });
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### Actualizar ProductoController

Refactoriza el controller para usar el servicio:

```java
package com.product.app.controller;

import org.springframework.web.bind.annotation.*;
import com.product.app.model.Producto;
import com.product.app.service.ProductoService;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    // Ahora usamos el Servicio, no el Repositorio directamente
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET /api/productos - Listar todos
    @GetMapping()
    public List<Producto> listaProductos() {
        return productoService.listarTodos();
    }

    // GET /api/productos/{id} - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/productos - Crear nuevo
    @PostMapping()
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    // PUT /api/productos/{id} - Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        return productoService.actualizar(id, producto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/productos/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

### Arquitectura Final

```
+-----------------------------------------------------------------+
|                 ARQUITECTURA DEL PROYECTO                        |
+-----------------------------------------------------------------+
|                                                                 |
|    Cliente (HTTP Request)                                       |
|            |                                                    |
|            v                                                    |
|    +---------------+                                            |
|    |  Controller   | ProductoController.java                   |
|    |               | @RestController                           |
|    |  - Recibe     |                                           |
|    |  - Valida     |                                           |
|    |  - Delega     |                                           |
|    +-------+-------+                                           |
|            | productoService.listarTodos()                       |
|            v                                                    |
|    +---------------+                                            |
|    |   Service      | ProductoService.java                      |
|    |               | @Service                                 |
|    |  - Logica     |                                           |
|    |  - Validar    |                                           |
|    |  - Orquestar  |                                           |
|    +-------+-------+                                           |
|            | productoRepository.findAll()                        |
|            v                                                    |
|    +---------------+                                            |
|    |  Repository    | ProductoRepository.java                   |
|    |  JpaRepository | @Repository                             |
|    +-------+-------+                                           |
|            |                                                    |
|            v                                                    |
|    +---------------+                                            |
|    |  H2 Database  | Base de datos en memoria                  |
|    +---------------+                                            |
|                                                                 |
+-----------------------------------------------------------------+
```

### Beneficios de Usar @Service

```
+-----------------------------------------------------------------+
|                    BENEFICIOS DE @SERVICE                       |
+-----------------------------------------------------------------+
|                                                                 |
|  1. Reutilizacion de Codigo                                    |
|     -> La misma logica en multiples controllers                 |
|                                                                 |
|  2. Facil de Probar                                            |
|     -> Podemos hacer mock del servicio                          |
|                                                                 |
|  3. Mantenimiento                                               |
|     -> Cambios en un lugar unico                               |
|                                                                 |
|  4. Separacion de Responsabilidades                            |
|     -> Controller: recibir y responder                          |
|     -> Service: logica de negocio                              |
|     -> Repository: acceso a datos                              |
|                                                                 |
|  5. Escalabilidad                                               |
|     -> Facil anadir transacciones, caching, etc.               |
|                                                                 |
+-----------------------------------------------------------------+
```

---

<a name="manejo-errores"></a>
## 4. Manejo de Errores

### Por que manejar errores?

```
+-----------------------------------------------------------------+
|              PAGINA DE ERROR POR DEFECTO                        |
+-----------------------------------------------------------------+
|                                                                 |
|  +-------------------------------------------------------+     |
|  |                                                       |     |
|  |           Whitelabel Error Page                       |     |
|  |                                                       |     |
|  |  This application has no explicit mapping for /error,|     |
|  |  so you are seeing this as a fallback.               |     |
|  |                                                       |     |
|  |  There was an unexpected error (type=Not Found,       |     |
|  |  status=404).                                         |     |
|  |                                                       |     |
|  |  Message: No message available                        |     |
|  |                                                       |     |
|  +-------------------------------------------------------+     |
|                                                                 |
|  * Problemas:                                                 |
|  - Mensaje poco util para el cliente                          |
|  - Sin estructura consistente                                 |
|  - No informa al cliente que salio mal                        |
|                                                                 |
+-----------------------------------------------------------------+
```

### Crear GlobalExceptionHandler

Crea una clase para manejar todos los errores:

```java
package com.product.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  // <- Anotacion para manejar errores globalmente
public class GlobalExceptionHandler {

    // ================================================================
    // MANEJAR RECURSO NO ENCONTRADO
    // ================================================================
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(
            ResourceNotFoundException ex) {
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "error");
        respuesta.put("codigo", 404);
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // ================================================================
    // MANEJAR ERRORES DE VALIDACION
    // ================================================================
    
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(
            ValidacionException ex) {
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "error");
        respuesta.put("codigo", 400);
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("errores", ex.getErrores());
        respuesta.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // ================================================================
    // MANEJAR ERRORES GENERICOS
    // ================================================================
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "error");
        respuesta.put("codigo", 500);
        respuesta.put("mensaje", "Error interno del servidor");
        respuesta.put("detalle", ex.getMessage());
        respuesta.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
```

### Crear Excepciones Personalizadas

Crea la carpeta `exception` y los archivos:

**ResourceNotFoundException.java:**
```java
package com.product.app.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
    
    public ResourceNotFoundException(String recurso, Long id) {
        super(String.format("%s con ID %d no encontrado", recurso, id));
    }
}
```

**ValidacionException.java:**
```java
package com.product.app.exception;

import java.util.List;

public class ValidacionException extends RuntimeException {
    
    private final List<String> errores;

    public ValidacionException(String mensaje, List<String> errores) {
        super(mensaje);
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
```

### Respuesta de Error Estructurada

```
+-----------------------------------------------------------------+
|               RESPUESTA DE ERROR ESTRUCTURADA                    |
+-----------------------------------------------------------------+
|                                                                 |
|  HTTP 404 Not Found                                            |
|                                                                 |
|  {                                                             |
|    "estado": "error",                                          |
|    "codigo": 404,                                              |
|    "mensaje": "Producto con ID 999 no encontrado",             |
|    "timestamp": 1710921600000                                  |
|  }                                                             |
|                                                                 |
|  -------------------------------------------------------------- |
|                                                                 |
|  HTTP 400 Bad Request                                          |
|                                                                 |
|  {                                                             |
|    "estado": "error",                                          |
|    "codigo": 400,                                              |
|    "mensaje": "Validacion fallida",                            |
|    "errores": [                                               |
|      "El nombre es requerido",                                 |
|      "El precio debe ser mayor a 0"                           |
|    ],                                                          |
|    "timestamp": 1710921600000                                  |
|  }                                                             |
|                                                                 |
+-----------------------------------------------------------------+
```

---

<a name="validacion"></a>
## 5. Validacion de Datos

### Por que validar?

```
+-----------------------------------------------------------------+
|                   VALIDACION: FRONTEND VS BACKEND               |
+-----------------------------------------------------------------+
|                                                                 |
|  FRONTEND (Navegador)          BACKEND (Spring Boot)            |
|  +--------------------+         +--------------------+           |
|  | Validacion rapida |         | Validacion segura |           |
|  | Mejor UX          |         | Nunca confiar     |           |
|  | Puede saltarse    |  -----> | SIEMPRE validar   |           |
|  | con curl/API      |         |                   |           |
|  +--------------------+         +--------------------+           |
|                                                                 |
|  IMPORTANTE:                                                   |
|  SIEMPRE validar en el backend!                                |
|  El usuario puede saltarse el frontend                          |
|                                                                 |
+-----------------------------------------------------------------+
```

### Agregar Validaciones al Modelo

```java
package com.product.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Producto {

    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================================================================
    // ANOTACIONES DE VALIDACION
    // ================================================================
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripcion no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    // Constructores
    public Producto() {}

    public Producto(String nombre, String descripcion, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
```

### Anotaciones de Validacion

```
+-----------------------------------------------------------------+
|              ANOTACIONES DE VALIDACION (@Valid)                 |
+-----------------------------------------------------------------+
|                                                                 |
|  @NotNull      -> El campo no puede ser null                   |
|  @NotBlank     -> No puede ser null, vacio o solo espacios     |
|  @NotEmpty     -> No puede ser null o vacio (colecciones)      |
|                                                                 |
|  @Size         -> Longitud del String o coleccion              |
|  @Min          -> Valor minimo numerico                        |
|  @Max          -> Valor maximo numerico                        |
|  @Positive     -> Debe ser positivo (> 0)                      |
|  @Negative     -> Debe ser negativo (< 0)                      |
|  @Email        -> Debe ser un email valido                     |
|  @Pattern      -> Debe coincidir con expresion regular         |
|                                                                 |
|  EJEMPLO:                                                      |
|  +----------------------------------------------------------+  |
|  |  @NotBlank                                               |  |
|  |  @Size(min = 3, max = 100)                              |  |
|  |  private String nombre;                                  |  |
|  +----------------------------------------------------------+  |
|                                                                 |
+-----------------------------------------------------------------+
```

### Actualizar Controller con @Valid

```java
package com.product.app.controller;

import org.springframework.web.bind.annotation.*;
import com.product.app.model.Producto;
import com.product.app.service.ProductoService;
import jakarta.validation.Valid;  // <- Importar Valid
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // POST con validacion
    @PostMapping()
    public ResponseEntity<?> crearProducto(
            @Valid @RequestBody Producto producto,
            BindingResult resultado) {
        
        // Si hay errores de validacion, retornarlos
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(
                new ErrorRespuesta("Validacion fallida", errores)
            );
        }
        
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(201).body(creado);
    }
    
    // ... otros metodos ...
}
```

### Crear DTO para Errores

```java
package com.product.app.dto;

import java.util.List;

public class ErrorRespuesta {
    
    private String mensaje;
    private List<String> errores;

    public ErrorRespuesta(String mensaje, List<String> errores) {
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public String getMensaje() { return mensaje; }
    public List<String> getErrores() { return errores; }
}
```

### Ejemplo de Validacion en Accion

```bash
# Intentar crear producto sin nombre (validacion fallida)
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"descripcion":"Solo descripcion","precio":100}'

# Respuesta (400 Bad Request):
{
  "mensaje": "Validacion fallida",
  "errores": ["nombre: El nombre es requerido"]
}

# Intentar crear con precio negativo
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"X","descripcion":"Test","precio":-50}'

# Respuesta (400 Bad Request):
{
  "mensaje": "Validacion fallida",
  "errores": ["nombre: El nombre debe tener entre 3 y 100 caracteres",
              "precio: El precio debe ser mayor a 0"]
}
```

---

<a name="mejores-practicas"></a>
## 6. Mejores Practicas

### Principios SOLID Aplicados

```
+-----------------------------------------------------------------+
|                   PRINCIPIOS SOLID EN SPRING                    |
+-----------------------------------------------------------------+
|                                                                 |
|  S -> Single Responsibility (Responsabilidad Unica)            |
|  |   -> Cada clase hace una cosa                               |
|  |   Controller: recibir peticiones                            |
|  |   Service: logica de negocio                                |
|  |   Repository: acceso a datos                                |
|                                                                 |
|  O -> Open/Closed (Abierto/Cerrado)                             |
|  |   -> Abierto para extension, cerrado para modificacion      |
|                                                                 |
|  L -> Liskov Substitution (Sustitucion de Liskov)              |
|  |   -> Las subclases pueden sustituir a sus padres            |
|                                                                 |
|  I -> Interface Segregation (Segregacion de Interfaz)           |
|  |   -> Muchas interfaces especificas > una general             |
|                                                                 |
|  D -> Dependency Inversion (Inversion de Dependencias)          |
|      -> Depender de abstracciones, no de concreciones           |
|      -> Constructor injection > @Autowired field               |
|                                                                 |
+-----------------------------------------------------------------+
```

### Checklist de Codigo Limpio

```
+-----------------------------------------------------------------+
|                 CHECKLIST ANTES DE COMMIT                       |
+-----------------------------------------------------------------+
|                                                                 |
|  CODING                                                        |
|  +-- [ ] Nombres descriptivos (clases, metodos, variables)      |
|  +-- [ ] Metodos pequenos (< 20 lineas idealmente)              |
|  +-- [ ] Sin codigo comentado innecesario                      |
|  +-- [ ] Consistencia en estilo                               |
|                                                                 |
|  SPRING BOOT                                                   |
|  +-- [ ] @Service para logica de negocio                       |
|  +-- [ ] @Repository para acceso a datos                       |
|  +-- [ ] ResponseEntity para codigos de estado                  |
|  +-- [ ] Validacion con @Valid                                |
|                                                                 |
|  API REST                                                      |
|  +-- [ ] Verbos HTTP correctos (GET, POST, PUT, DELETE)        |
|  +-- [ ] Codigos de estado apropiados                          |
|  +-- [ ] URLs RESTful (sustantivos, no verbos)                 |
|  +-- [ ] Manejo de errores consistente                         |
|                                                                 |
|  GIT                                                           |
|  +-- [ ] Commits atomicos (uno por cambio)                      |
|  +-- [ ] Mensajes descriptivos (feat:, fix:, docs:)           |
|  +-- [ ] Tests antes de commit                                |
|                                                                 |
+-----------------------------------------------------------------+
```

### Errores a Evitar

```
+-----------------------------------------------------------------+
|              ERRORES COMUNES Y COMO EVITARLOS                   |
+-----------------------------------------------------------------+
|                                                                 |
|  * NO HACER:                                                  |
|                                                                 |
|  1. Logica de negocio en el Controller                          |
|     -> Mover a Service                                         |
|                                                                 |
|  2. Concatenar SQL en strings                                  |
|     -> Usar JpaRepository o JPQL                               |
|                                                                 |
|  3. Excepciones genericas                                      |
|     -> Crear excepciones especificas                           |
|                                                                 |
|  4. Anidacion profunda de if/else                              |
|     -> Usar Optional, map, streams                            |
|                                                                 |
|  5. Campos nulos en respuestas JSON                           |
|     -> Usar Optional para valores que pueden no existir        |
|                                                                 |
|  * MEJOR:                                                    |
|                                                                 |
|  1. Separar en Controller -> Service -> Repository             |
|  2. Usar @Query o metodos derivados de JpaRepository           |
|  3. Crear clases de excepcion personalizadas                   |
|  4. Encadenar map() y orElse()                                 |
|  5. Retornar Optional y usar .orElse(ResponseEntity.notFound) |
|                                                                 |
+-----------------------------------------------------------------+
```

---

<a name="conclusiones"></a>
## 7. Conclusiones

### Resumen de lo Aprendido

```
+-----------------------------------------------------------------+
|                    RESUMEN DE LA CLASE 2                       |
+-----------------------------------------------------------------+
|                                                                 |
|  * PUT /api/productos/{id} - Actualizar producto               |
|  * DELETE /api/productos/{id} - Eliminar producto              |
|  * Capa de Servicio (@Service) - Separacion de responsabilidades |
|  * Manejo de errores (@ControllerAdvice) - Respuestas consistentes |
|  * Validacion (@Valid) - Datos limpios                         |
|  * Mejores practicas - Codigo profesional                      |
|                                                                 |
+-----------------------------------------------------------------+
```

### Endpoints Completos

```
+-----------------------------------------------------------------+
|              API DE PRODUCTOS - ENDPOINTS COMPLETOS             |
+-----------------------------------------------------------------+
|                                                                 |
|  CRUD Completo:                                                |
|  +-- GET    /api/productos       -> Listar todos               |
|  +-- GET    /api/productos/{id}  -> Obtener por ID             |
|  +-- POST   /api/productos       -> Crear producto            |
|  +-- PUT    /api/productos/{id}  -> Actualizar producto       |
|  +-- DELETE /api/productos/{id}  -> Eliminar producto         |
|                                                                 |
|  Codigos de Estado:                                            |
|  +-- 200 OK              -> GET, PUT exitoso                   |
|  +-- 201 Created         -> POST exitoso                       |
|  +-- 204 No Content      -> DELETE exitoso                    |
|  +-- 400 Bad Request      -> Validacion fallida               |
|  +-- 404 Not Found        -> Recurso no existe                |
|                                                                 |
+-----------------------------------------------------------------+
```

### Estructura Final del Proyecto

```
src/main/java/com/product/app/
+-- AppApplication.java              # Clase principal
+-- controller/
|   +-- ProductoController.java     # Endpoints REST
+-- service/
|   +-- ProductoService.java        # Logica de negocio
+-- repository/
|   +-- ProductoRepository.java     # Acceso a datos
+-- model/
|   +-- Producto.java               # Entidad con validaciones
+-- dto/
|   +-- ErrorRespuesta.java        # DTO de errores
+-- exception/
    +-- ResourceNotFoundException.java  # Excepcion 404
    +-- ValidacionException.java       # Excepcion 400
```

### Proximos Pasos

```
Nivel 3 - Produccion Ready
+-- Documentacion con Swagger/OpenAPI
+-- Testing con JUnit y Mockito
+-- Seguridad con Spring Security
+-- Paginacion y ordenamiento
+-- DTOs para optimizar respuestas
```

### Recursos Utiles

| Recurso | URL |
|---------|-----|
| Documentacion @Valid | https://jakarta.ee/specifications/validation/ |
| Spring Data JPA | https://docs.spring.io/spring-data/jpa/docs/current/reference/html/ |
| Baeldung - Validation | https://www.baeldung.com/javax-validation |

---

## Autoevaluacion

### Preguntas

1. **Cual es la diferencia entre PUT y PATCH?**
2. **Por que es importante tener una capa de Servicio?**
3. **Que hace @ControllerAdvice?**
4. **Cual es la diferencia entre @NotNull y @NotBlank?**
5. **Por que siempre debemos validar en el backend?**

### Ejercicios Practicos

1. **Implementa** el endpoint PUT en tu proyecto
2. **Implementa** el endpoint DELETE en tu proyecto
3. **Crea** la clase ProductoService
4. **Refactoriza** tu controller para usar el servicio
5. **Anade** validaciones al modelo Producto

---

*Excelente trabajo! Has completado la Clase 2.*

**Continua practicando y nos vemos en la proxima clase.**
