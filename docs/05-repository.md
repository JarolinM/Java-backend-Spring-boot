# 📦 Repository y Acceso a Datos

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, entenderás qué es un Repository en Spring Data, cómo JpaRepository maneja todas las operaciones CRUD, y cómo usar Query Methods para consultas personalizadas.

---

## 📚 Teoría

### ¿Qué es un Repository?

Un **Repository** es un patrón de diseño que **centraliza el acceso a los datos**. Actúa como una capa intermedia entre el código de negocio y la base de datos.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          Repository Pattern                                  │
│                                                                              │
│    Controller                      Repository                Base de Datos │
│        │                               │                          │         │
│        │  "Necesito todos              │                          │         │
│        │   los productos"              │                          │         │
│        │───────────────>                │                          │         │
│        │                               │                          │         │
│        │                               │  "SELECT * FROM          │         │
│        │                               │   PRODUCTO"              │         │
│        │                               │───────────────>           │         │
│        │                               │                          │         │
│        │                               │  [resultados]            │         │
│        │                               │<──────────────            │         │
│        │  [Lista<Producto>]             │                          │         │
│        │<───────────────                │                          │         │
│        │                               │                          │         │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ¿Qué es JpaRepository?

**JpaRepository** es una interfaz de Spring Data que proporciona **todas las operaciones CRUD** sin necesidad de escribir código SQL.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      JPA REPOSITORY HIERARCHY                                │
│                                                                              │
│  ┌─────────────────────────────┐                                            │
│  │     Repository<T, ID>        │  ← Interfaz base (vacío)                   │
│  │     (Spring Data)            │                                            │
│  └──────────────┬───────────────┘                                            │
│                 │                                                            │
│                 ▼                                                            │
│  ┌─────────────────────────────┐                                            │
│  │   CrudRepository<T, ID>     │  ← save, find, delete (básico)            │
│  │   (Spring Data)              │                                            │
│  └──────────────┬───────────────┘                                            │
│                 │                                                            │
│                 ▼                                                            │
│  ┌─────────────────────────────┐                                            │
│  │   PagingAndSortingRepo      │  ← + paginación y ordenamiento             │
│  │   (Spring Data)              │                                            │
│  └──────────────┬───────────────┘                                            │
│                 │                                                            │
│                 ▼                                                            │
│  ┌─────────────────────────────┐                                            │
│  │   JpaRepository<T, ID>      │  ← + flush, batch,@Transactional           │
│  │   (Spring Data JPA)         │                                            │
│  └─────────────────────────────┘                                            │
│                                                                              │
│  └───────────────────────────────────────────────────────────────────────────│
│  Tu Repository                                                                 │
│  ┌─────────────────────────────┐                                            │
│  │ ProductoRepository           │  ← Extiende JpaRepository<Producto, Long>  │
│  │ extends JpaRepository        │                                            │
│  └─────────────────────────────┘                                            │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Métodos Heredados de JpaRepository

JpaRepository proporciona **automaticamente** estos métodos:

| Método | Descripción | Retorna |
|--------|-------------|---------|
| `findAll()` | Obtiene todos los registros | `List<T>` |
| `findById(id)` | Busca por ID específico | `Optional<T>` |
| `save(entity)` | Guarda o actualiza | `T` |
| `saveAll(entities)` | Guarda varios | `List<T>` |
| `deleteById(id)` | Elimina por ID | `void` |
| `delete(entity)` | Elimina la entidad | `void` |
| `count()` | Cuenta registros | `long` |
| `existsById(id)` | Verifica si existe | `boolean` |

### Query Methods (Spring Data Magic)

Los **Query Methods** son métodos que Spring genera automáticamente basándose en el **nombre del método**. ¡No necesitas escribir SQL!

#### Estructura del nombre:

```
findBy + NombreCampo + Condición
    │          │           │
    │          │           └── GreaterThan, LessThan, Like, Equals...
    │          │
    │          └── Nombre del campo en camelCase
    │
    └── Método de búsqueda
```

#### Ejemplos de Query Methods:

```java
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Spring genera: SELECT * FROM PRODUCTO WHERE NOMBRE = ?
    Producto findByNombre(String nombre);
    
    // Spring genera: SELECT * FROM PRODUCTO WHERE PRECIO > ?
    List<Producto> findByPrecioGreaterThan(Double precio);
    
    // Spring genera: SELECT * FROM PRODUCTO WHERE NOMBRE LIKE '%texto%'
    List<Producto> findByNombreContaining(String texto);
    
    // Spring genera: SELECT * FROM PRODUCTO WHERE NOMBRE = ? AND PRECIO > ?
    List<Producto> findByNombreAndPrecioGreaterThan(String nombre, Double precio);
    
    // Spring genera: SELECT * FROM PRODUCTO ORDER BY PRECIO ASC
    List<Producto> findAllByOrderByPrecioAsc();
}
```

#### Tabla de Keywords de Query Methods:

| Keyword | Ejemplo | SQL Generado |
|---------|---------|--------------|
| `findBy` | `findByNombre(String n)` | WHERE nombre = ? |
| `findAllBy` | `findAllByPrecio(Double p)` | WHERE precio = ? |
| `ExistsBy` | `existsByEmail(String e)` | WHERE email = ? |
| `CountBy` | `countByActivo(Boolean a)` | COUNT(*) WHERE activo = ? |
| `DeleteBy` | `deleteById(Long id)` | DELETE WHERE id = ? |
| **Condiciones** | | |
| `GreaterThan` | `findByPrecioGreaterThan(100)` | WHERE precio > 100 |
| `LessThan` | `findByPrecioLessThan(100)` | WHERE precio < 100 |
| `Between` | `findByPrecioBetween(10, 100)` | WHERE precio BETWEEN 10 AND 100 |
| `Like` | `findByNombreLike("Lap%")` | WHERE nombre LIKE 'Lap%' |
| `Containing` | `findByNombreContaining("Lap")` | WHERE nombre LIKE '%Lap%' |
| `StartingWith` | `findByNombreStartingWith("Lap")` | WHERE nombre LIKE 'Lap%' |
| `EndingWith` | `findByNombreEndingWith("top")` | WHERE nombre LIKE '%top' |
| **Lógicos** | | |
| `And` | `findByNombreAndPrecio(...)` | WHERE nombre = ? AND precio = ? |
| `Or` | `findByNombreOrPrecio(...)` | WHERE nombre = ? OR precio = ? |
| `Not` | `findByNombreNot("test")` | WHERE nombre <> 'test' |
| **Ordenamiento** | | |
| `OrderBy` | `findByPrecioOrderByNombreAsc(...)` | ORDER BY nombre ASC |

---

## 💻 Código: ProductoRepository.java con Comentarios

```java
package com.ejemplo.productoapi.repository;

import com.ejemplo.productoapi.model.Producto;  // Tu entidad
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ═══════════════════════════════════════════════════════════════════════════
// ANOTACIONES
// ═══════════════════════════════════════════════════════════════════════════

@Repository  // ← Marca esta interfaz como componente de acceso a datos
             //   (Opcional en Spring Data, pero buena práctica)

// ═══════════════════════════════════════════════════════════════════════════
// INTERFAZ: JpaRepository<Producto, Long>
// ═══════════════════════════════════════════════════════════════════════════
//
//   Producto  = Tipo de entidad que maneja
//   Long      = Tipo de dato de la llave primaria (id)
//
//   Al extender JpaRepository, heredas AUTOMÁTICAMENTE:
//   - findAll()     → SELECT * FROM PRODUCTO
//   - findById(id)  → SELECT * FROM PRODUCTO WHERE ID = ?
//   - save(p)       → INSERT o UPDATE PRODUCTO
//   - deleteById(id)→ DELETE FROM PRODUCTO WHERE ID = ?
//   - count()       → SELECT COUNT(*) FROM PRODUCTO
//
// ═══════════════════════════════════════════════════════════════════════════

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // ═══════════════════════════════════════════════════════════════════════
    // QUERY METHODS: Spring genera el SQL automáticamente
    // ═══════════════════════════════════════════════════════════════════════
    
    // Busca productos cuyo nombre SEA EXACTO
    // SQL: SELECT * FROM PRODUCTO WHERE NOMBRE = ?
    Producto findByNombre(String nombre);
    
    // Busca productos cuyo precio SEA MAYOR QUE...
    // SQL: SELECT * FROM PRODUCTO WHERE PRECIO > ?
    List<Producto> findByPrecioGreaterThan(Double precio);
    
    // Busca productos cuyo nombre CONTENGA el texto
    // SQL: SELECT * FROM PRODUCTO WHERE NOMBRE LIKE '%?%'
    List<Producto> findByNombreContaining(String texto);
    
    // Busca productos donde nombre Y precio cumplan condiciones
    // SQL: SELECT * FROM PRODUCTO WHERE NOMBRE = ? AND PRECIO > ?
    List<Producto> findByNombreAndPrecioGreaterThan(
        String nombre, 
        Double precio
    );
    
    // Busca productos ordenados por precio ASCENDENTE
    // SQL: SELECT * FROM PRODUCTO ORDER BY PRECIO ASC
    List<Producto> findAllByOrderByPrecioAsc();
    
    // Verifica si existe un producto con ese nombre
    // SQL: SELECT COUNT(*) > 0 FROM PRODUCTO WHERE NOMBRE = ?
    boolean existsByNombre(String nombre);
    
    // Cuenta cuántos productos hay con precio menor a...
    long countByPrecioLessThan(Double precio);
}
```

---

## 📊 Diagrama: Flujo Controller → Repository → BD

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              FLUJO: Controller → Repository → JPA → Base de Datos             │
│                                                                              │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                     PRODUCTOCONTROLLER                                │    │
│  │                                                                      │    │
│  │  @GetMapping                                                         │    │
│  │  public List<Producto> listaProductos() {                            │    │
│  │      return productoRepository.findAll();  // ← Llamada al repo    │    │
│  │  }                                                                   │    │
│  └─────────────────────────────┬───────────────────────────────────────┘    │
│                                │                                              │
│                                │ productoRepository.findAll()                 │
│                                ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    PRODUCTOREPOSITORY                               │    │
│  │                                                                      │    │
│  │  Interfaz que extiende JpaRepository<Producto, Long>                │    │
│  │                                                                      │    │
│  │  + findAll(): List<Producto>      ← Ya viene incluido              │    │
│  │  + findById(id): Optional         ← Ya viene incluido              │    │
│  │  + save(producto): Producto      ← Ya viene incluido              │    │
│  │  + deleteById(id): void           ← Ya viene incluido              │    │
│  │                                                                      │    │
│  │  + findByNombre(nombre) ← ¡Nuevo! Spring genera el SQL             │    │
│  │  + findByPrecioGreaterThan(p) ← ¡Nuevo!                            │    │
│  └─────────────────────────────┬───────────────────────────────────────┘    │
│                                │                                              │
│                                │ Spring Data JPA genera implementación        │
│                                ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    JPA (HIBERNATE)                                  │    │
│  │                                                                      │    │
│  │  Convierte las llamadas a SQL específico de la base de datos:      │    │
│  │                                                                      │    │
│  │  findAll()           →  SELECT * FROM PRODUCTO                      │    │
│  │  findById(1)         →  SELECT * FROM PRODUCTO WHERE ID = 1         │    │
│  │  save(producto)      →  INSERT INTO PRODUCTO VALUES(...)             │    │
│  │  deleteById(1)       →  DELETE FROM PRODUCTO WHERE ID = 1            │    │
│  │  findByNombre("USB") →  SELECT * FROM PRODUCTO WHERE NOMBRE = 'USB'│    │
│  └─────────────────────────────┬───────────────────────────────────────┘    │
│                                │                                              │
│                                ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                         H2 DATABASE                                 │    │
│  │                                                                      │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │                        PRODUCTO                              │   │    │
│  │  ├─────────────────────────────────────────────────────────────┤   │    │
│  │  │  ID  │  NOMBRE    │  DESCRIPCION       │  PRECIO            │   │    │
│  │  ├──────┼───────────┼────────────────────┼────────────────────┤   │    │
│  │  │  1   │  Laptop    │  16GB RAM          │  999.99            │   │    │
│  │  │  2   │  Mouse     │  Inalámbrico       │  29.99             │   │    │
│  │  │  3   │  Teclado   │  Mecánico          │  79.99             │   │    │
│  │  └──────┴───────────┴────────────────────┴────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Tabla de Métodos Comunes de JpaRepository

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                    MÉTODOS DE JpaRepository                                     │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  OPERACIONES DE LECTURA                                                       │
│  ─────────────────────────                                                    │
│  ┌─────────────────────────────────┬────────────────────────────────────────┐  │
│  │ Método                          │ Descripción                           │  │
│  ├─────────────────────────────────┼────────────────────────────────────────┤  │
│  │ findAll()                      │ Obtiene TODOS los registros            │  │
│  │ findAllById(ids)               │ Obtiene varios por IDs                │  │
│  │ findById(id)                   │ Obtiene UNO por ID (Optional)          │  │
│  │ findAll(Sort)                 │ Obtiene todos ORDENADOS               │  │
│  │ findAll(Pageable)             │ Obtiene página de resultados          │  │
│  │ getReferenceById(id)          │ Obtiene proxy (no consulta BD aún)    │  │
│  └─────────────────────────────────┴────────────────────────────────────────┘  │
│                                                                              │
│  OPERACIONES DE ESCRITURA                                                     │
│  ─────────────────────────                                                    │
│  ┌─────────────────────────────────┬────────────────────────────────────────┐  │
│  │ Método                          │ Descripción                           │  │
│  ├─────────────────────────────────┼────────────────────────────────────────┤  │
│  │ save(entity)                   │ Guarda NUEVO o ACTUALIZA existente    │  │
│  │ saveAll(entities)              │ Guarda varios en batch               │  │
│  │ flush()                        │ Fuerza ejecución pendiente            │  │
│  │ deleteById(id)                 │ Elimina por ID                        │  │
│  │ delete(entity)                 │ Elimina la entidad específica        │  │
│  │ deleteAll()                    │ Elimina TODOS                         │  │
│  │ deleteAllInBatch(entities)     │ Elimina varios (sin events)          │  │
│  └─────────────────────────────────┴────────────────────────────────────────┘  │
│                                                                              │
│  OPERACIONES DE VERIFICACIÓN                                                 │
│  ─────────────────────────                                                    │
│  ┌─────────────────────────────────┬────────────────────────────────────────┐  │
│  │ Método                          │ Descripción                           │  │
│  ├─────────────────────────────────┼────────────────────────────────────────┤  │
│  │ existsById(id)                 │ ¿Existe el registro? (boolean)        │  │
│  │ count()                        │ ¿Cuántos registros hay? (long)        │  │
│  │ countByCampo(valor)            │ ¿Cuántos con condición?                │  │
│  └─────────────────────────────────┴────────────────────────────────────────┘  │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **Repository is null** | Falta `@Autowired` | Añadir en el controller |
| 2 | **findById devuelve Optional** | Necesitas manejar el Optional | Usar `.orElse()` o `.orElseThrow()` |
| 3 | **No property found for type** | Nombre de campo incorrecto | Verificar que el nombre coincida EXACTAMENTE |
| 4 | **Ambiguous method** | Dos métodos similares | Cambiar el nombre o añadir más condiciones |
| 5 | **Query investigation failed** | Nombre de método no válido | Usar palabras clave correctas |
| 6 | **Save no persiste** | No hay `@Transactional` implícito | JpaRepository ya lo tiene para save() |

---

## 💡 Tips

### 1. Optional y cómo manejarlo

```java
// ✗ INCORRECTO - No verifica si existe
Producto p = productoRepository.findById(id);
p.setNombre("Nuevo");  // NullPointerException si no existe

// ✓ CORRECTO - Con Optional
Optional<Producto> opt = productoRepository.findById(id);
if (opt.isPresent()) {
    Producto p = opt.get();
    p.setNombre("Nuevo");
}

// ✓ MEJOR - Con lambda
productoRepository.findById(id)
    .ifPresent(p -> p.setNombre("Nuevo"));

// ✓ BEST - Con orElseThrow (lanza excepción si no existe)
Producto p = productoRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
```

### 2. Save: Insert vs Update

```java
// Si id es NULL → INSERT (crea nuevo)
producto.setId(null);  // O no poner id
repository.save(producto);  // INSERT

// Si id EXISTE en BD → UPDATE (actualiza existente)
producto.setId(1L);
repository.save(producto);  // UPDATE

// No sabes si es insert o update → Save maneja ambos
```

### 3. Nombres de campos en Query Methods

```java
// El nombre del método debe coincidir EXACTAMENTE con el campo

@Entity
public class Producto {
    private String nombreCompleto;  // camelCase
}

// ✓ CORRECTO
List<Producto> findByNombreCompleto(String nombre);

// ✗ INCORRECTO - Buscará "Nombrecompleto" que no existe
List<Producto> findByNombrecompleto(String nombre);
```

### 4. Relaciones entre entidades (adelanto)

En el futuro, podrás hacer consultas como:

```java
// Si Producto tiene una relación con Categoria:
List<Producto> findByCategoriaNombre(String nombreCategoria);

// SQL generado:
// SELECT * FROM PRODUCTO p 
// JOIN CATEGORIA c ON p.categoria_id = c.id 
// WHERE c.NOMBRE = ?
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué interfaz proporciona JpaRepository?

- [ ] A) Repository<T, ID>
- [ ] B) CrudRepository<T, ID>
- [ ] C) PagingAndSortingRepository<T, ID>
- [ ] D) Todas las anteriores

### Pregunta 2:
¿Qué método devuelve `Optional<T>`?

- [ ] A) findAll()
- [ ] B) findById(id)
- [ ] C) save(entity)
- [ ] D) count()

### Pregunta 3:
¿Qué Query Method busca por nombre exacto?

- [ ] A) findByNombreLike()
- [ ] B) findByNombreContaining()
- [ ] C) findByNombre()
- [ ] D) findByNombreEquals()

### Pregunta 4:
¿Qué significa que JpaRepository herede métodos?

- [ ] A) Debes implementarlos todos
- [ ] B) Spring genera automáticamente la implementación
- [ ] C) No puedes usarlos
- [ ] D) Solo funcionan en ciertos casos

### Pregunta 5:
¿Qué método usarías para verificar si existe un producto?

- [ ] A) findById()
- [ ] B) count()
- [ ] C) existsById()
- [ ] D) getById()

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Añade un Query Method

En `ProductoRepository.java`, añade un método para:
- Buscar productos por descripción que CONTENGA un texto
- ¿Qué keyword necesitas?

### Ejercicio 2: Maneja el Optional

Escribe código que:
1. Busque un producto por ID = 1
2. Si existe, imprima su nombre
3. Si no existe, imprima "Producto no encontrado"

### Ejercicio 3: Crea el repository

Crea un `ClienteRepository` para una entidad `Cliente` con:
- `id` (Long)
- `nombre` (String)
- `email` (String)
- `telefono` (String)

El repository debe:
- Extenderse de JpaRepository
- Tener un método para buscar por email
- Tener un método para buscar por nombre que contenga texto

### Ejercicio 4: Investigando más Query Methods

Investiga qué Query Method usarías para:
1. Buscar entre un rango de precios (entre 10 y 100)
2. Buscar productos que contengan "lap" en el nombre Y tengan precio mayor a 500
3. Contar cuántos productos tienen stock mayor a 0

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
