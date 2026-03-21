# 🔧 Errores Comunes y Troubleshooting

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, podrás diagnosticar y resolver los 10 errores más comunes en Spring Boot, entendiendo el proceso de debugging paso a paso.

---

## 📚 Los 10 Errores Más Comunes

### Tabla de Errores y Soluciones

| # | Error | Causa Común | Solución |
|---|-------|-------------|----------|
| 1 | **404 Not Found** | Ruta mal escrita o clase no escaneada | Verificar @RequestMapping y package |
| 2 | **400 Bad Request** | JSON malformado o tipos incorrectos | Validar sintaxis JSON |
| 3 | **500 Internal Server Error** | NullPointerException, error en código | Revisar stack trace |
| 4 | **Whitelabel Error Page** | Exception no manejada | Añadir @ExceptionHandler |
| 5 | **Failed to configure DataSource** | URL de BD incorrecta | Revisar application.properties |
| 6 | **Table "PRODUCTO" not found** | JPA no creó la tabla | Configurar ddl-auto=create |
| 7 | **Port 8080 already in use** | Puerto ocupado por otro proceso | Matar proceso o cambiar puerto |
| 8 | **@PathVariable mismatch** | Nombre de parámetro diferente | Usar @PathVariable("id") |
| 9 | **No converter found for class** | Falta getter/setter o dependencia | Añadir getters/setters |
| 10 | **H2 Console login failed** | JDBC URL incorrecta | Usar jdbc:h2:mem:testdb |

---

## 📊 Error 1: 404 Not Found

### Causa
La URL no coincide con ningún endpoint definido.

### Diagnóstico
```
URL accedida: http://localhost:8080/api/productoss
@Controller:  @RequestMapping("/api/productos")
                                                    ↑
                                         ¿Ves el error? (productoss vs productos)
```

### Solución

**1. Verificar el @RequestMapping del Controller:**
```java
@RestController
@RequestMapping("/api/productos")  // ← Verificar que coincida
public class ProductoController { }
```

**2. Verificar que el package está siendo escaneado:**
```java
// Spring Boot escanea paquetes desde donde está la clase main
@SpringBootApplication
public class AppApplication { }
// Spring escanea com.ejemplo.productoapi.*
// Si tu controller está en otro paquete, no lo encontrará
```

**3. Verificar que el controller está bien anotado:**
```java
@RestController  // ← ¿Falta @RestController?
@RequestMapping("/api/productos")
public class ProductoController { }
```

### Checklist para 404:
```
□ ¿La URL está escrita correctamente?
□ ¿@RestController está presente?
□ ¿@RequestMapping coincide?
□ ¿El package está bajo el main package?
□ ¿La aplicación está corriendo?
```

---

## 📊 Error 2: 400 Bad Request

### Causa
El JSON enviado tiene errores de sintaxis o tipos incompatibles.

### Ejemplo de JSON incorrecto:
```json
{
    "nombre": "Laptop",       ← Comillas incorrectas
    precio: 999.99            ← Falta comillas en key
    "descripcion": "16GB",   ← Falta coma
}
```

### JSON correcto:
```json
{
    "nombre": "Laptop",
    "precio": 999.99,
    "descripcion": "16GB"
}
```

### Solución con curl:
```bash
# Usar comillas simples para el JSON en bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop","precio":999.99,"descripcion":"16GB"}'
```

### Checklist para 400:
```
□ ¿El JSON tiene sintaxis correcta?
□ ¿Las keys tienen comillas dobles?
□ ¿Los valores String tienen comillas dobles?
□ ¿Los valores Number NO tienen comillas?
□ ¿No hay comas al final del último elemento?
□ ¿El Content-Type header es application/json?
```

---

## 📊 Error 3: 500 Internal Server Error

### Causa
Error en el código Java (NullPointerException, etc.)

### Ejemplo de stack trace:
```
java.lang.NullPointerException: Cannot invoke method because "productoRepository" is null
    at com.ejemplo.productoapi.controller.ProductoController.listaProductos(ProductoController.java:25)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
```

### Solución - NullPointerException:
```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    // ✗ INCORRECTO - repository es null
    private ProductoRepository productoRepository;
    
    // ✓ CORRECTO - Spring inyecta el bean
    @Autowired
    private ProductoRepository productoRepository;
}
```

### Checklist para 500:
```
□ ¿Hay un @Autowired faltante?
□ ¿El repository está correctamente definido?
□ ¿Los campos de Producto tienen tipos correctos?
□ ¿Se está intentando acceder a un campo null?
□ Revisar el stack trace para la línea exacta del error
```

---

## 📊 Error 4: Whitelabel Error Page

### Causa
Spring Boot muestra una página de error genérica porque hay una excepción no manejada.

### Ejemplo:
```
Whitelabel Error Page
This application has no explicit mapping for /error, 
so you are seeing this as a fallback.
```

### Solución - Añadir ExceptionHandler:
```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Manejar recurso no encontrado
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProductNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

// Clase de error
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    
    public ErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    // getters...
}
```

### Mejor solución - Global Exception Handler:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ProductNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(404).body(response);
    }
}
```

---

## 📊 Error 5: Failed to configure DataSource

### Causa
La URL de la base de datos es incorrecta o H2 no está configurado.

### Solución - Verificar application.properties:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

### Verificar que H2 está en pom.xml:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 📊 Error 6: Table "PRODUCTO" not found

### Causa
JPA/Hibernate no creó las tablas porque `ddl-auto` está en `none`.

### Solución - application.properties:
```properties
# Opción 1: Crear tablas automáticamente (para desarrollo)
spring.jpa.hibernate.ddl-auto=create

# Opción 2: Crear y drop al cerrar (para testing)
spring.jpa.hibernate.ddl-auto=create-drop

# Opción 3: Solo actualizar si hay cambios (para producción-like)
spring.jpa.hibernate.ddl-auto=update
```

### Verificar en los logs:
```
Hibernate: 
    drop table if exists producto cascade
Hibernate: 
    create table producto (
        id bigint generated by default as identity,
        descripcion varchar(255),
        nombre varchar(255),
        precio double,
        primary key (id)
    )
```

---

## 📊 Error 7: Port 8080 already in use

### Causa
Otro proceso está usando el puerto 8080.

### Solución 1 - Encontrar y matar el proceso:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Solución 2 - Cambiar el puerto:
```properties
server.port=8081
```

```bash
# O desde línea de comandos
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

---

## 📊 Error 8: @PathVariable mismatch

### Causa
El nombre del parámetro en la URL no coincide con el del método.

### Problema:
```java
@GetMapping("/{identificador}")  // URL usa "identificador"
public Producto obtener(@PathVariable Long id) {  // Método espera "id"
    // Error: No se puede resolver el valor
}
```

### Solución:
```java
// Opción 1: Especificar el nombre
@GetMapping("/{id}")
public Producto obtener(@PathVariable("id") Long id) { }

// Opción 2: Usar el mismo nombre
@GetMapping("/{id}")
public Producto obtener(@PathVariable Long id) { }
```

---

## 📊 Error 9: No converter found for class

### Causa
Falta un getter/setter o una dependencia de Jackson.

### Problema:
```java
@Entity
public class Producto {
    private Long id;
    private String nombre;
    
    // ✗ Faltan getters y setters - Jackson no puede serializar
}
```

### Solución:
```java
@Entity
public class Producto {
    private Long id;
    private String nombre;
    
    // ✓ Jackson necesita getters para leer (serialización)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
```

### Verificar dependencia Jackson:
```xml
<!-- Ya viene incluido con spring-boot-starter-web -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

---

## 📊 Error 10: H2 Console login failed

### Causa
La JDBC URL en H2 Console no coincide con application.properties.

### Pasos para solucionar:

**1. Verificar la URL en application.properties:**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

**2. En H2 Console, usar EXACTAMENTE la misma URL:**
```
JDBC URL: jdbc:h2:mem:testdb
```

**3. Verificar credenciales:**
```
User Name: sa
Password: (vacío, sin espacios)
```

---

## 📊 Flujo de Debug: Error → Stack Trace → Solución

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         FLUJO DE DEBUGGING                                    │
│                                                                              │
│  1. ERROR DETECTADO                                                          │
│     └─→ El navegador/app muestra algo inesperado                              │
│         - Página en blanco                                                    │
│         - Código de error (500, 404, 400)                                   │
│         - Whitelabel Error Page                                              │
│                                                                              │
│  2. REVISAR LA CONSOLA/TERMINAL                                              │
│     └─→ Buscar el STACK TRACE completo                                        │
│         - Empieza con "Caused by:"                                           │
│         - La primera línea es la causa principal                            │
│         - Indica la clase y línea exactas                                   │
│                                                                              │
│  3. IDENTIFICAR LA CAUSA                                                     │
│     └─→ Leer el mensaje de error                                              │
│         - NullPointerException → Algo es null                                │
│         - IllegalArgumentException → Parámetro inválido                      │
│         - JSON parse error → Problema en JSON                                │
│                                                                              │
│  4. APLICAR LA SOLUCIÓN                                                      │
│     └─→ Buscar en esta guía                                                  │
│         - ¿Falta @Autowired? → Añadirlo                                     │
│         - ¿JSON malformado? → Corregir sintaxis                             │
│         - ¿Tabla no existe? → Configurar ddl-auto                           │
│                                                                              │
│  5. VERIFICAR                                                                │
│     └─→ Probar de nuevo el endpoint                                          │
│         - ¿Funciona? → Listo!                                                │
│         - ¿Nuevo error? → Volver al paso 2                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 💡 Tips de Debugging

### 1. Habilitar logs SQL para ver queries:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 2. Añadir logging en el código:
```java
@RestController
public class ProductoController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
    
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado: {}", id);
                    return new RuntimeException("Producto no existe");
                });
    }
}
```

### 3. Usar curl -v para ver headers:
```bash
curl -v http://localhost:8080/api/productos/999
```

### 4. Probar en Postman con format JSON:
Postman formatea automáticamente el JSON, facilitando ver errores.

### 5. Reiniciar la aplicación a veces ayuda:
```bash
# Detener (Ctrl+C) y reiniciar
mvn spring-boot:run
```

---

## ❓ Preguntas de Troubleshooting

### Pregunta 1:
Tu endpoint devuelve 404, ¿qué verías primero?

- [ ] A) El código del controller
- [ ] B) Los logs de la aplicación
- [ ] C) El @RequestMapping
- [ ] D) El pom.xml

### Pregunta 2:
¿Cuál es la causa más común de NullPointerException en Spring?

- [ ] A) Falta de memoria
- [ ] B) Falta @Autowired
- [ ] C) Puerto incorrecto
- [ ] D) JSON malformado

### Pregunta 3:
¿Qué configuración evita que JPA cree las tablas?

- [ ] A) ddl-auto=create
- [ ] B) ddl-auto=none
- [ ] C) ddl-auto=update
- [ ] D) ddl-auto=validate

### Pregunta 4:
¿Cómo manejas una excepción en Spring Boot REST?

- [ ] A) No se puede
- [ ] B) Con @ExceptionHandler
- [ ] C) Con try-catch obligatorio
- [ ] D) Con @ErrorHandler

### Pregunta 5:
¿Qué necesitas para que Jackson convierta JSON a objeto?

- [ ] A) @JsonProperty
- [ ] B) Getters y setters
- [ ] C) Constructor con parámetros
- [ ] D) @Column

---

## 📋 Ejercicios de Troubleshooting

### Ejercicio 1: Diagnostica el error

Este código devuelve 500. ¿Cuál es el problema?

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private ProductoRepository productoRepository;
    
    @GetMapping
    public List<Producto> lista() {
        return productoRepository.findAll();
    }
}
```

### Ejercicio 2: Encuentra 3 errores

Este código tiene 3 errores. Encuéntralos:

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping("/{id"}
    public Producto obtener(@PathVariable Long id) {
        return productoRepository.findById(id).get();
    }
    
    @PostMapping
    public Producto crear(Producto p {
        return productoRepository.save(p);
    }
}
```

### Ejercicio 3: Resuelve paso a paso

La aplicación no inicia. La consola muestra:
```
Error creating bean with name 'productoController'
```

¿Qué harías paso a paso?

### Ejercicio 4: Debug challenge

La API funciona con curl pero Postman devuelve 400. ¿Por qué?

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
