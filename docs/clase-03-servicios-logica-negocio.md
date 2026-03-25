# Clase 3: Servicios y Lógica de Negocio con Spring Boot

---

## 📋 Tabla de Contenidos

1. [Bienvenida y Repaso](#bienvenida)
2. [Inversión de Dependencias](#inversion-dependencias)
3. [Patrón Service Layer](#service-layer)
4. [Anotaciones de Componente](#anotaciones)
5. [Implementación de Servicios](#implementacion)
6. [Refactorización con IA](#refactorizacion-ia)
7. [Conclusiones y Ejercicios](#conclusiones)

---

<a name="bienvenida"></a>
## 1. Bienvenida y Repaso

### 🎯 Objetivos de Aprendizaje

Al finalizar esta clase, serás capaz de:

- ✅ Comprender el concepto de **Inversión de Dependencias**
- ✅ Implementar servicios limpios y reutilizables con **@Service**
- ✅ Separar responsabilidades entre **Controllers** y **Services**
- ✅ Conocer cuándo usar **@Component**, **@Bean**, **@Configuration**
- ✅ Aplicar **refactorización asistida por IA** para dividir lógica compleja

### 📝 Repaso: Arquitectura Actual

```
┌─────────────────────────────────────────────────────────────────┐
│              ARQUITECTURA SIN SERVICE LAYER                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Controller                                                     │
│  ├── Recibe petición                                           │
│  ├── Lógica de negocio (✗ MEZCLADO)                          │
│  ├── Validaciones                                             │
│  ├── Acceso a BD (repository)                                  │
│  └── Formatear respuesta                                       │
│                                                                 │
│  ❌ Problemas:                                                 │
│  ├── Difícil de mantener                                     │
│  ├── Código duplicado si hay otro controller                  │
│  ├── Difícil de probar unitariamente                          │
│  └── Violación del principio de responsabilidad única          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📋 Estructura Actual del Proyecto

```
src/main/java/com/product/app/
├── controller/
│   └── ProductoController.java     ← Toda la lógica aquí
├── model/
│   └── Producto.java
└── repository/
    └── ProductoRepository.java
```

---

<a name="inversion-dependencias"></a>
## 2. Inversión de Dependencias (Dependency Injection)

### 📖 ¿Qué es la Inversión de Dependencias?

```
┌─────────────────────────────────────────────────────────────────┐
│           ¿QUÉ ES LA INVERSIÓN DE DEPENDENCIAS?                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  PRINCIPIO:                                                    │
│  "Los módulos de alto nivel no deben depender                    │
│   de los módulos de bajo nivel.                                 │
│   Ambos deben depender de abstracciones."                       │
│                                               — Robert C. Martin│
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 🔄 Sin DI vs Con DI

```
┌─────────────────────────────────────────────────────────────────┐
│                    SIN INVERSIÓN DE DEPENDENCIAS                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  public class ProductoController {                              │
│                                                                 │
│      // ❌ Creamos la dependencia directamente                   │
│      private ProductoRepository repo = new ProductoRepository(); │
│                                                                 │
│      // Problema: ¿Cómo probamos sin BD real?                   │
│      // Problema: ¿Cómo cambiamos a otro repositorio?           │
│  }                                                              │
│                                                                 │
│  Acoplamiento fuerte: Controller conoce implementación exacta  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    CON INVERSIÓN DE DEPENDENCIAS                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  public class ProductoController {                              │
│                                                                 │
│      // ✅ Recibimos la dependencia por constructor              │
│      private final ProductoRepository repo;                     │
│                                                                 │
│      public ProductoController(ProductoRepository repo) {       │
│          this.repo = repo;                                      │
│      }                                                          │
│  }                                                              │
│                                                                 │
│  ✅ Flexibilidad: Podemos pasar cualquier implementación        │
│  ✅ Testabilidad: Podemos pasar un mock                        │
│  ✅ Bajo acoplamiento: Dependemos de abstracciones             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 💉 Tipos de Inyección de Dependencias

```
┌─────────────────────────────────────────────────────────────────┐
│              TIPOS DE INYECCIÓN EN SPRING                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1️⃣ CONSTRUCTOR INJECTION (Recomendada)                        │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  @RestController                                         │ │
│  │  public class ProductoController {                       │ │
│  │                                                           │ │
│  │      private final ProductoService service;              │ │
│  │                                                           │ │
│  │      public ProductoController(ProductoService service) { │ │
│  │          this.service = service;                         │ │
│  │      }                                                    │ │
│  │  }                                                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ✅ Inmutable (final)                                         │
│  ✅ Obligatorio (garantizado en constructor)                  │
│  ✅ Fácil de probar                                           │
│                                                                 │
│  2️⃣ SETTER INJECTION                                          │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  @RestController                                         │ │
│  │  public class ProductoController {                       │ │
│  │                                                           │ │
│  │      private ProductoService service;                    │ │
│  │                                                           │ │
│  │      @Autowired                                          │ │
│  │      public void setService(ProductoService service) {   │ │
│  │          this.service = service;                         │ │
│  │      }                                                    │ │
│  │  }                                                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ⚠️ Opcional, más flexible pero menos seguro                 │
│                                                                 │
│  3️⃣ FIELD INJECTION (No recomendada)                          │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  @RestController                                         │ │
│  │  public class ProductoController {                       │ │
│  │                                                           │ │
│  │      @Autowired                                          │ │
│  │      private ProductoService service;                    │ │
│  │  }                                                        │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ❌ Dificulta pruebas                                        │
│  ❌ Rompe inmutabilidad                                      │
│  ❌ Oculta dependencias                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 💻 Ejemplo Práctico: Constructor Injection

```java
package com.product.app.controller;

import org.springframework.web.bind.annotation.*;
import com.product.app.service.ProductoService;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    // ✅ Inyección por constructor (Spring lo hace automáticamente)
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listarTodos();
    }
    
    // ... otros métodos
}
```

---

<a name="service-layer"></a>
## 3. Patrón Service Layer (Capa de Servicio)

### 📖 ¿Qué es el Patrón Service Layer?

```
┌─────────────────────────────────────────────────────────────────┐
│                    PATRÓN SERVICE LAYER                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  DEFINICIÓN:                                                    │
│  La capa de servicio contiene la lógica de negocio             │
│  y actúa como intermediario entre los controllers              │
│  y los repositories.                                           │
│                                                                 │
│  RESPONSABILIDADES:                                             │
│  ├── Ejecutar lógica de negocio                               │
│  ├── Coordinar operaciones (transacciones)                   │
│  ├── Aplicar validaciones de negocio                          │
│  ├── Transformar datos entre capas                           │
│  └── Manejar errores específicos del dominio                  │
│                                                                 │
│  RESPONSABILIDADES (NO):                                       │
│  ├── ✗ Recibir peticiones HTTP                               │
│  ├── ✗ Formatear respuestas JSON                             │
│  └── ✗ Acceso directo a base de datos                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 🏗️ Arquitectura por Capas

```
┌─────────────────────────────────────────────────────────────────┐
│              ARQUITECTURA POR CAPAS EN SPRING                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                   PRESENTATION LAYER                      │  │
│  │  @RestController                                         │  │
│  │  ├── Recibe peticiones HTTP                             │  │
│  │  ├── Valida parámetros                                  │  │
│  │  ├── Llama al Service                                   │  │
│  │  └── Formatea la respuesta                              │  │
│  └──────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    SERVICE LAYER                          │  │
│  │  @Service                                               │  │
│  │  ├── Lógica de negocio                                 │  │
│  │  ├── Validaciones de dominio                            │  │
│  │  ├── Transacciones                                     │  │
│  │  └── Coordina repositories                              │  │
│  └──────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                   DATA ACCESS LAYER                      │  │
│  │  @Repository (JpaRepository)                            │  │
│  │  ├── Acceso a base de datos                            │  │
│  │  ├── Operaciones CRUD                                  │  │
│  │  └── Consultas específicas                             │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📊 Comparación: Sin Service vs Con Service

```
┌─────────────────────────────────────────────────────────────────┐
│                    SIN SERVICE LAYER                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Controller                                                     │
│  ├── if (producto.precio < 0) throw error;  ← Lógica mezclada│
│  ├── repo.save(producto);                                      │
│  ├── log.info("Producto guardado");  ← Más lógica mezclada    │
│  └── return producto;                                          │
│                                                                 │
│  ❌ Controller hace demasiado                                  │
│  ❌ Lógica duplicada en otros controllers                      │
│  ❌ Difícil de cambiar la lógica                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    CON SERVICE LAYER                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Controller:                        Service:                     │
│  ├── Validar @PathVariable    →    ├── Validar precio negativo│
│  ├── Llamar service.crear()   →    ├── Guardar en BD         │
│  └── Retornar Response        ←    ├── Logging               │
│                                     └── Retornar resultado      │
│                                                                 │
│  ✅ Controller: limpio y simple                               │
│  ✅ Service: reutilizable                                    │
│  ✅ Fácil de testear                                         │
│  ✅ Lógica centralizada                                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

<a name="anotaciones"></a>
## 4. Anotaciones de Componente (@Component, @Service, @Repository, @Configuration)

### 📋 Resumen de Anotaciones

```
┌─────────────────────────────────────────────────────────────────┐
│              ANOTACIONES DE COMPONENTE SPRING                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  @Component                                                     │
│  ├── Anotación genérica para cualquier bean                     │
│  ├── Uso: Clases utilitarias, helpers                          │
│  └── Ejemplo: Clase de utilidad, converters                    │
│                                                                 │
│  @Service                                                       │
│  ├── Especialización de @Component                             │
│  ├── Semántica: lógica de negocio                             │
│  ├── Uso: Clases de servicio                                  │
│  └── Ejemplo: ProductoService, UsuarioService                  │
│                                                                 │
│  @Repository                                                    │
│  ├── Especialización de @Component                             │
│  ├── Semántica: acceso a datos                                │
│  ├── Uso: Repositorios JPA (normalmente Spring Data lo hace)  │
│  └── Ejemplo: ProductoRepository                              │
│                                                                 │
│  @Configuration                                                 │
│  ├── Marca una clase como fuente de definiciones de beans     │
│  ├── Uso: Configuración programática                          │
│  └── Ejemplo: Configurar DataSource, crear beans              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 💻 Diferencia Visual

```java
// ═══════════════════════════════════════════════════════════════════
// @Component - Uso Genérico
// ═══════════════════════════════════════════════════════════════════

@Component  // ← Anotación base/genérica
public class ConversorMoneda {
    
    public Double convertir(Double monto, String de, String a) {
        // Lógica de conversión
        return monto * getTasaCambio(de, a);
    }
}

// ═══════════════════════════════════════════════════════════════════
// @Service - Lógica de Negocio
// ═══════════════════════════════════════════════════════════════════

@Service  // ← Especialización semántica para servicios
public class ProductoService {
    
    private final ProductoRepository repository;
    
    // Lógica de negocio
    public Producto crearProducto(Producto producto) {
        // Validar reglas de negocio
        validarProducto(producto);
        
        // Aplicar descuentos si aplica
        Double precioFinal = calcularPrecio(producto);
        producto.setPrecio(precioFinal);
        
        // Guardar
        return repository.save(producto);
    }
}

// ═══════════════════════════════════════════════════════════════════
// @Configuration - Configuración
// ═══════════════════════════════════════════════════════════════════

@Configuration  // ← Clase de configuración
public class AppConfig {
    
    @Bean  // ← Define un bean manualmente
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 📊 Cuándo Usar Cada Una

```
┌─────────────────────────────────────────────────────────────────┐
│              CUÁNDO USAR CADA ANOTACIÓN                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  @Component                                                     │
│  ├── ✅ Helpers/Utilidades                                     │
│  ├── ✅ Converters                                            │
│  ├── ✅ Listeners de eventos                                  │
│  ├── ✅ Cuando ninguna otra semántica aplica                  │
│  └── ❌ NO para lógica de negocio                            │
│                                                                 │
│  @Service                                                      │
│  ├── ✅ TODA clase que contiene lógica de negocio            │
│  ├── ✅ Servicios de dominio                                  │
│  ├── ✅ Orquestación de repositorios                         │
│  └── ❌ NO para acceso directo a datos                       │
│                                                                 │
│  @Repository                                                   │
│  ├── ✅ Repositorios JPA (si no usas Spring Data)            │
│  ├── ✅ DAOs custom                                           │
│  └── ✅ Excepciones traducidas a DataAccessException         │
│                                                                 │
│  @Configuration                                                │
│  ├── ✅ Beans que necesitan configuración compleja           │
│  ├── ✅ Integración con sistemas externos                     │
│  ├── ✅ Crear beans de terceros                              │
│  └── ❌ NO para lógica de negocio                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

<a name="implementacion"></a>
## 5. Implementación de Servicios

### 💻 Paso 1: Crear el Servicio

```java
package com.product.app.service;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service  // ← Marca esta clase como servicio de Spring
public class ProductoService {

    // Inyección por constructor
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES CRUD
    // ═══════════════════════════════════════════════════════════════

    /**
     * Lista todos los productos
     * @return Lista de productos
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Crea un nuevo producto
     * @param producto Datos del producto
     * @return Producto creado con ID asignado
     */
    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productoActualizado Nuevos datos
     * @return Optional con el producto actualizado
     */
    public Optional<Producto> actualizar(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    return productoRepository.save(producto);
                });
    }

    /**
     * Elimina un producto
     * @param id ID del producto a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### 💻 Paso 2: Refactorizar el Controller

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

    // ✅ AHORA USAMOS SERVICE, NO REPOSITORY
    private final ProductoService productoService;

    // Spring inyecta automáticamente el servicio
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET /api/productos - Listar todos
    @GetMapping
    public List<Producto> listarProductos() {
        // Delegamos al servicio
        return productoService.listarTodos();
    }

    // GET /api/productos/{id} - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        // Delegamos al servicio
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/productos - Crear
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        // Delegamos al servicio
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(201).body(creado);
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

### 💻 Paso 3: Añadir Lógica de Negocio

```java
package com.product.app.service;

import com.product.app.model.Producto;
import com.product.app.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ═══════════════════════════════════════════════════════════════
    // LÓGICA DE NEGOCIO ADICIONAL
    // ═══════════════════════════════════════════════════════════════

    /**
     * Aplica un descuento a todos los productos
     * @param porcentajeDescuento Porcentaje de descuento (0-100)
     * @return Lista de productos con descuento aplicado
     */
    public List<Producto> aplicarDescuentoGeneral(double porcentajeDescuento) {
        List<Producto> productos = productoRepository.findAll();
        
        productos.forEach(producto -> {
            Double precioOriginal = producto.getPrecio();
            Double descuento = precioOriginal * (porcentajeDescuento / 100);
            Double precioFinal = precioOriginal - descuento;
            producto.setPrecio(precioFinal);
        });
        
        return productoRepository.saveAll(productos);
    }

    /**
     * Busca productos por rango de precio
     * @param precioMin Precio mínimo
     * @param precioMax Precio máximo
     * @return Lista de productos en el rango
     */
    public List<Producto> buscarPorRangoDePrecio(Double precioMin, Double precioMax) {
        return productoRepository.findAll().stream()
                .filter(p -> p.getPrecio() >= precioMin && p.getPrecio() <= precioMax)
                .toList();
    }

    /**
     * Calcula el valor total del inventario
     * @return Suma de precios de todos los productos
     */
    public Double calcularValorInventario() {
        return productoRepository.findAll().stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
    }

    /**
     * Valida reglas de negocio antes de guardar
     * @param producto Producto a validar
     * @throws IllegalArgumentException si la validación falla
     */
    public void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        
        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
        
        if (producto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
    }

    // CRUD básico
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        validarProducto(producto);  // ← Validamos antes de guardar
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(existente -> {
                    validarProducto(producto);
                    existente.setNombre(producto.getNombre());
                    existente.setDescripcion(producto.getDescripcion());
                    existente.setPrecio(producto.getPrecio());
                    return productoRepository.save(existente);
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

### 📊 Flujo Completo de una Petición

```
┌─────────────────────────────────────────────────────────────────┐
│            FLUJO COMPLETO: POST /api/productos                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Cliente                                                     │
│     │                                                          │
│     │ POST /api/productos                                      │
│     │ Content-Type: application/json                           │
│     │ {"nombre": "Laptop", "precio": 999.99}                   │
│     ▼                                                          │
│                                                                 │
│  2. DispatcherServlet (Spring MVC)                             │
│     │                                                          │
│     │ Recibe petición HTTP                                    │
│     │ Busca Controller con @RequestMapping("/api/productos")  │
│     ▼                                                          │
│                                                                 │
│  3. ProductoController                                          │
│     │                                                          │
│     │ Recibe Producto del body                                │
│     │ productoService.crear(producto)  ← Llama al servicio   │
│     ▼                                                          │
│                                                                 │
│  4. ProductoService                                             │
│     │                                                          │
│     │ validarProducto(producto)  ← Lógica de negocio         │
│     │ productoRepository.save(producto)  ← Acceso a datos    │
│     ▼                                                          │
│                                                                 │
│  5. ProductoRepository (JpaRepository)                         │
│     │                                                          │
│     │ Hibernate genera INSERT SQL                             │
│     │ Ejecuta en H2                                           │
│     ▼                                                          │
│                                                                 │
│  6. Response (vía reversa)                                     │
│     │                                                          │
│     │ 201 Created                                             │
│     │ {"id": 6, "nombre": "Laptop", "precio": 999.99}         │
│     ▼                                                          │
│                                                                 │
│  7. Cliente recibe respuesta                                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

<a name="refactorizacion-ia"></a>
## 6. Refactorización Asistida por IA

### 📖 ¿Qué es Refactorizar?

```
┌─────────────────────────────────────────────────────────────────┐
│                    ¿QUÉ ES REFACTORIZAR?                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  REFACTORIZAR = Cambiar la estructura del código               │
│                 SIN cambiar su comportamiento                  │
│                                                                 │
│  OBJETIVOS:                                                    │
│  ├── Mejorar legibilidad                                       │
│  ├── Facilitar mantenimiento                                  │
│  ├── Reducir complejidad                                      │
│  └── Preparar para futuras extensiones                         │
│                                                                 │
│  ⚠️ IMPORTANTE:                                                │
│  ├── No agregar funcionalidad nueva                           │
│  ├── No cambiar comportamiento                               │
│  └── Siempre tener tests antes de refactorizar               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 🔄 Tipos de Refactorización con IA

```
┌─────────────────────────────────────────────────────────────────┐
│              TIPOS DE REFACTORIZACIÓN CON IA                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1️⃣ DIVIDIR MÉTODOS COMPLEJOS                                  │
│     → "Divide este método en métodos más pequeños"              │
│                                                                 │
│  2️⃣ CREAR MÉTODOS REUTILIZABLES                               │
│     → "Extrae la lógica repetida a un método"                  │
│                                                                 │
│  3️⃣ RENOMBRAR CORRECTAMENTE                                    │
│     → "Renombra este método para que sea más descriptivo"      │
│                                                                 │
│  4️⃣ SIMPLIFICAR CONDICIONALES                                  │
│     → "Simplifica esta cadena de if-else"                     │
│                                                                 │
│  5️⃣ ELIMINAR CÓDIGO MUERTO                                     │
│     → "Identifica métodos no utilizados"                       │
│                                                                 │
│  6️⃣ APLICAR PATRONES DE DISEÑO                                │
│     → "Aplica el patrón Strategy a esta lógica"               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 💻 Ejemplo 1: Dividir Método Complejo

```java
// ═══════════════════════════════════════════════════════════════════
// ANTES: Método largo y difícil de leer
// ═══════════════════════════════════════════════════════════════════

public void procesarPedido(Pedido pedido) {
    // Validar cliente
    if (pedido.getCliente() == null) {
        throw new IllegalArgumentException("Cliente requerido");
    }
    if (pedido.getCliente().getEmail() == null || !pedido.getCliente().getEmail().contains("@")) {
        throw new IllegalArgumentException("Email inválido");
    }
    
    // Validar productos
    if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
        throw new IllegalArgumentException("Productos requeridos");
    }
    for (Producto p : pedido.getProductos()) {
        if (p.getCantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
        }
    }
    
    // Calcular total
    double total = 0;
    for (Producto p : pedido.getProductos()) {
        total += p.getPrecio() * p.getCantidad();
    }
    
    // Aplicar descuentos
    if (total > 1000) {
        total = total * 0.9; // 10% descuento
    } else if (total > 500) {
        total = total * 0.95; // 5% descuento
    }
    
    // Guardar pedido
    pedido.setTotal(total);
    pedido.setFecha(LocalDateTime.now());
    pedidoRepository.save(pedido);
    
    // Enviar email
    emailService.enviarConfirmacion(pedido.getCliente().getEmail(), pedido);
}


// ═══════════════════════════════════════════════════════════════════
// DESPUÉS: Dividido en métodos pequeños
// ═══════════════════════════════════════════════════════════════════

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EmailService emailService;

    public PedidoService(PedidoRepository pedidoRepository, EmailService emailService) {
        this.pedidoRepository = pedidoRepository;
        this.emailService = emailService;
    }

    public void procesarPedido(Pedido pedido) {
        validarPedido(pedido);
        double total = calcularTotal(pedido);
        guardarPedido(pedido, total);
        notificarCliente(pedido);
    }

    private void validarPedido(Pedido pedido) {
        validarCliente(pedido.getCliente());
        validarProductos(pedido.getProductos());
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente requerido");
        }
        if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    private void validarProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            throw new IllegalArgumentException("Productos requeridos");
        }
        for (Producto p : productos) {
            if (p.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
            }
        }
    }

    private double calcularTotal(Pedido pedido) {
        double subtotal = pedido.getProductos().stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();
        return aplicarDescuentos(subtotal);
    }

    private double aplicarDescuentos(double total) {
        if (total > 1000) {
            return total * 0.9;
        } else if (total > 500) {
            return total * 0.95;
        }
        return total;
    }

    private void guardarPedido(Pedido pedido, double total) {
        pedido.setTotal(total);
        pedido.setFecha(LocalDateTime.now());
        pedidoRepository.save(pedido);
    }

    private void notificarCliente(Pedido pedido) {
        emailService.enviarConfirmacion(
            pedido.getCliente().getEmail(), 
            pedido
        );
    }
}
```

### 💻 Ejemplo 2: Renombrar con IA

```
┌─────────────────────────────────────────────────────────────────┐
│              RENOMBRAR CON IA                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ANTES (Poco descriptivo):                                      │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  public void proc(Pedido p) {                           │   │
│  │      // hace algo                                        │   │
│  │  }                                                       │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                 │
│  💬 Prompt para IA:                                            │
│  "Renombra este método para que sea más descriptivo"           │
│                                                                 │
│  DESPUÉS (Descriptivo):                                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  public void procesarPedido(Pedido pedido) {             │   │
│  │      // hace algo                                        │   │
│  │  }                                                       │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 💻 Ejemplo 3: Extraer Método Reutilizable

```java
// ═══════════════════════════════════════════════════════════════════
// ANTES: Lógica duplicada
// ═══════════════════════════════════════════════════════════════════

public void crearProducto(Producto p) {
    // Validar precio
    if (p.getPrecio() < 0) {
        throw new IllegalArgumentException("Precio negativo");
    }
    // ... resto del código
}

public void actualizarProducto(Long id, Producto p) {
    // Validar precio (¡DUPLICADO!)
    if (p.getPrecio() < 0) {
        throw new IllegalArgumentException("Precio negativo");
    }
    // ... resto del código
}


// ═══════════════════════════════════════════════════════════════════
// DESPUÉS: Método reutilizable
// ═══════════════════════════════════════════════════════════════════

@Service
public class ProductoService {

    private final ProductoRepository repository;

    // ═══════════════════════════════════════════════════════════════
    // MÉTODO REUTILIZABLE PARA VALIDAR PRECIO
    // ═══════════════════════════════════════════════════════════════
    
    private void validarPrecio(Double precio) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio es requerido");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    public void crearProducto(Producto p) {
        validarPrecio(p.getPrecio());  // ← Reutilizamos
        repository.save(p);
    }

    public void actualizarProducto(Long id, Producto p) {
        validarPrecio(p.getPrecio());  // ← Reutilizamos
        repository.save(p);
    }
}
```

### 💡 Prompts Útiles para Refactorizar con IA

```
┌─────────────────────────────────────────────────────────────────┐
│              PROMPTS PARA REFACTORIZACIÓN CON IA               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  📝 "Divide este método en métodos más pequeños"                │
│                                                                 │
│  📝 "Extrae la lógica repetida a un método privado"             │
│                                                                 │
│  📝 "Renombra este método para que sea más descriptivo"        │
│                                                                 │
│  📝 "Agrega documentación Javadoc a esta clase"                 │
│                                                                 │
│  📝 "Simplifica este try-catch usando try-with-resources"      │
│                                                                 │
│  📝 "Convierte esta cadena if-else a switch"                   │
│                                                                 │
│  📝 "Usa Optional en lugar de null"                            │
│                                                                 │
│  📝 "Aplica el principio de responsabilidad única a esta clase"│
│                                                                 │
│  📝 "Agrega validación de null checks a este método"           │
│                                                                 │
│  📝 "Refactoriza para usar streams en lugar de for loops"      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### ⚠️ Precauciones al Refactorizar con IA

```
┌─────────────────────────────────────────────────────────────────┐
│              ⚠️ PRECAUCIONES IMPORTANTES                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✅ SIEMPRE:                                                   │
│  ├── Revisar el código generado por IA                        │
│  ├── Entender qué hace cada cambio                            │
│  ├── Ejecutar tests después de refactorizar                   │
│  └── Hacer commits pequeños y frecuentes                       │
│                                                                 │
│  ❌ NUNCA:                                                     │
│  ├── Copiar y pegar sin entender                              │
│  ├── Refactorizar todo de golpe                               │
│  ├── Olvidar hacer backup (git)                               │
│  └── Ignorar errores de compilación                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

<a name="conclusiones"></a>
## 7. Conclusiones y Ejercicios

### ✅ Resumen de lo Aprendido

```
┌─────────────────────────────────────────────────────────────────┐
│                    🎯 RESUMEN DE LA CLASE 3                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✅ Inversión de Dependencias                                  │
│     → Constructor Injection > Setter > Field                   │
│                                                                 │
│  ✅ Patrón Service Layer                                       │
│     → Separación Controller ↔ Service ↔ Repository            │
│                                                                 │
│  ✅ @Component, @Service, @Configuration                      │
│     → Cuándo usar cada uno                                    │
│                                                                 │
│  ✅ Implementación de Servicios                                │
│     → ProductoService con lógica de negocio                    │
│                                                                 │
│  ✅ Refactorización con IA                                    │
│     → Dividir, extraer, renombrar                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📁 Estructura Final del Proyecto

```
src/main/java/com/product/app/
├── AppApplication.java
├── controller/
│   └── ProductoController.java     ← Receives HTTP requests
├── service/
│   └── ProductoService.java        ← ✨ Lógica de negocio ✨
├── repository/
│   └── ProductoRepository.java     ← Acceso a datos
├── model/
│   └── Producto.java               ← Entidad
└── dto/
    └── ErrorRespuesta.java        ← DTO de errores
```

### 📋 Checklist de Implementación

```
┌─────────────────────────────────────────────────────────────────┐
│              CHECKLIST ANTES DE MERGE                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  CÓDIGO                                                        │
│  ├── [ ] @Service creado para Producto                        │
│  ├── [ ] Controller usa ProductoService                       │
│  ├── [ ] Constructor injection en ambos                       │
│  ├── [ ] Lógica de negocio en Service                         │
│  └── [ ] Repository solo en Service                           │
│                                                                 │
│  CALIDAD                                                       │
│  ├── [ ] Métodos con Javadoc                                  │
│  ├── [ ] Nombres descriptivos                                 │
│  ├── [ ] Sin código duplicado                                 │
│  └── [ ] Tests pasando                                        │
│                                                                 │
│  GIT                                                           │
│  ├── [ ] Commits atómicos                                     │
│  └── [ ] Mensajes descriptivos                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📝 Ejercicios Prácticos

#### Ejercicio 1: Crear ProductoService
```
1. Crea una nueva clase ProductoService en el paquete service
2. Mueve la lógica de negocio del Controller al Service
3. Refactoriza el Controller para usar el Service
4. Ejecuta la aplicación y verifica que todo funciona
```

#### Ejercicio 2: Añadir Método de Descuento
```
1. En ProductoService, crea un método aplicarDescuento(Long id, double porcentaje)
2. El método debe buscar el producto, calcular el nuevo precio y guardarlo
3. Crea un endpoint POST /api/productos/{id}/descuento?porcentaje=10
4. Prueba con curl
```

#### Ejercicio 3: Refactorizar con IA
```
1. Toma un método largo de tu código
2. Usa Cursor AI o ChatGPT para dividirlo
3. Compara el código antes y después
4. Verifica que sigue funcionando igual
```

#### Ejercicio 4: Validación en Service
```
1. Añade un método validarProducto(Producto p) en ProductoService
2. Valida: nombre no vacío, precio no negativo
3. Usa esta validación antes de crear y actualizar
4. Maneja IllegalArgumentException con try-catch en el Controller
```

### ❓ Preguntas de Autoevaluación

1. **¿Cuál es la diferencia entre @Component y @Service?**
2. **¿Por qué Constructor Injection es preferible a Field Injection?**
3. **¿Cuál es la responsabilidad de la capa de Servicio?**
4. **¿Qué debemos hacer antes de refactorizar?**
5. **¿Puedo acceder directamente al Repository desde el Controller?**

---

### 🔗 Recursos Útiles

| Recurso | URL |
|---------|-----|
| Spring DI Documentation | https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans |
| Baeldung - Spring DI | https://www.baeldung.com/spring-dependency-injection |
| Martin Fowler - Refactoring | https://refactoring.com/ |

---

*¡Excelente trabajo! Has completado la Clase 3.* 🎉

*Autor: Jarolin Matos Martinez*

**Practica mucho y nos vemos en la próxima clase.**
