# 📋 Glosario de Anotaciones Spring Boot

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, tendrás una referencia completa de todas las anotaciones Spring Boot importantes, saber dónde se usan y cómo se combinan.

---

## 📚 Tabla Completa de Anotaciones

### Anotaciones de Aplicación Principal

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@SpringBootApplication` | Clase Main | Marca la clase principal de Spring Boot. Combina `@Configuration`, `@EnableAutoConfiguration`, y `@ComponentScan` | `@SpringBootApplication public class App { }` |

---

### Anotaciones de Controlador REST

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@RestController` | Clase | Marca la clase como controlador REST. Combina `@Controller` y `@ResponseBody` en todos los métodos | `@RestController` |
| `@Controller` | Clase | Controlador MVC tradicional (para vistas HTML). Usar `@RestController` para APIs REST | `@Controller` |
| `@RequestMapping` | Clase/Método | Define la URL base para el controlador o método. Puede especificar verbo(s) HTTP | `@RequestMapping("/api/productos")` |
| `@GetMapping` | Método | Mapea peticiones GET | `@GetMapping("/productos")` |
| `@PostMapping` | Método | Mapea peticiones POST | `@PostMapping` |
| `@PutMapping` | Método | Mapea peticiones PUT | `@PutMapping("/{id}")` |
| `@PatchMapping` | Método | Mapea peticiones PATCH (actualización parcial) | `@PatchMapping("/{id}")` |
| `@DeleteMapping` | Método | Mapea peticiones DELETE | `@DeleteMapping("/{id}")` |

---

### Anotaciones de Parámetros

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@PathVariable` | Parámetro | Extrae valor de la URL (variable de ruta) | `@GetMapping("/{id}") Long id)` |
| `@RequestParam` | Parámetro | Extrae query parameters (después del `?`) | `@RequestParam String nombre` |
| `@RequestBody` | Parámetro | Convierte JSON del body a objeto Java | `@RequestBody Producto p` |
| `@ResponseBody` | Método | Fuerza que el return se envíe como JSON (implícito en @RestController) | `@ResponseBody` |

---

### Anotaciones de Inyección de Dependencias

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@Autowired` | Campo/Constructor/Setter | Inyecta automáticamente una dependencia | `@Autowired private Repo repo;` |
| `@Component` | Clase | Marca la clase como componente Spring (bean) genérico | `@Component` |
| `@Service` | Clase | Marca la clase como servicio (similar a @Component, pero semántico) | `@Service` |
| `@Repository` | Interfaz/Clase | Marca como repositorio de acceso a datos | `@Repository` |

---

### Anotaciones JPA / Persistencia

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@Entity` | Clase | Marca la clase como entidad JPA (tabla) | `@Entity` |
| `@Table` | Clase | Especifica nombre de la tabla en BD | `@Table(name = "PRODUCTOS")` |
| `@Id` | Campo | Marca el campo como llave primaria | `@Id` |
| `@GeneratedValue` | Campo | Define estrategia de generación del ID | `@GeneratedValue(strategy=IDENTITY)` |
| `@Column` | Campo | Configura columna específica (nombre, nullable, length) | `@Column(name = "precio_unitario")` |
| `@Transient` | Campo | Excluye el campo de persistencia (no se guarda en BD) | `@Transient` |

---

### Anotaciones de Configuración

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `@Configuration` | Clase | Marca la clase como fuente de configuración/beans | `@Configuration` |
| `@Bean` | Método | Declara un bean manualmente dentro de @Configuration | `@Bean public DataSource ds() { }` |
| `@Primary` | Clase/Método | Indica que este bean es el principal cuando hay múltiples | `@Primary` |

---

### Anotaciones de Respuesta HTTP

| Anotación | Ubicación | Descripción | Ejemplo |
|-----------|-----------|-------------|---------|
| `ResponseEntity<T>` | Tipo de Return | Control total sobre la respuesta HTTP (status, headers, body) | `ResponseEntity.ok(p)` |
| `@ResponseStatus` | Método/Clase | Define el código de estado HTTP | `@ResponseStatus(HttpStatus.NOT_FOUND)` |

---

## 📊 Diagrama Visual: Dónde se Usa Cada Anotación

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ANOTACIONES POR CAPA                                  │
│                                                                              │
│                                                                              │
│  ═══════════════════════════════════════════════════════════════════════════│
│  CAPA: APLICACIÓN (main)                                                     │
│  ═══════════════════════════════════════════════════════════════════════════│
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  AppApplication.java                                                │   │
│  │                                                                      │   │
│  │  @SpringBootApplication  ← Punto de entrada                        │   │
│  │  public class AppApplication {                                       │   │
│  │      public static void main(String[] args) { ... }                  │   │
│  │  }                                                                   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ═══════════════════════════════════════════════════════════════════════════│
│  CAPA: CONTROLADOR (REST API)                                                │
│  ═══════════════════════════════════════════════════════════════════════════│
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ProductoController.java                                             │   │
│  │                                                                      │   │
│  │  @RestController                        ← Controlador REST           │   │
│  │  @RequestMapping("/api/productos")       ← URL base                 │   │
│  │  public class ProductoController {                                  │   │
│  │                                                                      │   │
│  │      @Autowired                              ← Inyección             │   │
│  │      private ProductoRepository repo;                               │   │
│  │                                                                      │   │
│  │      @GetMapping                             ← GET                  │   │
│  │      public List<Producto> lista() { ... }                          │   │
│  │                                                                      │   │
│  │      @GetMapping("/{id}")                    ← GET con path var     │   │
│  │      public Producto obtener(                ← @PathVariable         │   │
│  │          @PathVariable Long id) { ... }                              │   │
│  │                                                                      │   │
│  │      @PostMapping                            ← POST                 │   │
│  │      public Producto crear(                  ← @RequestBody        │   │
│  │          @RequestBody Producto p) { ... }                            │   │
│  │  }                                                                   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ═══════════════════════════════════════════════════════════════════════════│
│  CAPA: REPOSITORIO (acceso a datos)                                          │
│  ═══════════════════════════════════════════════════════════════════════════│
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ProductoRepository.java                                             │   │
│  │                                                                      │   │
│  │  @Repository                                  ← Repositorio          │   │
│  │  public interface ProductoRepository          ← Extiende JPA        │   │
│  │      extends JpaRepository<Producto, Long> {                         │   │
│  │                                                                      │   │
│  │      // Query Methods                                               │   │
│  │      Producto findByNombre(String nombre);                           │   │
│  │  }                                                                   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ═══════════════════════════════════════════════════════════════════════════│
│  CAPA: MODELO (entidades)                                                    │
│  ═══════════════════════════════════════════════════════════════════════════│
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Producto.java                                                        │   │
│  │                                                                      │   │
│  │  @Entity                                     ← Tabla JPA            │   │
│  │  @Table(name = "PRODUCTO")                   ← Nombre tabla         │   │
│  │  public class Producto {                                             │   │
│  │                                                                      │   │
│  │      @Id                                     ← Llave primaria       │   │
│  │      @GeneratedValue                         ← Auto-generado       │   │
│  │      private Long id;                                                │   │
│  │                                                                      │   │
│  │      @Column(name = "nombre", nullable = false)  ← Columna config  │   │
│  │      private String nombre;                                           │   │
│  │                                                                      │   │
│  │      private String descripcion;                                      │   │
│  │      private Double precio;                                          │   │
│  │  }                                                                   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ═══════════════════════════════════════════════════════════════════════════│
│  CAPA: CONFIGURACIÓN (beans custom)                                          │
│  ═══════════════════════════════════════════════════════════════════════════│
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  DataInitializer.java                                                │   │
│  │                                                                      │   │
│  │  @Configuration                              ← Configuración         │   │
│  │  public class DataInitializer {                                      │   │
│  │                                                                      │   │
│  │      @Bean                                    ← Bean manual         │   │
│  │      CommandLineRunner init(ProductoRepository repo) {              │   │
│  │          // código de inicialización                                 │   │
│  │      }                                                               │   │
│  │  }                                                                   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Mapa Mental de Anotaciones

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           SPRING BOOT ANNOTATIONS                            │
│                                                                              │
│                                                                              │
│    ┌─────────────────────────────────────────────────────────────────────┐   │
│    │                    POR UBICACIÓN                                     │   │
│    │                                                                     │   │
│    │   ┌──────────────┐ ┌──────────────┐ ┌──────────────┐                │   │
│    │   │    CLASE      │ │   MÉTODO     │ │  PARÁMETRO   │                │   │
│    │   ├──────────────┤ ├──────────────┤ ├──────────────┤                │   │
│    │   │@SpringBoot   │ │@GetMapping   │ │@PathVariable │                │   │
│    │   │@RestController│ │@PostMapping │ │@RequestParam │                │   │
│    │   │@Controller   │ │@PutMapping   │ │@RequestBody  │                │   │
│    │   │@RequestMapping│ │@DeleteMapping│ │              │                │   │
│    │   │@Entity       │ │@Bean         │ │              │                │   │
│    │   │@Table        │ │@Transactional│ │              │                │   │
│    │   │@Configuration│ │              │ │              │                │   │
│    │   │@Repository   │ │              │ │              │                │   │
│    │   │@Component    │ │              │ │              │                │   │
│    │   │@Service      │ │              │ │              │                │   │
│    │   └──────────────┘ └──────────────┘ └──────────────┘                │   │
│    │                                                                     │   │
│    └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│    ┌─────────────────────────────────────────────────────────────────────┐   │
│    │                    POR FUNCIONALIDAD                                 │   │
│    │                                                                     │   │
│    │   HTTP REST                                                          │   │
│    │   ├── @RestController (clase)                                        │   │
│    │   ├── @GetMapping / @PostMapping / @PutMapping / @DeleteMapping     │   │
│    │   ├── @PathVariable (parámetro)                                     │   │
│    │   └── @RequestBody (parámetro)                                      │   │
│    │                                                                     │   │
│    │   INYECCIÓN                                                           │   │
│    │   ├── @Autowired (campo/método)                                     │   │
│    │   ├── @Component (clase)                                            │   │
│    │   ├── @Service (clase)                                              │   │
│    │   └── @Repository (clase)                                           │   │
│    │                                                                     │   │
│    │   JPA/PERSISTENCIA                                                    │   │
│    │   ├── @Entity (clase)                                                │   │
│    │   ├── @Table (clase)                                                │   │
│    │   ├── @Id (campo)                                                   │   │
│    │   ├── @GeneratedValue (campo)                                        │   │
│    │   └── @Column (campo)                                               │   │
│    │                                                                     │   │
│    │   CONFIGURACIÓN                                                      │   │
│    │   ├── @Configuration (clase)                                       │   │
│    │   ├── @Bean (método)                                                │   │
│    │   └── @SpringBootApplication (clase)                               │   │
│    │                                                                     │   │
│    └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes al Usar Anotaciones

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **Anotación en lugar incorrecto** | `@PathVariable` en lugar de `@RequestParam` | Usar el correcto según el tipo de parámetro |
| 2 | **@Autowired en null** | No se escaneó el paquete | Verificar que la clase está en el mismo proyecto/módulo |
| 3 | **@Entity not found** | Falta dependencia JPA | Añadir `spring-boot-starter-data-jpa` |
| 4 | **Confundir @Controller con @RestController** | Return es vista vs JSON | Usar `@RestController` para APIs REST |
| 5 | **@RequestBody null** | JSON malformado | Validar que el JSON sea válido |
| 6 | **No @Id en Entity** | JPA requiere PK | Añadir `@Id` a un campo |
| 7 | **@PathVariable sin valor** | Nombre diferente en URL | Usar `@PathVariable("id")` |

---

## 💡 Tips para Memorizar

### 1. Agrupa por capas

```
PELOTA DE TENIS → Memoriza las capas de afuera hacia adentro:
┌─────────────────────────────────┐
│         @Controller            │  ← Capa exterior (HTTP)
│           @Service             │  ← Capa media (lógica)
│           @Repository          │  ← Capa interior (datos)
│             @Entity            │  ← Mapeo a BD
└─────────────────────────────────┘
```

### 2. Por sufijo recuerda la función

```
@GetMapping     → GET (obtener)
@PostMapping    → POST (crear)
@PutMapping     → PUT (actualizar)
@DeleteMapping  → DELETE (eliminar)

@Component      → Componente genérico
@Service        → Servicio de negocio
@Repository     → Acceso a datos
@Configuration   → Configuración
```

### 3. @Autowired es contexto

```
@Autowired
├── En Repository → Spring inyecta implementación de JPA
├── En Service → Spring inyecta otro bean
└── En Controller → Spring inyecta el Repository
```

### 4. Entity tiene @Id SIEMPRE

```
@Entity
public class Producto {
    @Id  ← SIEMPRE necesitas un @Id
    private Long id;
}
```

### 5. Request vs Response

```
@RequestBody  → Request (entrada) → JSON → Objeto
@ResponseBody → Response (salida) → Objeto → JSON
```

---

## 📝 Resumen Rápido

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║                         CHEAT SHEET DE ANOTACIONES                           ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                                                                               ║
║  CLASE PRINCIPAL                                                              ║
║  @SpringBootApplication           ← main class                               ║
║                                                                               ║
║  CONTROLADORES                                                                ║
║  @RestController                  ← API REST                                 ║
║  @RequestMapping("/path")         ← URL base                                 ║
║  @GetMapping / @PostMapping / ... ← Verbos HTTP                              ║
║                                                                               ║
║  PARÁMETROS                                                                   ║
║  @PathVariable                     ← De URL: /{id}                          ║
║  @RequestParam                     ← De query: ?id=1                        ║
║  @RequestBody                      ← JSON del body                          ║
║                                                                               ║
║  INYECCIÓN                                                                    ║
║  @Autowired                        ← Inyectar dependencia                    ║
║  @Component / @Service / @Repository ← Tipos de beans                      ║
║                                                                               ║
║  JPA                                                                          
║  @Entity                           ← Mapeo a tabla                          ║
║  @Table(name="...")                ← Nombre de tabla                        ║
║  @Id                               ← Llave primaria                         ║
║  @GeneratedValue                    ← ID auto-generado                      ║
║  @Column                           ← Configuración de columna               ║
║                                                                               ║
║  CONFIGURACIÓN                                                                
║  @Configuration                     ← Clase de configuración                ║
║  @Bean                             ← Bean manual                            ║
║                                                                               ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué anotación marca la clase main de Spring Boot?

- [ ] A) @Controller
- [ ] B) @SpringBootApplication
- [ ] C) @Entity
- [ ] D) @Configuration

### Pregunta 2:
¿Qué anotación extrae valores de la URL?

- [ ] A) @RequestBody
- [ ] B) @RequestParam
- [ ] C) @PathVariable
- [ ] D) @Autowired

### Pregunta 3:
¿Cuál anotación convierte JSON a objeto Java?

- [ ] A) @ResponseBody
- [ ] B) @RequestBody
- [ ] C) @RequestParam
- [ ] D) @PathVariable

### Pregunta 4:
¿Qué anotación marca una clase como entidad JPA?

- [ ] A) @Table
- [ ] B) @Entity
- [ ] C) @Id
- [ ] D) @Column

### Pregunta 5:
¿Cuál es la diferencia entre @Controller y @RestController?

- [ ] A) Ninguna
- [ ] B) @RestController retorna JSON automáticamente
- [ ] C) @Controller es más rápido
- [ ] D) @RestController solo acepta POST

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Identifica las anotaciones

En el siguiente código, identifica qué anotación corresponde a cada línea:

```java
_______      // 1
public class ProductoController {
    
    _______      // 2
    private ProductoRepository repo;
    
    _______      // 3
    @GetMapping("/{id}")
    public Producto obtener(_______ Long id) {  // 4
        return repo.findById(id).get();
    }
}
```

### Ejercicio 2: Corrige los errores

Encuentra y corrige los errores:

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private ProductoRepository repo;
    
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return repo.findById(id).get();
    }
    
    @PostMapping
    public Producto crear(Producto p) {  // ¿Qué falta?
        return repo.save(p);
    }
}
```

### Ejercicio 3: Escribe las anotaciones

Escribe el código completo para:

1. Un Entity `Cliente` con:
   - @Id auto-generado
   - Campos: nombre, email, telefono

2. Un Repository `ClienteRepository`

3. Un Controller `ClienteController` con:
   - GET /api/clientes
   - GET /api/clientes/{id}
   - POST /api/clientes

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
