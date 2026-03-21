# 🎮 Controller REST

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, dominarás la creación de controladores REST, las anotaciones de mapping, cómo extraer parámetros de URLs y parsear JSON del body.

---

## 📚 Teoría

### @RestController vs @Controller

Esta es una distinción crucial:

```
@Controller                                    @RestController
───────────────────────────────────────────────────────────────
Retorna VISTAS (HTML/JSP)                        Retorna DATOS (JSON/XML)

    │                                               │
    ▼                                               ▼
┌─────────────┐                           ┌─────────────────┐
│  returning  │                           │  @ResponseBody  │
│  "producto" │                           │  (implícito)     │
└──────┬──────┘                           └────────┬────────┘
       │                                            │
       ▼                                            ▼
┌─────────────┐                           ┌─────────────────┐
│ ViewResolver│                           │ MessageConverter│
│ busca       │                           │ convierte a     │
│ producto.jsp│                           │ JSON automáticamente
└─────────────┘                           └─────────────────┘
```

**En REST API, SIEMPRE usamos `@RestController`.**

### Anotaciones de Mapping

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ANOTACIONES DE MAPPING HTTP                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  @GetMapping      → Responde SOLO a GET    (leer datos)                      │
│  @PostMapping     → Responde SOLO a POST   (crear datos)                     │
│  @PutMapping      → Responde SOLO a PUT    (actualizar todo)                 │
│  @PatchMapping    → Responde SOLO a PATCH  (actualizar parcialmente)       │
│  @DeleteMapping   → Responde SOLO a DELETE (eliminar datos)                  │
│                                                                              │
│  @RequestMapping  → Responde a VARIOS verbos (GET, POST, PUT...)            │
│                     Se puede usar en clase o método                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### @RequestMapping - Prefijos de Ruta

Se usa para definir la **URL base** de todos los endpoints del controlador:

```java
@RestController
@RequestMapping("/api/productos")  // ← URL base para TODOS los métodos
public class ProductoController {
    
    @GetMapping        // → GET /api/productos
    public List<Producto> lista() { ... }
    
    @GetMapping("/{id}")  // → GET /api/productos/{id}
    public Producto obtener(@PathVariable Long id) { ... }
    
    @PostMapping       // → POST /api/productos
    public Producto crear(@RequestBody Producto p) { ... }
}
```

### @GetMapping - Obtener Datos

```java
@GetMapping
public List<Producto> listaProductos() {
    return productoRepository.findAll();
}

@GetMapping("/{id}")
public Producto obtenerProducto(@PathVariable Long id) {
    return productoRepository.findById(id).get();
}
```

### @PostMapping - Crear Recursos

```java
@PostMapping
public Producto crearProducto(@RequestBody Producto producto) {
    return productoRepository.save(producto);
}
```

### @PutMapping y @DeleteMapping

```java
@PutMapping("/{id}")
public Producto actualizarProducto(@PathVariable Long id, 
                                     @RequestBody Producto producto) {
    Producto existente = productoRepository.findById(id).get();
    existente.setNombre(producto.getNombre());
    existente.setPrecio(producto.getPrecio());
    return productoRepository.save(existente);
}

@DeleteMapping("/{id}")
public void eliminarProducto(@PathVariable Long id) {
    productoRepository.deleteById(id);
}
```

### @PathVariable - Extraer de URL

`@PathVariable` extrae valores de la URL (variables de camino):

```
URL: /api/productos/5
                    │
                    ▼
         @PathVariable Long id  →  valor = 5
```

```java
@GetMapping("/{id}")
public Producto obtenerPorId(@PathVariable Long id) {
    //                                     │
    //                        Spring inyecta el valor de {id}
    //                                    │
    return productoRepository.findById(id).orElseThrow();
}

// También puedes especificar el nombre entre comillas:
@GetMapping("/{id}")
public Producto obtenerPorId(@PathVariable("id") Long identificador) {
    return productoRepository.findById(identificador).orElseThrow();
}
```

### @RequestBody - Parsear JSON

`@RequestBody` convierte el JSON del request a un objeto Java:

```
HTTP Request:
───────────────────────────────
POST /api/productos
Content-Type: application/json

{
    "nombre": "Laptop",
    "precio": 999.99
}
        │
        ▼
@RequestBody Producto producto
        │
        ▼
Producto obj = {
    nombre: "Laptop",
    precio: 999.99
}
```

```java
@PostMapping
public Producto crear(@RequestBody Producto producto) {
    // producto ya tiene los datos del JSON
    return productoRepository.save(producto);
}
```

### ResponseEntity<T>

`ResponseEntity` te da control total sobre la respuesta HTTP:

```
ResponseEntity<T>
        │
        ├── .ok(body)              → 200 OK con body
        ├── .ok().build()          → 200 OK sin body
        ├── .created(uri)          → 201 Created
        ├── .noContent()           → 204 No Content
        ├── .notFound()            → 404 Not Found
        ├── .badRequest()          → 400 Bad Request
        └── .status(HttpStatus.X)  → Código específico
```

#### Ejemplos:

```java
// 200 OK con body
return ResponseEntity.ok(producto);

// 201 Created con Location header
return ResponseEntity
    .created(URI.create("/api/productos/" + producto.getId()))
    .body(producto);

// 404 Not Found
return ResponseEntity.notFound().build();

// 400 Bad Request
return ResponseEntity.badRequest().build();

// Código específico
return ResponseEntity.status(HttpStatus.ACCEPTED).body(producto);
```

---

## 💻 Código: ProductoController.java Completo

```java
package com.ejemplo.productoapi.controller;

import com.ejemplo.productoapi.model.Producto;
import com.ejemplo.productoapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// CONTROLADOR REST
// ═══════════════════════════════════════════════════════════════════════════
//
// @RestController = @Controller + @ResponseBody en cada método
// - Recibe peticiones HTTP
// - Procesa datos
// - Devuelve JSON automáticamente
//
// @RequestMapping("/api/productos") = URL base para todos los endpoints
//
// ═══════════════════════════════════════════════════════════════════════════

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // ═══════════════════════════════════════════════════════════════════════
    // INYECCIÓN DE DEPENDENCIA
    // ═══════════════════════════════════════════════════════════════════════
    
    // @Autowired: Spring automáticamente crea e inyecta una instancia
    // del ProductoRepository. Sin esto, repository sería null.
    
    @Autowired
    private ProductoRepository productoRepository;

    // ═══════════════════════════════════════════════════════════════════════
    // ENDPOINT 1: GET /api/productos
    // ═══════════════════════════════════════════════════════════════════════
    //
    // OBJETIVO: Obtener la lista de TODOS los productos
    // MÉTODO HTTP: GET
    // RESPONSE: 200 OK con Lista<Producto> en JSON
    //
    // EJEMPLO:
    //   curl http://localhost:8080/api/productos
    //
    // ═══════════════════════════════════════════════════════════════════════
    
    @GetMapping
    public ResponseEntity<List<Producto>> listaProductos() {
        // 1. Consultar todos los productos en la BD
        List<Producto> productos = productoRepository.findAll();
        
        // 2. Devolver 200 OK con la lista
        return ResponseEntity.ok(productos);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ENDPOINT 2: GET /api/productos/{id}
    // ═══════════════════════════════════════════════════════════════════════
    //
    // OBJETIVO: Obtener UN producto específico por su ID
    // MÉTODO HTTP: GET
    // PATH VARIABLE: {id} → Extrae el ID de la URL
    // RESPONSE: 
    //   - 200 OK + Producto (si existe)
    //   - 404 Not Found (si no existe)
    //
    // EJEMPLO:
    //   curl http://localhost:8080/api/productos/1
    //
    // ═══════════════════════════════════════════════════════════════════════
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(
            @PathVariable Long id) {
        
        // 1. Buscar el producto por ID
        // findById retorna Optional<Producto> (puede estar vacío)
        return productoRepository.findById(id)
                // 2. Si existe, devolver 200 OK con el producto
                .map(ResponseEntity::ok)
                // 3. Si no existe, devolver 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ENDPOINT 3: POST /api/productos
    // ═══════════════════════════════════════════════════════════════════════
    //
    // OBJETIVO: Crear un NUEVO producto
    // MÉTODO HTTP: POST
    // REQUEST BODY: JSON con datos del producto
    // RESPONSE: 
    //   - 201 Created + Producto creado (con ID asignado)
    //   - El header "Location" apunta al nuevo recurso
    //
    // EJEMPLO:
    //   curl -X POST http://localhost:8080/api/productos \
    //     -H "Content-Type: application/json" \
    //     -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
    //
    // ═══════════════════════════════════════════════════════════════════════
    
    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @RequestBody Producto producto) {
        
        // 1. Guardar el producto en la BD
        // Spring genera el ID automáticamente
        Producto guardado = productoRepository.save(producto);
        
        // 2. Crear la URI del nuevo recurso
        // /api/productos/{id}
        URI ubicacion = URI.create(
            "/api/productos/" + guardado.getId()
        );
        
        // 3. Devolver 201 Created con:
        //    - Body: el producto guardado (con ID)
        //    - Location header: URI del nuevo recurso
        return ResponseEntity.created(ubicacion).body(guardado);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ENDPOINT 4: PUT /api/productos/{id}
    // ═══════════════════════════════════════════════════════════════════════
    //
    // OBJETIVO: Actualizar TODOS los campos de un producto existente
    // MÉTODO HTTP: PUT
    // PATH VARIABLE: {id} → ID del producto a actualizar
    // REQUEST BODY: JSON con TODOS los datos (incluye ID)
    // RESPONSE:
    //   - 200 OK + Producto actualizado
    //   - 404 Not Found (si no existe el ID)
    //
    // ═══════════════════════════════════════════════════════════════════════
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        
        // 1. Buscar si el producto existe
        return productoRepository.findById(id)
                .map(productoExistente -> {
                    // 2. SI EXISTE: Actualizar todos los campos
                    productoExistente.setNombre(producto.getNombre());
                    productoExistente.setDescripcion(producto.getDescripcion());
                    productoExistente.setPrecio(producto.getPrecio());
                    
                    // 3. Guardar cambios
                    Producto actualizado = productoRepository.save(productoExistente);
                    
                    // 4. Devolver 200 OK
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
                // 5. SI NO EXISTE: Devolver 404
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ENDPOINT 5: DELETE /api/productos/{id}
    // ═══════════════════════════════════════════════════════════════════════
    //
    // OBJETIVO: Eliminar un producto por ID
    // MÉTODO HTTP: DELETE
    // PATH VARIABLE: {id} → ID del producto a eliminar
    // RESPONSE:
    //   - 204 No Content (si se eliminó)
    //   - 404 Not Found (si no existía)
    //
    // ═══════════════════════════════════════════════════════════════════════
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {
        
        // 1. Verificar si el producto existe
        if (productoRepository.existsById(id)) {
            // 2. SI EXISTE: Eliminar y devolver 204
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            // 3. SI NO EXISTE: Devolver 404
            return ResponseEntity.notFound().build();
        }
    }
}
```

---

## 📊 Diagrama: Los 5 Endpoints del Controller

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      PRODUCTOCONTROLLER                                       │
│                   @RequestMapping("/api/productos")                           │
│                                                                              │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                                                                      │    │
│  │   ┌───────────────┐                                                 │    │
│  │   │  @GetMapping  │                                                 │    │
│  │   │               │                                                 │    │
│  │   │ GET /api/productos                                            │    │
│  │   │               │                                                 │    │
│  │   │ → listaProductos()                                            │    │
│  │   │ → findAll()                                                    │    │
│  │   │ → returns: List<Producto>                                      │    │
│  │   │ → HTTP: 200 OK                                                  │    │
│  │   └───────────────┘                                                 │    │
│  │                                                                      │    │
│  │   ┌───────────────┐                                                 │    │
│  │   │  @GetMapping  │                                                 │    │
│  │   │ /{id}         │                                                 │    │
│  │   │               │                                                 │    │
│  │   │ GET /api/productos/{id}                                         │    │
│  │   │               │                                                 │    │
│  │   │ → obtenerPorId(id)                                              │    │
│  │   │ → findById(id)                                                  │    │
│  │   │ → returns: Optional<Producto>                                   │    │
│  │   │ → HTTP: 200 OK / 404 Not Found                                  │    │
│  │   └───────────────┘                                                 │    │
│  │                                                                      │    │
│  │   ┌───────────────┐                                                 │    │
│  │   │  @PostMapping │                                                 │    │
│  │   │               │                                                 │    │
│  │   │ POST /api/productos                                             │    │
│  │   │               │                                                 │    │
│  │   │ @RequestBody: Producto                                          │    │
│  │   │ → crearProducto(producto)                                       │    │
│  │   │ → save(producto)                                                 │    │
│  │   │ → returns: Producto (con ID)                                    │    │
│  │   │ → HTTP: 201 Created                                             │    │
│  │   └───────────────┘                                                 │    │
│  │                                                                      │    │
│  │   ┌───────────────┐                                                 │    │
│  │   │  @PutMapping  │                                                 │    │
│  │   │ /{id}         │                                                 │    │
│  │   │               │                                                 │    │
│  │   │ PUT /api/productos/{id}                                         │    │
│  │   │               │                                                 │    │
│  │   │ @RequestBody: Producto (completo)                              │    │
│  │   │ → actualizarProducto(id, producto)                             │    │
│  │   │ → HTTP: 200 OK / 404 Not Found                                  │    │
│  │   └───────────────┘                                                 │    │
│  │                                                                      │    │
│  │   ┌───────────────┐                                                 │    │
│  │   │@DeleteMapping │                                                 │    │
│  │   │ /{id}         │                                                 │    │
│  │   │               │                                                 │    │
│  │   │ DELETE /api/productos/{id}                                     │    │
│  │   │               │                                                 │    │
│  │   │ → eliminarProducto(id)                                          │    │
│  │   │ → deleteById(id)                                                │    │
│  │   │ → HTTP: 204 No Content / 404 Not Found                          │    │
│  │   └───────────────┘                                                 │    │
│  │                                                                      │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Tabla de Anotaciones de Mapping

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                    ANOTACIONES DE MAPPING                                      │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌───────────────────┬──────────────────────────────────────────────────┐   │
│  │ ANOTACIÓN          │ DESCRIPCIÓN                                      │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @RestController   │ Marca la clase como controlador REST             │   │
│  │                   │ (combina @Controller + @ResponseBody)             │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @RequestMapping   │ Define URL base para toda la clase               │   │
│  │ (clase)           │ Ejemplo: "/api/productos"                         │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @GetMapping       │ Responde SOLO a peticiones GET                    │   │
│  │ @PostMapping      │ Responde SOLO a peticiones POST                   │   │
│  │ @PutMapping       │ Responde SOLO a peticiones PUT                   │   │
│  │ @PatchMapping     │ Responde SOLO a peticiones PATCH                 │   │
│  │ @DeleteMapping    │ Responde SOLO a peticiones DELETE                │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @PathVariable     │ Extrae valor de la URL                           │   │
│  │                   │ Ejemplo: /productos/{id} → id = 5                │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @RequestParam     │ Extrae query parameters                          │   │
│  │                   │ Ejemplo: /buscar?nombre=laptop → nombre="laptop" │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @RequestBody      │ Convierte JSON del body a objeto Java            │   │
│  │                   │                                                  │   │
│  ├───────────────────┼──────────────────────────────────────────────────┤   │
│  │                   │                                                  │   │
│  │ @ResponseStatus    │ Define código HTTP de respuesta                  │   │
│  │                   │                                                  │   │
│  └───────────────────┴──────────────────────────────────────────────────┘   │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **404 en todos los endpoints** | Falta `@RestController` o `@RequestMapping` | Añadir ambas anotaciones |
| 2 | **NullPointerException** | Falta `@Autowired` en el repository | Añadir `@Autowired` |
| 3 | **400 Bad Request** | JSON malformado o tipos incorrectos | Validar el JSON |
| 4 | **415 Unsupported Media Type** | Falta header `Content-Type: application/json` | Añadir el header |
| 5 | **500 al acceder a {id}** | El ID no existe en BD | Manejar Optional correctamente |
| 6 | **Confundir @PathVariable con @RequestParam** | URL vs query string | @PathVariable para `/{id}`, @RequestParam para `?id=5` |

---

## 💡 Tips

### 1. Diferencia entre @PathVariable y @RequestParam

```
@PathVariable    → Variable en la ruta
/api/productos/5        → {id} = 5

@RequestParam   → Query parameter
/api/buscar?nombre=laptop  → nombre = "laptop"
```

### 2. Manejo correcto de Optional

```java
// ❌ INCORRECTO - Puede lanzar NullPointerException
@GetMapping("/{id}")
public Producto obtener(@PathVariable Long id) {
    return productoRepository.findById(id).get(); // ¡get() sin verificar!
}

// ✅ CORRECTO - Maneja ambos casos
@GetMapping("/{id}")
public ResponseEntity<Producto> obtener(@PathVariable Long id) {
    return productoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

### 3. Status codes según la acción

```
GET    → 200 OK (siempre)
POST   → 201 Created (recurso nuevo)
PUT    → 200 OK (actualización) o 404
DELETE → 204 No Content (éxito sin body)
```

### 4. Usa URI para Location header

```java
// ❌ Feo
@PostMapping
public ResponseEntity<Producto> crear(@RequestBody Producto p) {
    return ResponseEntity
        .status(201)
        .header("Location", "/api/productos/" + p.getId())
        .body(p);
}

// ✅ Mejor
@PostMapping
public ResponseEntity<Producto> crear(@RequestBody Producto p) {
    Producto guardado = repo.save(p);
    URI location = URI.create("/api/productos/" + guardado.getId());
    return ResponseEntity.created(location).body(guardado);
}
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué anotación convierte JSON a objeto Java?

- [ ] A) @PathVariable
- [ ] B) @RequestParam
- [ ] C) @RequestBody
- [ ] D) @ResponseBody

### Pregunta 2:
¿Qué anotación extrae valores de la URL?

- [ ] A) @RequestBody
- [ ] B) @PathVariable
- [ ] C) @GetMapping
- [ ] D) @Autowired

### Pregunta 3:
¿Qué código de estado devuelve POST exitoso?

- [ ] A) 200
- [ ] B) 201
- [ ] C) 204
- [ ] D) 400

### Pregunta 4:
¿Cuál es la diferencia entre @RestController y @Controller?

- [ ] A) Ninguna
- [ ] B) @RestController retorna JSON automáticamente
- [ ] C) @Controller es más rápido
- [ ] D) @RestController solo acepta POST

### Pregunta 5:
¿Qué devuelve `findById()`?

- [ ] A) Producto directamente
- [ ] B) List<Producto>
- [ ] C) Optional<Producto>
- [ ] D) void

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Añade PUT y DELETE

En el controller actual, añade:
1. Un endpoint PUT para actualizar productos
2. Un endpoint DELETE para eliminar productos

### Ejercicio 2: Endpoint con query parameter

Añade un endpoint para buscar productos:
- URL: `/api/productos/buscar?nombre=Laptop`
- Usa `@RequestParam`

### Ejercicio 3: Valida el request

Modifica el POST para:
1. Verificar que el nombre no esté vacío
2. Verificar que el precio sea mayor a 0
3. Devolver 400 Bad Request si falla la validación

### Ejercicio 4: Documenta tu API

Para cada endpoint de tu controller, escribe:
1. URL completa
2. Método HTTP
3. Request body (si aplica)
4. Response codes posibles
5. Ejemplo de curl

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
