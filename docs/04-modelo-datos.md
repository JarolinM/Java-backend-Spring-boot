# 💾 Modelo de Datos con JPA

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, entenderás qué es JPA, cómo mapear una clase Java a una tabla de base de datos, y cómo Spring Boot maneja la persistencia de datos automáticamente.

---

## 📚 Teoría

### ¿Qué es JPA?

**JPA** (Java Persistence API) es una **especificación estándar** de Java para manejar datos persistentes. Piensa en él como el "traductor" entre objetos Java y tablas de base de datos.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           JPA (Java Persistence API)                         │
│                                                                              │
│   Java Objects                    JPA                    Tablas SQL          │
│                                                                              │
│   ┌──────────────┐                                    ┌──────────────┐       │
│   │  Producto    │                                    │  PRODUCTO    │       │
│   │──────────────│         ┌─────────────┐          │──────────────│       │
│   │ Long id      │◀═══════▶│  Hibernate  │◀═════════▶│ ID           │       │
│   │ String nombre│         │  (provider)  │          │ NOMBRE       │       │
│   │ Double precio│         └─────────────┘          │ DESCRIPCION  │       │
│   └──────────────┘                                    │ PRECIO       │       │
│                                                       └──────────────┘       │
│                                                                              │
│   Mapping: "Esta clase Java representa esa tabla SQL"                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Nota importante:** JPA es solo una **especificación**. Para usarla necesitamos una **implementación**, y en Spring Boot la más común es **Hibernate**.

### ¿Qué es Hibernate?

**Hibernate** es la implementación más popular de JPA. Cuando Spring Boot dice "JPA", en realidad usa Hibernate por debajo.

```
JPA (Interfaz/Especificación)
    │
    ├── Implementación 1: Hibernate (más popular)
    ├── Implementación 2: EclipseLink
    └── Implementación 3: OpenJPA
```

### La Anotación @Entity

`@Entity` es la anotación que **marca una clase como una entidad JPA**. Esto significa que Spring creará una tabla correspondiente en la base de datos.

```java
@Entity  // ← Esta clase se mapea a una tabla
public class Producto {
    // campos...
}
```

**Reglas importantes:**
1. La clase debe tener un **constructor sin argumentos** (protected o public)
2. Debe tener un campo marcado con `@Id`
3. La clase no debe ser `final`

### Las Anotaciones @Id y @GeneratedValue

**@Id** marca el campo como la **llave primaria** de la tabla:

```java
@Id
private Long id;  // Este campo es la PK
```

**@GeneratedValue** indica que el ID se **genera automáticamente**:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

#### Estrategias de generación:

| Estrategia | Descripción | Cuándo usarla |
|------------|-------------|---------------|
| `IDENTITY` | Auto-increment en la BD | MySQL, SQL Server, H2 |
| `SEQUENCE` | Secuencia de la BD | PostgreSQL, Oracle |
| `TABLE` | Tabla especial de secuencias | Cualquier BD |
| `AUTO` | Spring decide (default) | Cuando no importa |

### Tipos de Datos Java vs SQL

Spring Boot/JPA convierten automáticamente entre tipos Java y tipos SQL:

```
┌────────────────────────┬────────────────────────┬────────────────────────┐
│   Tipo Java            │   Tipo SQL              │   Notas                 │
├────────────────────────┼────────────────────────┼────────────────────────┤
│   String               │   VARCHAR / TEXT        │   Sin límite           │
│   Integer / int        │   INTEGER / INT         │   32 bits               │
│   Long / long          │   BIGINT                │   64 bits               │
│   Double / double      │   DOUBLE                │   Decimal 8 bytes      │
│   Float / float        │   FLOAT                 │   Decimal 4 bytes      │
│   BigDecimal           │   DECIMAL               │   Preciso para dinero  │
│   Boolean / boolean    │   BOOLEAN / BIT         │   true/false           │
│   LocalDate            │   DATE                  │   Solo fecha            │
│   LocalDateTime        │   TIMESTAMP             │   Fecha y hora         │
│   LocalTime            │   TIME                  │   Solo hora             │
│   byte[]               │   BLOB                  │   Datos binarios        │
└────────────────────────┴────────────────────────┴────────────────────────┘
```

**Recomendación para dinero:** Usa siempre `BigDecimal` en lugar de `Double`:

```java
// ✓ CORRECTO para dinero
private BigDecimal precio;

// ✗ EVITAR para dinero (problemas de precisión)
private Double precio;
```

---

## 💻 Código: Producto.java con Comentarios

```java
package com.ejemplo.productoapi.model;

// ═══════════════════════════════════════════════════════════════════════════
// IMPORTACIONES NECESARIAS
// ═══════════════════════════════════════════════════════════════════════════

import jakarta.persistence.Entity;              // Marca esta clase como entidad JPA
import jakarta.persistence.Table;             // Define el nombre de la tabla
import jakarta.persistence.Id;                 // Marca la llave primaria
import jakarta.persistence.GeneratedValue;     // Genera el ID automáticamente
import jakarta.persistence.GenerationType;      // Estrategia de generación

// ═══════════════════════════════════════════════════════════════════════════
// ANOTACIONES DE CLASE
// ═══════════════════════════════════════════════════════════════════════════

@Entity                     // ← JPA crea una tabla para esta clase
@Table(name = "PRODUCTO")   // ← Nombre exacto de la tabla en la BD
public class Producto {      // ← Nombre de la clase = nombre de la tabla (por defecto)
    
    // ═══════════════════════════════════════════════════════════════════════
    // CAMPO: ID (Llave Primaria)
    // ═══════════════════════════════════════════════════════════════════════
    
    @Id                                             // ← Este campo es la PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← Se auto-genera
    private Long id;                                // ← Tipo Long (soporta más registros)

    // ═══════════════════════════════════════════════════════════════════════
    // CAMPOS: Datos del producto
    // ═══════════════════════════════════════════════════════════════════════
    
    private String nombre;         // → Se mapea a columna NOMBRE
    private String descripcion;    // → Se mapea a columna DESCRIPCION
    private Double precio;         // → Se mapea a columna PRECIO
    
    // ═══════════════════════════════════════════════════════════════════════
    // CONSTRUCTORES
    // ═══════════════════════════════════════════════════════════════════════
    
    // Constructor SIN argumentos (REQUERIDO por JPA)
    // Sin este constructor, Spring NO puede crear instancias
    public Producto() {
    }
    
    // Constructor CON argumentos (para facilidad de uso)
    public Producto(String nombre, String descripcion, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }
    
    // ═══════════════════════════════════════════════════════════════════════
    // GETTERS Y SETTERS
    // ═══════════════════════════════════════════════════════════════════════
    
    // JPA usa los getters para LEER datos de la BD
    public Long getId() {
        return id;
    }
    
    // JPA usa el setter para ESCRIBIR datos en la BD
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

---

## 📊 Diagrama: Clase Java ↔ Tabla SQL

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      MAPPING: Java ↔ SQL                                     │
│                                                                              │
│  ┌─────────────────────────────────┐         mapea a        ┌───────────────┴───────┐
│  │       Producto.java             │ ──────────────────────▶│      PRODUCTO          │
│  ├─────────────────────────────────┤                       ├───────────────────────┤
│  │                                 │                       │                       │
│  │  @Id                            │                       │  ID                    │
│  │  @GeneratedValue                │ ─────────────────────▶│  BIGINT (PK, AUTO)     │
│  │  private Long id;              │                       │                       │
│  │                                 │                       │                       │
│  │  private String nombre;         │ ─────────────────────▶│  NOMBRE                │
│  │                                 │                       │  VARCHAR               │
│  │  private String descripcion;    │ ─────────────────────▶│  DESCRIPCION           │
│  │                                 │                       │  VARCHAR               │
│  │  private Double precio;         │ ─────────────────────▶│  PRECIO                │
│  │                                 │                       │  DOUBLE                │
│  │                                 │                       │                       │
│  └─────────────────────────────────┘                       └───────────────────────┘
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Tabla de Tipos de Datos Completa

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                    TIPOS DE DATOS: Java ↔ JPA ↔ SQL                          │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  JAVA / JAVA (JPA)              SQL (H2/MySQL/PostgreSQL)                    │
│  ────────────────────────────────────────────────────────────────────────────│
│                                                                              │
│  String                         VARCHAR (H2/MySQL) / TEXT (PostgreSQL)       │
│  Integer / int                  INTEGER / INT                                │
│  Long / long                    BIGINT                                        │
│  Double / double                DOUBLE / DOUBLE PRECISION                    │
│  Float / float                  FLOAT                                         │
│  BigDecimal                     DECIMAL(p,s)                                  │
│  Boolean / boolean              BOOLEAN / BIT                                 │
│  Byte / byte                    TINYINT                                       │
│  Short / short                  SMALLINT                                      │
│  Character / char               CHAR(1)                                       │
│  LocalDate                      DATE                                          │
│  LocalDateTime                  TIMESTAMP / DATETIME                          │
│  LocalTime                      TIME                                          │
│  Instant                        TIMESTAMP WITH TIME ZONE                      │
│  Duration                       BIGINT (milisegundos)                         │
│  byte[]                         BLOB / BYTEA                                  │
│  UUID                           UUID / CHAR(36)                               │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Visualización: Proceso de Mapping

```
        CREAR / ACTUALIZAR                                    LEER
        ─────────────────                                     ─────

   JSON                              Base de                   Base de
   ────                              Datos                     Datos
     │                                │                         │
     ▼                                ▼                         ▼
┌─────────┐                    ┌──────────────┐           ┌──────────────┐
│  new    │                    │   Tabla      │           │   Tabla      │
│ Producto│                    │  PRODUCTO    │           │  PRODUCTO    │
│         │                    │              │           │              │
│nombre:  │                    │ NOMBRE │ ... │           │NOMBRE │ ... │
│"Laptop" │                    │ Laptop │    │           │Laptop │    │
│         │                    │        │    │           │       │    │
│precio:  │                    │        │    │           │       │    │
│999.99   │                    │        │    │           │       │    │
└────┬────┘                    └────────┴────┘           └───────┴────┘
     │                                                           │
     │ Jackson deserializa                                       │
     │                                                            │
     ▼                                                            ▼
┌─────────┐                                              ┌─────────────┐
│ Producto│                                              │  Producto   │
│ (Java)  │                                              │   (Java)    │
└────┬────┘                                              └──────┬──────┘
     │                                                          │
     │ JPA/Hibernate                                            │
     │ convierte a SQL                                           │ JPA/Hibernate
     │                                                          │ convierte de SQL
     ▼                                                          ▼
┌──────────┐                                            ┌──────────────┐
│ INSERT   │                                            │ Producto     │
│ INTO     │                                            │ obj = new... │
│ PRODUCTO │                                            │ obj.setNombre│
│ ...      │                                            │ ("Laptop")   │
└──────────┘                                            └──────────────┘
```

---

## ❌ Errores Comunes

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **Table "PRODUCTO" not found** | JPA no creó la tabla | Añadir `spring.jpa.hibernate.ddl-auto=create` |
| 2 | **No default constructor for entity** | Falta constructor sin args | Añadir `public Producto() {}` |
| 3 | **No getter for property: id** | Falta getter del id | Añadir `getId()` |
| 4 | **Type mismatch** | Tipos incompatibles | Verificar tipos Java vs SQL |
| 5 | **Entity is not mapped** | Falta `@Entity` | Añadir `@Entity` antes de la clase |
| 6 | **Duplicate mapping for entity** | Dos entidades con mismo nombre | Usar `@Table(name="nombre")` |
| 7 | **NULL not allowed for id** | ID null en UPDATE | Verificar que el ID existe |

---

## 💡 Tips

### 1. Nombre de la tabla

Por defecto, JPA usa el nombre de la clase como nombre de tabla:
```
Producto → PRODUCTO (Spring lo convierte a mayúsculas)
```

Para un nombre específico, usa `@Table`:
```java
@Entity
@Table(name = "PRODUCTOS")  // Nombre exacto en la BD
public class Producto { ... }
```

### 2. Nombre de las columnas

Por defecto, los nombres de columnas usan camelCase → snake_case:
```
nombre → NOMBRE
descripcion → DESCRIPCION
precioUnitario → PRECIO_UNITARIO
```

### 3. Constructor vacío es OBLIGATORIO

JPA necesita crear objetos vacíos y luego populate los campos:
```java
// ✓ SIEMPRE necesario
public Producto() {
}

// Útil para crear productos directamente
public Producto(String nombre, Double precio) {
    this.nombre = nombre;
    this.precio = precio;
}
```

### 4. Usa getters y setters

Sin ellos, JPA no puede leer ni escribir datos:
```java
// JPA USA getters para LEER
producto.getNombre()  // Obtiene el nombre de la BD

// JPA USA setters para ESCRIBIR
producto.setPrecio(99.99)  // Prepara para guardar en BD
```

### 5. BigDecimal para dinero (importante)

```java
// ✓ CORRECTO - Precisión exacta
private BigDecimal precio;

// ✗ PROBLEMAS - Errores de округления
private Double precio;

// Ejemplo del problema:
// 0.1 + 0.2 = 0.30000000000000004 (Double)
// 0.1 + 0.2 = 0.3 (BigDecimal)
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué anotación marca una clase como tabla de BD?

- [ ] A) @Table
- [ ] B) @Entity
- [ ] C) @Id
- [ ] D) @Column

### Pregunta 2:
¿Qué anotación marca la llave primaria?

- [ ] A) @GeneratedValue
- [ ] B) @Id
- [ ] C) @PrimaryKey
- [ ] D) @PK

### Pregunta 3:
¿Qué tipo Java es mejor para guardar dinero?

- [ ] A) Double
- [ ] B) Float
- [ ] C) BigDecimal
- [ ] D) Integer

### Pregunta 4:
¿Qué estrategia de generación usa H2 por defecto?

- [ ] A) SEQUENCE
- [ ] B) TABLE
- [ ] C) IDENTITY
- [ ] D) AUTO

### Pregunta 5:
¿Qué constructor es OBLIGATORIO en una entidad JPA?

- [ ] A) Constructor con parámetros
- [ ] B) Constructor sin parámetros
- [ ] C) Constructor copiando otro objeto
- [ ] D) Ninguno

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Añade un nuevo campo

Añade un campo `stock` (cantidad disponible) al modelo Producto:

1. ¿Qué tipo de dato usarías?
2. ¿Qué anotaciones necesitas?
3. ¿Cómo se llamará la columna en la BD?

### Ejercicio 2: Crea una nueva entidad

Crea una entidad `Cliente` con:
- `id` (Long, auto-generado)
- `nombre` (String)
- `email` (String)
- `telefono` (String)

### Ejercicio 3: Investiga

Investiga qué anotación usarías para:
1. Hacer que un campo NO se guarde en la BD
2. Cambiar el nombre de una columna específica
3. Hacer que un campo no acepte valores nulos

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
