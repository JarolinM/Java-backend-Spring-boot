# 📝 Ejercicios Prácticos Completos

## 🎯 Objetivo de Aprendizaje

Pon a prueba todo lo que has aprendido con estos ejercicios de diferentes niveles. Al final encontrarás las respuestas para autoevaluarte.

---

## 📝 PARTE A - Preguntas de Opción Múltiple

### Sección 1: REST y HTTP (Preguntas 1-5)

**Pregunta 1:** ¿Qué significa REST?

- [ ] A) Un lenguaje de programación
- [ ] B) Un estilo de arquitectura para servicios web
- [ ] C) Una base de datos
- [ ] D) Un protocolo de red

**Pregunta 2:** ¿Qué verbo HTTP usas para CREAR un recurso?

- [ ] A) GET
- [ ] B) POST
- [ ] C) PUT
- [ ] D) DELETE

**Pregunta 3:** ¿Qué código de estado devuelve un recurso NO ENCONTRADO?

- [ ] A) 200
- [ ] B) 201
- [ ] C) 400
- [ ] D) 404

**Pregunta 4:** ¿Qué código de estado devuelve un POST exitoso?

- [ ] A) 200 OK
- [ ] B) 201 Created
- [ ] C) 204 No Content
- [ ] D) 400 Bad Request

**Pregunta 5:** ¿Cuál es el formato estándar para intercambiar datos en REST?

- [ ] A) XML
- [ ] B) HTML
- [ ] C) JSON
- [ ] D) CSV

---

### Sección 2: Spring Boot (Preguntas 6-10)

**Pregunta 6:** ¿Qué anotación marca una clase como controlador REST?

- [ ] A) @Controller
- [ ] B) @Component
- [ ] C) @RestController
- [ ] D) @Service

**Pregunta 7:** ¿Qué anotación convierte JSON a objeto Java?

- [ ] A) @PathVariable
- [ ] B) @RequestParam
- [ ] C] @RequestBody
- [ ] D] @ResponseBody

**Pregunta 8:** ¿Qué anotación marca una clase como entidad JPA?

- [ ] A] @Table
- [ ] B] @Entity
- [ ] C] @Id
- [ ] D] @Column

**Pregunta 9:** ¿Qué método de JpaRepository devuelve Optional?

- [ ] A] findAll()
- [ ] B] findById(id)
- [ ] C] save(entity)
- [ ] D] count()

**Pregunta 10:** ¿Cuál es la base de datos en memoria por defecto en Spring Boot?

- [ ] A] MySQL
- [ ] B] PostgreSQL
- [ ] C] H2
- [ ] D] Oracle

---

### Sección 3: El Proyecto (Preguntas 11-15)

**Pregunta 11:** ¿Qué archivo contiene la clase main de Spring Boot?

- [ ] A) Producto.java
- [ ] B) ProductoRepository.java
- [ ] C] ProductoController.java
- [ ] D] AppApplication.java

**Pregunta 12:** ¿Cuál es la URL base de la API según el proyecto?

- [ ] A] /productos
- [ ] B] /api
- [ ] C] /api/productos
- [ ] D] /producto-api

**Pregunta 13:** ¿Qué método HTTP usa el endpoint de crear producto?

- [ ] A] GET
- [ ] B] POST
- [ ] C] PUT
- [ ] D] DELETE

**Pregunta 14:** ¿Qué devuelve `findAll()` del repository?

- [ ] A] Un Producto
- [ ] B] Optional<Producto>
- [ ] C] List<Producto>
- [ ] D] void

**Pregunta 15:** ¿Qué configuración habilita H2 Console?

- [ ] A] spring.h2.enabled=true
- [ ] B] spring.h2.console.enabled=true
- [ ] C] spring.console.enabled=true
- [ ] D] h2.console.enabled=true

---

## 📝 PARTE B - Completar Código

### Ejercicio 1: Añadir @Autowired

Completa el código faltante:

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    _______________  // Falta: ¿Qué anotación?
    private ProductoRepository productoRepository;
    
    @GetMapping
    public List<Producto> lista() {
        return productoRepository.findAll();
    }
}
```

---

### Ejercicio 2: Añadir @PathVariable

Completa el código:

```java
@GetMapping("/________")  // Falta: ¿Qué variable?
public Producto obtener(_______________ Long id) {  // Falta: ¿Qué anotación?
    return productoRepository.findById(id).get();
}
```

---

### Ejercicio 3: Entity Producto

Completa la entidad:

```java
_______________  // Falta: ¿Qué anotación?
@Table(name = "PRODUCTO")
public class Producto {
    
    _______________  // Falta: ¿Qué anotación?
    _______________  // Falta: ¿Qué anotación y estrategia?
    private Long id;
    
    private String nombre;
    private String descripcion;
    private Double precio;
}
```

---

### Ejercicio 4: Repository con Query Method

Añade el Query Method faltante:

```java
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Busca productos por nombre exacto
    _______________  // Falta: ¿Qué método?
    String nombre);
    
    // Busca productos con precio mayor a...
    _______________  // Falta: ¿Qué método?
    Double precio);
}
```

---

### Ejercicio 5: ResponseEntity

Completa el método para devolver 201 Created:

```java
@PostMapping
public _______________<Producto> crear(@RequestBody Producto producto) {
    Producto guardado = productoRepository.save(producto);
    URI ubicacion = URI.create("/api/productos/" + guardado.getId());
    return _______________.created(ubicacion).body(guardado);  // Falta: ¿Qué clase?
}
```

---

## 📝 PARTE C - Ejercicios Prácticos

### Ejercicio 1: Probar con curl

Ejecuta los siguientes comandos y anota los resultados:

```bash
# 1. Listar todos los productos
curl http://localhost:8080/api/productos

# 2. Obtener producto con ID 1
curl http://localhost:8080/api/productos/1

# 3. Crear un nuevo producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Monitor","descripcion":"24 pulgadas","precio":199.99}'

# 4. Verificar que se creó
curl http://localhost:8080/api/productos
```

**¿Cuántos productos había antes? ¿Cuántos hay ahora?**

---

### Ejercicio 2: Crear nuevo campo

1. Añade un campo `stock` (Integer) a la entidad Producto
2. Añade getter y setter
3. Reinicia la aplicación
4. Verifica en H2 Console que la tabla se actualizó
5. Inserta un producto con stock via curl

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Teclado","descripcion":"Mecánico RGB","precio":89.99,"stock":50}'
```

---

### Ejercicio 3: Crear nueva entidad

Crea una nueva entidad `Cliente` con:
- `id` (Long, auto-generado)
- `nombre` (String)
- `email` (String)
- `telefono` (String)

Pasos:
1. Crea `Cliente.java` en el paquete `model`
2. Crea `ClienteRepository.java` en el paquete `repository`
3. Crea `ClienteController.java` en el paquete `controller`
4. Añade datos iniciales en `DataInitializer.java`
5. Prueba los endpoints

---

### Ejercicio 4: Añadir PUT y DELETE

En `ProductoController`, añade:

```java
// PUT - Actualizar producto
@PutMapping("/{id}")
public ResponseEntity<Producto> actualizar(
    @PathVariable Long id,
    @RequestBody Producto producto) {
    // Implementar
}

// DELETE - Eliminar producto
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    // Implementar
}
```

Prueba con curl:
```bash
# PUT
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Pro","descripcion":"32GB","precio":1299.99}'

# DELETE
curl -X DELETE http://localhost:8080/api/productos/1
```

---

### Ejercicio 5: Validación de datos

Añade validación para que:
1. El nombre no pueda estar vacío
2. El precio sea mayor a 0

```java
@PostMapping
public ResponseEntity<Producto> crear(@RequestBody @Valid Producto producto) {
    // Implementar
}
```

En el modelo, usa:
```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@NotBlank
private String nombre;

@Positive
private Double precio;
```

---

## 📝 PARTE D - Desafíos (Opcionales)

### Desafío 1: Añadir campo stock y query

1. Añade `stock` (Integer) a Producto
2. Crea un Query Method `findByStockLessThan(Integer cantidad)`
3. Crea un endpoint `GET /api/productos/sin-stock`
4. Prueba que funcione

---

### Desafío 2: Endpoint de búsqueda por nombre

1. Añade un Query Method `findByNombreContaining(String texto)`
2. Crea endpoint `GET /api/productos/buscar?texto=Lap`
3. Debe devolver productos cuyo nombre contenga el texto

---

## 📋 RESPUESTAS

### PARTE A - Respuestas

| Pregunta | Respuesta | Explicación |
|----------|-----------|-------------|
| 1 | **B** | REST es un estilo de arquitectura |
| 2 | **B** | POST crea recursos |
| 3 | **D** | 404 = Not Found |
| 4 | **B** | 201 = Created para POST exitoso |
| 5 | **C** | JSON es el formato estándar |
| 6 | **C** | @RestController para APIs REST |
| 7 | **C** | @RequestBody convierte JSON a objeto |
| 8 | **B** | @Entity marca tabla JPA |
| 9 | **B** | findById devuelve Optional |
| 10 | **C** | H2 es la BD en memoria por defecto |
| 11 | **D** | AppApplication.java es el main |
| 12 | **C** | La URL base es /api/productos |
| 13 | **B** | Crear usa POST |
| 14 | **C** | findAll devuelve List |
| 15 | **B** | spring.h2.console.enabled=true |

---

### PARTE B - Respuestas

**Ejercicio 1:**
```java
@Autowired
private ProductoRepository productoRepository;
```

**Ejercicio 2:**
```java
@GetMapping("/{id}")
public Producto obtener(@PathVariable Long id) {
```

**Ejercicio 3:**
```java
@Entity
@Table(name = "PRODUCTO")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
```

**Ejercicio 4:**
```java
Producto findByNombre(String nombre);

List<Producto> findByPrecioGreaterThan(Double precio);
```

**Ejercicio 5:**
```java
public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
    Producto guardado = productoRepository.save(producto);
    URI ubicacion = URI.create("/api/productos/" + guardado.getId());
    return ResponseEntity.created(ubicacion).body(guardado);
}
```

---

### PARTE C - Respuestas

**Ejercicio 1:** (Respuesta depende de datos iniciales)

**Ejercicio 2:** Verificar que el campo aparece en H2 Console.

**Ejercicio 3:** Código de参考:

```java
// Cliente.java
@Entity
@Table(name = "CLIENTE")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}

// ClienteRepository.java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

// ClienteController.java
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    
    @GetMapping
    public List<Cliente> lista() {
        return clienteRepository.findAll();
    }
}
```

**Ejercicio 4:**
```java
@PutMapping("/{id}")
public ResponseEntity<Producto> actualizar(
        @PathVariable Long id,
        @RequestBody Producto producto) {
    return productoRepository.findById(id)
            .map(p -> {
                p.setNombre(producto.getNombre());
                p.setDescripcion(producto.getDescripcion());
                p.setPrecio(producto.getPrecio());
                return ResponseEntity.ok(productoRepository.save(p));
            })
            .orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    if (productoRepository.existsById(id)) {
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}
```

**Ejercicio 5:** Añadir dependencia:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### PARTE D - Respuestas

**Desafío 1:**
```java
// En ProductoRepository
List<Producto> findByStockLessThan(Integer cantidad);

// En ProductoController
@GetMapping("/sin-stock")
public List<Producto> sinStock() {
    return productoRepository.findByStockLessThan(1);
}
```

**Desafío 2:**
```java
// En ProductoRepository
List<Producto> findByNombreContaining(String texto);

// En ProductoController
@GetMapping("/buscar")
public List<Producto> buscar(@RequestParam String texto) {
    return productoRepository.findByNombreContaining(texto);
}
```

---

## 🎉 ¡Felicitaciones!

Si completaste todos los ejercicios, has demostrado un entendimiento sólido de Spring Boot REST. Ahora puedes:

- Crear controladores REST completos
- Mapear entidades a tablas de BD
- Realizar operaciones CRUD
- Usar H2 Console
- Probar APIs con curl y Postman
- Depurar errores comunes

**¡Sigue practicando y explorando!**

---

*Vuelve al README.md para ver el orden de estudio recomendado.*
