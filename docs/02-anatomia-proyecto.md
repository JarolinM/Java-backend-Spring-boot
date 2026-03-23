# 🔍 Anatomía de un Proyecto Spring Boot

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, entenderás la estructura completa de un proyecto Spring Boot, cómo se organizan los archivos y qué función tiene cada componente.

---

## 📚 Teoría

### ¿Qué es MVC?

**MVC** significa **Model-View-Controller** y es un patrón de arquitectura de software que separa las responsabilidades en tres partes:

```
┌─────────────────────────────────────────────────────────────────┐
│                          MVC                                     │
│                                                                 │
│    ┌───────────┐    ┌───────────┐    ┌───────────┐              │
│    │   MODEL   │◀───│CONTROLLER │◀───│   VIEW    │              │
│    │           │    │           │    │           │              │
│    │  Datos    │    │ Lógica    │    │ Interfaz  │              │
│    │ Entidades │    │ Negocio   │    │ (HTML)    │              │
│    └───────────┘    └───────────┘    └───────────┘              │
│         │                │                                       │
│         │                │                                       │
│         ▼                ▼                                       │
│    Repository        Cliente                                    │
│    (BD)              (Navegador)                                │
└─────────────────────────────────────────────────────────────────┘
```

**En REST con Spring Boot:**
- **Model** → Entidades (Producto.java)
- **View** → JSON (no hay HTML en REST API)
- **Controller** → ProductoController.java

### Packages en Spring Boot

Un proyecto Spring Boot bien organizado usa **packages** (paquetes) para separar las responsabilidades:

```
com.ejemplo.productoapi
├── controller/      ← Controladores (manejan requests HTTP)
├── model/           ← Entidades (representan datos)
├── repository/      ← Repositorios (acceso a base de datos)
├── service/         ← Servicios (lógica de negocio) [opcional]
└── config/          ← Configuraciones especiales
```

---

## 💻 Código: Archivo por Archivo

### 1. AppApplication.java (Punto de Entrada)

```java
package com.ejemplo.productoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        // Este método inicia toda la aplicación Spring Boot
        SpringApplication.run(AppApplication.class, args);
    }
}
```

#### ¿Qué hace cada parte?

| Código | Significado |
|--------|-------------|
| `package com.ejemplo.productoapi;` | Ubicación del archivo en el proyecto |
| `@SpringBootApplication` | **Anotación mágica** que dice: "Esta clase inicia Spring Boot" |
| `public static void main` | Punto de entrada de cualquier aplicación Java |
| `SpringApplication.run(...)` | Inicia el servidor embebido de Tomcat |

**Analogía:** Piensa en `@SpringBootApplication` como el interruptor de luz. Sin él, nada en la casa funciona.

---

### 2. Producto.java (Modelo/Entidad)

```java
package com.ejemplo.productoapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;

    // Constructor vacío (requerido por JPA)
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(String nombre, String descripcion, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
```

#### ¿Qué hace cada anotación?

| Anotación | Para qué sirve |
|-----------|----------------|
| `@Entity` | Le dice a JPA: "Esta clase es una tabla de BD" |
| `@Table(name = "PRODUCTO")` | Nombre exacto de la tabla en la BD |
| `@Id` | Este campo es la llave primaria |
| `@GeneratedValue` | El ID se genera automáticamente |

#### Campos de la entidad:

| Campo | Tipo Java | Tipo en BD |
|-------|-----------|------------|
| id | Long | BIGINT (auto-generado) |
| nombre | String | VARCHAR |
| descripcion | String | VARCHAR |
| precio | Double | DOUBLE |

---

### 3. ProductoRepository.java (Repositorio)

```java
package com.ejemplo.productoapi.repository;

import com.ejemplo.productoapi.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository 
    extends JpaRepository<Producto, Long> {
    
    // JpaRepository ya provee:
    // - findAll()      → Lista todos
    // - findById(id)   → Busca por ID
    // - save(producto) → Guarda/actualiza
    // - deleteById(id) → Elimina por ID
}
```

#### ¿Por qué es una interfaz y no una clase?

Spring Data JPA usa **magia**: tú defines la interfaz y Spring genera la implementación automáticamente.

#### Métodos heredados de JpaRepository:

| Método | Descripción |
|--------|-------------|
| `findAll()` | Retorna todos los productos |
| `findById(id)` | Busca uno por ID (Optional) |
| `save(producto)` | Guarda nuevo o actualiza existente |
| `deleteById(id)` | Elimina por ID |
| `count()` | Cuenta cuántos hay |
| `existsById(id)` | Verifica si existe |

---

### 4. ProductoController.java (Controlador REST)

```java
package com.ejemplo.productoapi.controller;

import com.ejemplo.productoapi.model.Producto;
import com.ejemplo.productoapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // GET /api/productos
    @GetMapping
    public List<Producto> listaProductos() {
        return productoRepository.findAll();
    }

    // GET /api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/productos
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto guardado = productoRepository.save(producto);
        return ResponseEntity.status(201).body(guardado);
    }
}
```

#### Anotaciones explicadas:

| Anotación | Qué hace |
|-----------|----------|
| `@RestController` | Marca esta clase como controlador REST |
| `@RequestMapping("/api/productos")` | URL base para todos los endpoints |
| `@GetMapping` | Responde solo a peticiones GET |
| `@PostMapping` | Responde solo a peticiones POST |
| `@PathVariable` | Extrae el {id} de la URL |
| `@RequestBody` | Convierte el JSON del body a objeto Java |
| `@Autowired` | Inyecta automáticamente el repository |
| `ResponseEntity` | Control total sobre el HTTP response |

---

### 5. DataInitializer.java (Datos Iniciales)

```java
package com.ejemplo.productoapi.config;

import com.ejemplo.productoapi.model.Producto;
import com.ejemplo.productoapi.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository repository) {
        return args -> {
            repository.save(new Producto("Laptop", "16GB RAM", 999.99));
            repository.save(new Producto("Mouse", "Inalámbrico", 29.99));
            repository.save(new Producto("Teclado", "Mecánico", 79.99));
            repository.save(new Producto("Monitor", "24 pulgadas", 199.99));
            repository.save(new Producto("USB", "32GB", 15.99));
            
            System.out.println("✓ Datos iniciales cargados");
        };
    }
}
```

#### ¿Qué hace?

Se ejecuta **automáticamente** cuando inicia la aplicación y carga 5 productos de ejemplo en la base de datos.

---

## 📊 Diagrama: Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        ARQUITECTURA EN CAPAS                                 │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                        CAPA 1: PRESENTACIÓN                         │    │
│  │                                                                      │    │
│  │   ┌─────────────────────────────────────────────────────────────┐  │    │
│  │   │                    ProductoController                         │  │    │
│  │   │                      @RestController                          │  │    │
│  │   │                                                                   │  │    │
│  │   │   Recibe HTTP Request ──► Procesa ──► Devuelve HTTP Response │  │    │
│  │   └─────────────────────────────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                        CAPA 2: NEGOCIO (implícito)                   │    │
│  │                                                                      │    │
│  │   El Controller usa directamente el Repository                      │    │
│  │   (En apps más grandes, habría una capa Service aquí)               │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                        CAPA 3: DATOS                                 │    │
│  │                                                                      │    │
│  │   ┌─────────────────────────────────────────────────────────────┐  │    │
│  │   │                    ProductoRepository                         │  │    │
│  │   │                      @Repository                             │  │    │
│  │   │                                                                   │  │    │
│  │   │   JpaRepository<Producto, Long>                                │  │    │
│  │   │   ├── findAll()                                                │  │    │
│  │   │   ├── findById(id)                                             │  │    │
│  │   │   └── save(producto)                                           │  │    │
│  │   └─────────────────────────────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                        CAPA 4: PERSISTENCIA                         │    │
│  │                                                                      │    │
│  │   ┌─────────────────────────────────────────────────────────────┐  │    │
│  │   │                      Producto (Entidad)                      │  │    │
│  │   │                         @Entity                              │  │    │
│  │   │                                                                   │  │    │
│  │   │   Representa la tabla PRODUCTO en la base de datos            │  │    │
│  │   │   JPA/Hibernate convierte objetos Java ↔ filas SQL            │  │    │
│  │   └─────────────────────────────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Diagrama: Dependencias entre Clases

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          DEPENDENCIAS                                        │
│                                                                              │
│                                                                              │
│                         ┌───────────────────┐                               │
│                         │  AppApplication   │                               │
│                         │ @SpringBootApp    │                               │
│                         └─────────┬─────────┘                               │
│                                   │                                          │
│                                   │ inicia                                    │
│                                   ▼                                          │
│                         ┌───────────────────┐                               │
│                         │ ProductoController│ ◀────── @Autowired           │
│                         │  @RestController │                               │
│                         └─────────┬─────────┘                               │
│                                   │                                          │
│                                   │ usa                                      │
│                                   ▼                                          │
│                         ┌───────────────────┐                               │
│                         │ ProductoRepository│ ◀────── extiende            │
│                         │   @Repository    │                               │
│                         └─────────┬─────────┘                               │
│                                   │                                          │
│                                   │ operaciones CRUD                        │
│                                   ▼                                          │
│                         ┌───────────────────┐                               │
│                         │      JPA          │                               │
│                         │  (Hibernate)      │                               │
│                         └─────────┬─────────┘                               │
│                                   │                                          │
│                                   │ convierte                                │
│                                   ▼                                          │
│                         ┌───────────────────┐                               │
│                         │   Producto        │                               │
│                         │    @Entity        │                               │
│                         └─────────┬─────────┘                               │
│                                   │                                          │
│                                   │ mapea a                                  │
│                                   ▼                                          │
│                         ┌───────────────────┐                               │
│                         │  Tabla PRODUCTO   │                               │
│                         │   (H2 Database)   │                               │
│                         └───────────────────┘                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes

| # | Error | Solución |
|---|-------|----------|
| 1 | **Clase no encontrada** | Verifica que el `package` sea correcto |
| 2 | **Entity not mapped** | Revisa que `@Entity` esté presente |
| 3 | **NullPointerException en repository** | Falta `@Autowired` |
| 4 | **404 en todos los endpoints** | Falta `@RestController` o `@RequestMapping` |
| 5 | **Tabla no existe** | Falta `spring.jpa.hibernate.ddl-auto=create` |
| 6 | **Sin getters/setters** | JPA necesita getters para leer, setters para escribir |
| 7 | **Importar clases incorrectas** | Usar `jakarta.persistence` no `javax.persistence` (en Spring Boot 3+) |

---

## 💡 Tips

### 1. Convenciones de nombres
```
Clase:        ProductoController
              ProductoRepository
              Producto (entidad)
              
Archivo:      ProductoController.java
              ProductoRepository.java
              Producto.java

URLs:         /api/productos
```

### 2. Estructura del package
```
com.empresa.proyecto
     │        │
     │        └── nombre del proyecto
     │
     └── paquete base
```

### 3. Orden de creación recomendado:
```
1. Crear el modelo (Producto.java)
2. Crear el repository (ProductoRepository.java)
3. Crear el controller (ProductoController.java)
4. Crear datos iniciales (DataInitializer.java)
5. Probar
```

### 4. Comandos útiles Maven:
```bash
mvn clean              # Limpia archivos compilados
mvn compile           # Compila el proyecto
mvn spring-boot:run    # Ejecuta la aplicación
mvn package            # Crea el JAR
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué anotación marca la clase principal de Spring Boot?

- [ ] A) @Entity
- [ ] B) @Controller
- [ ] C) @SpringBootApplication
- [ ] D) @RestController

### Pregunta 2:
¿Qué capa se encarga de recibir las peticiones HTTP?

- [ ] A) Model
- [ ] B) Repository
- [ ] C) Controller
- [ ] D) Entity

### Pregunta 3:
¿Qué significa `@Autowired`?

- [ ] A) Crea una nueva instancia
- [ ] B) Inyecta automáticamente una dependencia
- [ ] C) Guarda en base de datos
- [ ] D) Convierte JSON a objeto

### Pregunta 4:
¿Qué paquete contiene las entidades?

- [ ] A) controller
- [ ] B) repository
- [ ] C) model
- [ ] D) config

### Pregunta 5:
¿Para qué sirve `@Entity`?

- [ ] A) Para crear un controlador
- [ ] B) Para mapear una clase a una tabla
- [ ] C) Para recibir peticiones HTTP
- [ ] D) Para guardar datos

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Identifica el archivo

Indica qué archivo (.java) corresponde a cada función:

1. Recibe peticiones HTTP y devuelve JSON ___________
2. Representa una tabla en la base de datos ___________
3. Provee métodos para acceder a la BD ___________
4. Es el punto de entrada de la aplicación ___________
5. Carga datos iniciales al iniciar ___________

### Ejercicio 2: Completa las anotaciones

Añade la anotación correcta a cada lugar:

```java
_______      // 1
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
}

_______      // 2
public interface ProductoRepository 
    extends JpaRepository<Producto, Long> {
}

_______      // 3
@RequestMapping("/api/productos")
public class ProductoController {
    ...
}
```

### Ejercicio 3: Crea un nuevo campo

En `Producto.java`, añade un nuevo campo:
- Nombre: `stock`
- Tipo: `Integer`
- Descripción: cantidad disponible

Luego, ¿qué cambia automáticamente?

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*

*Autor: Jarolin Matos Martinez*
