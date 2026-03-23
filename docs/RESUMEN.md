# 📚 RESUMEN - Cheat Sheet de Spring Boot REST

## 🎯 Objetivo

Esta página contiene TODO lo esencial de Spring Boot en una sola página. Úsala como referencia rápida mientras programas.

---

## 🔥 Las 10+ Anotaciones Más Importantes

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                         ANOTACIONES ESENCIALES                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║  CLASE PRINCIPAL                                                              ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │ @SpringBootApplication           ← main class                         │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║  CONTROLADORES                                                                ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │ @RestController                  ← Controlador REST                  │  ║
║  │ @RequestMapping("/path")         ← URL base                          │  ║
║  │ @GetMapping / @PostMapping       ← Métodos HTTP                      │  ║
║  │ @PutMapping / @DeleteMapping     ←                                    │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║  PARÁMETROS                                                                   ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │ @PathVariable                     ← De URL: /{id}                    │  ║
║  │ @RequestParam                     ← De query: ?id=1                 │  ║
║  │ @RequestBody                      ← JSON del body → objeto          │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║  INYECCIÓN                                                                    ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │ @Autowired                        ← Inyectar dependencia            │  ║
║  │ @Component / @Service / @Repository ← Tipos de beans                │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║  JPA/PERSISTENCIA                                                             ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │ @Entity                            ← Mapeo a tabla                  │  ║
║  │ @Table(name="NOMBRE")               ← Nombre de tabla                │  ║
║  │ @Id                                ← Llave primaria                  │  ║
║  │ @GeneratedValue                     ← ID auto-generado               │  ║
║  │ @Column                            ← Config de columna              │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🌐 Los 3 Endpoints del Proyecto

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                         ENDPOINTS DEL PROYECTO                                 ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │                                                                        │  ║
║  │   GET /api/productos                                                   │  ║
║  │                                                                        │  ║
║  │   → Lista todos los productos                                          │  ║
║  │   → Response: 200 OK + [Producto, Producto, ...]                        │  ║
║  │                                                                        │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                    ↓                                         ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │                                                                        │  ║
║  │   GET /api/productos/{id}                                             │  ║
║  │                                                                        │  ║
║  │   → Obtiene un producto por ID                                        │  ║
║  │   → Response: 200 OK + Producto | 404 Not Found                        │  ║
║  │                                                                        │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                    ↓                                         ║
║  ┌────────────────────────────────────────────────────────────────────────┐  ║
║  │                                                                        │  ║
║  │   POST /api/productos                                                 │  ║
║  │   Body: {"nombre":"...", "descripcion":"...", "precio":...}           │  ║
║  │                                                                        │  ║
║  │   → Crea un nuevo producto                                            │  ║
║  │   → Response: 201 Created + Producto (con ID)                         │  ║
║  │                                                                        │  ║
║  └────────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🖥️ Comandos curl Esenciales

```bash
# ═══════════════════════════════════════════════════════════════════════════
# GET - Listar todos
# ═══════════════════════════════════════════════════════════════════════════
curl http://localhost:8080/api/productos

# ═══════════════════════════════════════════════════════════════════════════
# GET - Obtener uno por ID
# ═══════════════════════════════════════════════════════════════════════════
curl http://localhost:8080/api/productos/1

# ═══════════════════════════════════════════════════════════════════════════
# POST - Crear producto
# ═══════════════════════════════════════════════════════════════════════════
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'

# ═══════════════════════════════════════════════════════════════════════════
# PUT - Actualizar producto
# ═══════════════════════════════════════════════════════════════════════════
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Pro","descripcion":"32GB","precio":1299.99}'

# ═══════════════════════════════════════════════════════════════════════════
# DELETE - Eliminar producto
# ═══════════════════════════════════════════════════════════════════════════
curl -X DELETE http://localhost:8080/api/productos/1
```

---

## 📊 Códigos HTTP Más Comunes

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                         CÓDIGOS HTTP                                          ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║    2xx ✓  ÉXITO                                                               ║
║    ┌─────────────────────────────────────────────────────────────────────┐  ║
║    │ 200  OK              ← Petición exitosa                             │  ║
║    │ 201  Created         ← Recurso creado (POST)                        │  ║
║    │ 204  No Content      ← Éxito sin body (DELETE)                      │  ║
║    └─────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║    4xx ✗  ERROR DEL CLIENTE                                                  ║
║    ┌─────────────────────────────────────────────────────────────────────┐  ║
║    │ 400  Bad Request       ← JSON malformado o datos inválidos          │  ║
║    │ 401  Unauthorized     ← No autenticado                              │  ║
║    │ 403  Forbidden         ← Sin permisos                                │  ║
║    │ 404  Not Found         ← Recurso no existe                           │  ║
║    └─────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
║    5xx ✗  ERROR DEL SERVIDOR                                                 ║
║    ┌─────────────────────────────────────────────────────────────────────┐  ║
║    │ 500  Internal Server Error ← Error en el código                     │  ║
║    └─────────────────────────────────────────────────────────────────────┘  ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🗄️ Queries SQL Básicos

```sql
-- ═══════════════════════════════════════════════════════════════════════════
-- SELECT - Leer datos
-- ═══════════════════════════════════════════════════════════════════════════
SELECT * FROM PRODUCTO;
SELECT * FROM PRODUCTO WHERE ID = 1;
SELECT * FROM PRODUCTO WHERE PRECIO > 100;
SELECT * FROM PRODUCTO ORDER BY PRECIO ASC;

-- ═══════════════════════════════════════════════════════════════════════════
-- INSERT - Crear datos
-- ═══════════════════════════════════════════════════════════════════════════
INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO)
VALUES ('USB', '32GB', 15.99);

-- ═══════════════════════════════════════════════════════════════════════════
-- UPDATE - Actualizar datos
-- ═══════════════════════════════════════════════════════════════════════════
UPDATE PRODUCTO SET PRECIO = 20.00 WHERE ID = 1;

-- ═══════════════════════════════════════════════════════════════════════════
-- DELETE - Eliminar datos
-- ═══════════════════════════════════════════════════════════════════════════
DELETE FROM PRODUCTO WHERE ID = 1;
```

---

## 🔨 Comandos Maven Útiles

```bash
# ═══════════════════════════════════════════════════════════════════════════
# EJECUTAR LA APLICACIÓN
# ═══════════════════════════════════════════════════════════════════════════
mvn spring-boot:run

# ═══════════════════════════════════════════════════════════════════════════
# LIMPIAR Y COMPILAR
# ═══════════════════════════════════════════════════════════════════════════
mvn clean compile

# ═══════════════════════════════════════════════════════════════════════════
# CREAR PAQUETE (JAR)
# ═══════════════════════════════════════════════════════════════════════════
mvn package

# ═══════════════════════════════════════════════════════════════════════════
# EJECUTAR TESTS
# ═══════════════════════════════════════════════════════════════════════════
mvn test

# ═══════════════════════════════════════════════════════════════════════════
# VER DEPENDENCIAS
# ═══════════════════════════════════════════════════════════════════════════
mvn dependency:tree
```

---

## 📐 Mini-Diagrama del Flujo

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         FLUJO COMPLETO                                       │
│                                                                              │
│  CLIENTE                                                                  │
│  ┌─────────┐                                                                │
│  │  curl   │                                                                │
│  │Postman  │                                                                │
│  └────┬────┘                                                                │
│       │ HTTP Request                                                         │
│       ▼                                                                     │
│  ┌────────────┐     ┌────────────────┐     ┌───────────────────┐          │
│  │ Dispatcher │────▶│  Controller   │────▶│   Repository      │          │
│  │  Servlet   │     │  @RestController    │JpaRepository     │          │
│  └────────────┘     └────────────────┘     └─────────┬─────────┘          │
│                                                      │                     │
│                                                      ▼                     │
│                                                ┌───────────────────┐      │
│                                                │   JPA (Hibernate) │      │
│                                                └─────────┬─────────┘      │
│                                                          │                 │
│                                                          ▼                 │
│                                                ┌───────────────────┐      │
│                                                │   H2 Database    │      │
│                                                │   En memoria     │      │
│                                                └───────────────────┘      │
│                                                          │                 │
│  CLIENTE                                                                  │
│  ┌─────────┐◀─────── HTTP Response (JSON)                               │
│  │  JSON   │                                                                │
│  └─────────┘                                                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📝 Estructura del Proyecto

```
producto-api/
│
├── src/main/java/com/ejemplo/productoapi/
│   │
│   ├── AppApplication.java              ← @SpringBootApplication
│   │
│   ├── controller/
│   │   └── ProductoController.java     ← @RestController
│   │                                      @RequestMapping("/api/productos")
│   │
│   ├── model/
│   │   └── Producto.java               ← @Entity
│   │                                      @Id, @GeneratedValue
│   │
│   ├── repository/
│   │   └── ProductoRepository.java     ← @Repository
│   │                                      extends JpaRepository<Producto, Long>
│   │
│   └── config/
│       └── DataInitializer.java        ← @Configuration
│                                          @Bean
│
└── src/main/resources/
    └── application.properties          ← Configuración
```

---

## ⚡ Quick Reference

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              QUICK REFERENCE                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Endpoint          Método    Descripción              Response            │
│  ──────────────────────────────────────────────────────────────────────────│
│  /api/productos    GET       Lista todos              200 + List          │
│  /api/productos/1  GET       Uno por ID               200 + Producto      │
│  /api/productos    POST      Crear nuevo              201 + Producto      │
│  /api/productos/1  PUT       Actualizar               200 + Producto      │
│  /api/productos/1  DELETE    Eliminar                 204 No Content       │
│                                                                              │
│  ──────────────────────────────────────────────────────────────────────────│
│                                                                              │
│  Repository Methods           Descripción                                   │
│  ──────────────────────────────────────────────────────────────────────────│
│  findAll()                   Lista todos                                   │
│  findById(id)                Uno por ID (Optional)                         │
│  save(entity)                Guardar/actualizar                            │
│  deleteById(id)              Eliminar por ID                               │
│  existsById(id)              Verificar si existe                           │
│  count()                     Contar registros                              │
│                                                                              │
│  ──────────────────────────────────────────────────────────────────────────│
│                                                                              │
│  Query Methods               SQL Generado                                   │
│  ──────────────────────────────────────────────────────────────────────────│
│  findByNombre(n)             WHERE NOMBRE = ?                              │
│  findByPrecioGreaterThan(p)   WHERE PRECIO > ?                              │
│  findByNombreContaining(t)   WHERE NOMBRE LIKE '%?%'                       │
│                                                                              │
│  ──────────────────────────────────────────────────────────────────────────│
│                                                                              │
│  Tipos de Dato              Java → SQL                                      │
│  ──────────────────────────────────────────────────────────────────────────│
│  String                   → VARCHAR                                       │
│  Long, Integer            → BIGINT, INTEGER                                │
│  Double                  → DOUBLE                                         │
│  Boolean                 → BOOLEAN                                        │
│  LocalDate               → DATE                                           │
│  LocalDateTime           → TIMESTAMP                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔑 Fórmulas Mágicas

### Fórmula 1: Controller REST mínimo
```java
@RestController
@RequestMapping("/api/recurso")
public class RecursoController {
    @Autowired private RecursoRepository repo;
    
    @GetMapping public List<Recurso> lista() { return repo.findAll(); }
    @GetMapping("/{id}") public Optional<Recurso> obtener(@PathVariable Long id) { 
        return repo.findById(id); 
    }
    @PostMapping public Recurso crear(@RequestBody Recurso r) { return repo.save(r); }
}
```

### Fórmula 2: Entity JPA mínima
```java
@Entity
public class Entidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String campo1;
    private String campo2;
    // constructors, getters, setters
}
```

### Fórmula 3: Repository mínimo
```java
@Repository
public interface MiRepo extends JpaRepository<Entidad, Long> {
    // Ya tiene: findAll, findById, save, deleteById
}
```

---

## 🎯 Errores Comunes - Soluciones Rápidas

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                         ERROR → SOLUCIÓN                                      ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║  404 Not Found         → Verificar @RequestMapping y @RestController          ║
║  400 Bad Request       → JSON malformado, verificar sintaxis                  ║
║  500 Internal Error   → Falta @Autowired → Repository es null               ║
║  Table not found      → spring.jpa.hibernate.ddl-auto=create                 ║
║  Port in use          → Matar proceso o cambiar puerto                       ║
║  NullPointer          → @Autowired faltante                                  ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 💬 Frase Motivacional Final

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                                                                               ║
║   "Cada experto en Spring Boot alguna vez fue un principiante.               ║
║                                                                               ║
║    No te rindas. Cada error que resuelves es una lección.                    ║
║    Cada línea de código te acerca más a tu objetivo.                         ║
║                                                                               ║
║    ¡Tú puedes hacerlo! 💪"                                                  ║
║                                                                               ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║   Próximos pasos:                                                            ║
║   1. Profundiza en validaciones (@Valid, @NotNull, etc.)                    ║
║   2. Aprende paginación y ordenamiento                                       ║
║   3. Explora relaciones entre entidades (@OneToMany, @ManyToOne)            ║
║   4. Implementa autenticación (Spring Security)                             ║
║   5. Despliega tu API a producción                                           ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 📍 Recursos Adicionales

```
├── 📖 Documentación Spring Boot: https://docs.spring.io/spring-boot/docs/
├── 📖 Spring Data JPA: https://docs.spring.io/spring-data/jpa/docs/
├── 📖 Spring Web: https://docs.spring.io/spring-framework/docs/
├── 🛠️  H2 Database: http://www.h2database.com/html/quickstart.html
└── 📦 Postman: https://www.postman.com/downloads/
```

---

**¡Gracias por estudiar con nosotros!** 🎉

*Autor: Jarolin Matos Martinez*

*Este cheat sheet es parte del curso de Spring Boot - Nivel 1: Fundamentos*
