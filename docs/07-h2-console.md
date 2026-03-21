# 🗄️ H2 Console - Explorando la Base de Datos

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, sabrás cómo usar H2 Console para visualizar, consultar y manipular datos en tu base de datos directamente desde el navegador.

---

## 📚 Teoría

### ¿Qué es H2 Database?

**H2** es una base de datos **en memoria** escrita completamente en Java. En Spring Boot, se usa comúnmente para:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           H2 DATABASE                                       │
│                                                                              │
│   CARACTERÍSTICA              DESCRIPCIÓN                                    │
│   ─────────────               ───────────                                    │
│                                                                              │
│   En memoria                  Los datos EXISTEN solo mientras               │
│                               la app está corriendo                          │
│                               (Se borran al reiniciar)                       │
│                                                                              │
│   Embebida                    Viene incluido con Spring Boot                │
│                               (No necesitas instalar MySQL/PostgreSQL)     │
│                                                                              │
│   Ligera                      Solo ~2MB, muy rápida                         │
│                                                                              │
│   Compatible SQL              Soporta estándar SQL                          │
│                                                                              │
│   Modo desarrollo             Perfecta para aprender y desarrollar          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ¿Por qué H2 en Spring Boot?

```
INSTALAR MySQL/PostgreSQL:                    USAR H2 con Spring Boot:
────────────────────────                              ──────────────────────
1. Descargar MySQL                               1. Añadir dependencia
2. Instalar en el sistema                            (ya viene incluido!)
3. Crear usuario/password
4. Crear base de datos                          2. Configurar en properties
5. Configurar conexión                          
         │                                            ✓ Listo!
         ▼                                           
    30+ minutos                                   5 minutos
```

### ¿Qué es H2 Console?

**H2 Console** es una **interfaz web** para explorar y administrar tu base de datos H2. Es similar a phpMyAdmin pero para H2.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          H2 CONSOLE (Interfaz Web)                           │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  H2 Console                                              [H2]       │   │
│   ├─────────────────────────────────────────────────────────────────────┤   │
│   │                                                                     │   │
│   │  ┌─────────────────────────────────────────────────────────────┐   │   │
│   │  │ JDBC URL: jdbc:h2:mem:testdb                                │   │   │
│   │  │ User Name: sa                                               │   │   │
│   │  │ Password:    [________]                                     │   │   │
│   │  │ [Connect]                                                    │   │   │
│   │  └─────────────────────────────────────────────────────────────┘   │   │
│   │                                                                     │   │
│   │  ┌───────────────────────────────────────────────────────────┐     │   │
│   │  │ SQL Statement                                              │     │   │
│   │  │ ┌───────────────────────────────────────────────────────┐ │     │   │
│   │  │ │ SELECT * FROM PRODUCTO;                                │ │     │   │
│   │  │ └───────────────────────────────────────────────────────┘ │     │   │
│   │  │                                    [Run] [Clear]          │     │   │
│   │  └───────────────────────────────────────────────────────────┘     │   │
│   │                                                                     │   │
│   │  ┌───────────────────────────────────────────────────────────┐     │   │
│   │  │ RESULTS                                                    │     │   │
│   │  │ ┌─────────┬──────────┬────────────────┬────────┐          │     │   │
│   │  │ │ ID      │ NOMBRE   │ DESCRIPCION    │ PRECIO │          │     │   │
│   │  │ ├─────────┼──────────┼────────────────┼────────┤          │     │   │
│   │  │ │ 1       │ Laptop   │ 16GB RAM       │ 999.99 │          │     │   │
│   │  │ │ 2       │ Mouse    │ Inalámbrico    │ 29.99  │          │     │   │
│   │  │ │ 3       │ Teclado  │ Mecánico       │ 79.99  │          │     │   │
│   │  │ └─────────┴──────────┴────────────────┴────────┘          │     │   │
│   │  └───────────────────────────────────────────────────────────┘     │   │
│   │                                                                     │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Configuración en application.properties

Por defecto, Spring Boot ya configura H2. Pero puedes personalizar:

```properties
# ─────────────────────────────────────────────────────────────────────────────
# CONFIGURACIÓN H2 DATABASE
# ─────────────────────────────────────────────────────────────────────────────

# URL de conexión JDBC
spring.datasource.url=jdbc:h2:mem:testdb

# Usuario (por defecto 'sa' para H2)
spring.datasource.username=sa

# Password (vacío por defecto)
spring.datasource.password=

# Driver de H2 (Spring Boot lo auto-detecta)
spring.datasource.driver-class-name=org.h2.Driver

# ─────────────────────────────────────────────────────────────────────────────
# JPA / HIBERNATE
# ─────────────────────────────────────────────────────────────────────────────

# Acción automática de Hibernate al iniciar
# Valores posibles:
#   none        → No hace nada (tú creas las tablas manualmente)
#   validate    → Solo valida que las tablas coincidan
#   update      → Actualiza las tablas si hay cambios
#   create      → CREA las tablas cada vez (¡borra datos!)
#   create-drop → CREATE al iniciar, DROP al cerrar
spring.jpa.hibernate.ddl-auto=create

# Muestra las consultas SQL en la consola
spring.jpa.show-sql=true

# Formatea el SQL para que sea más legible
spring.jpa.properties.hibernate.format_sql=true

# Dialecto de SQL (H2 en este caso)
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# ─────────────────────────────────────────────────────────────────────────────
# H2 CONSOLE (Interfaz Web)
# ─────────────────────────────────────────────────────────────────────────────

# Habilitar la consola H2
spring.h2.console.enabled=true

# URL de la consola (default: /h2-console)
spring.h2.console.path=/h2-console

# Permitir acceso desde cualquier origen (para desarrollo)
spring.h2.console.settings.web-allow-others=false
```

---

## 💻 Tutorial Paso a Paso

### Paso 1: Ejecutar la Aplicación

Primero, asegúrate de que tu aplicación Spring Boot esté corriendo:

```bash
# Desde la terminal, en la carpeta del proyecto
mvn spring-boot:run
```

Deberías ver algo como:
```
☕ Started AppApplication in X.XXX seconds
🎯 H2 Console available at '/h2-console'
💾 JDBC URL: jdbc:h2:mem:testdb
```

---

### Paso 2: Abrir el Navegador

Abre tu navegador web favorito (Chrome, Firefox, Edge, etc.)

---

### Paso 3: Ir a la Consola H2

En la barra de direcciones, escribe:

```
http://localhost:8080/h2-console
```

---

### Paso 4: Configurar la Conexión

En la pantalla de login de H2 Console:

```
┌─────────────────────────────────────────────┐
│              H2 Console Login                │
├─────────────────────────────────────────────┤
│                                             │
│  JDBC URL:                                   │
│  ┌─────────────────────────────────────────┐│
│  │ jdbc:h2:mem:testdb                      ││  ← Debe coincidir con
│  └─────────────────────────────────────────┘│     application.properties
│                                             │
│  User Name:                                 │
│  ┌─────────────────────────────────────────┐│
│  │ sa                                       ││  ← Usuario por defecto
│  └─────────────────────────────────────────┘│
│                                             │
│  Password:                                  │
│  ┌─────────────────────────────────────────┐│
│  │                                          ││  ← Vacío por defecto
│  └─────────────────────────────────────────┘│
│                                             │
│  [Connect]                                  │
│                                             │
└─────────────────────────────────────────────┘
```

**Haz clic en "Connect"**

---

### Paso 5: Escribir Consultas SQL

¡Bienvenido a la consola! Ahora puedes escribir SQL:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  H2 Console                                                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  TABLES:                                                                   │
│  ├── PRODUCTO                                                              │
│  └── ...                                                                   │
│                                                                             │
│  SQL Statement:                                                            │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │ SELECT * FROM PRODUCTO;                                                │ │
│  │                                                                       │ │
│  │                                                                       │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
│                                                    [Run] [Clear]           │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Captura Visual Descrita

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  ESTRUCTURA DE H2 CONSOLE                                                   │
│                                                                              │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │  Panel Izquierdo: ÁRBOL DE NAVEGACIÓN                                  │  │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │  │
│  │  │ 🗄️ Tables                                                          │  │  │
│  │  │    ├── 📋 PRODUCTO                                                │  │  │
│  │  │    │   └── Columnas: ID, NOMBRE, DESCRIPCION, PRECIO             │  │  │
│  │  │    └── 📋 ( otras tablas )                                       │  │  │
│  │  └─────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │  Panel Derecho: EDITOR SQL + RESULTADOS                                │  │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │  │
│  │  │ SQL Editor                                                        │  │  │
│  │  │ ┌─────────────────────────────────────────────────────────────┐│  │  │
│  │  │ │ SELECT * FROM PRODUCTO                                      ││  │  │
│  │  │ └─────────────────────────────────────────────────────────────┘│  │  │
│  │  │                                      [Run] [Format] [History]  │  │  │
│  │  └─────────────────────────────────────────────────────────────────┘  │  │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Resultados                                                       │  │  │
│  │  │ ┌──────────┬────────────┬────────────────┬────────┐           │  │  │
│  │  │ │ ID (PK)  │ NOMBRE     │ DESCRIPCION     │ PRECIO │           │  │  │
│  │  │ ├──────────┼────────────┼────────────────┼────────┤           │  │  │
│  │  │ │ 1        │ Laptop     │ 16GB RAM       │ 999.99 │           │  │  │
│  │  │ │ 2        │ Mouse      │ Inalámbrico    │ 29.99  │           │  │  │
│  │  │ │ 3        │ Teclado    │ Mecánico       │ 79.99  │           │  │  │
│  │  │ └──────────┴────────────┴────────────────┴────────┘           │  │  │
│  │  └─────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📋 Comandos SQL Útiles

### SELECT - Leer datos

```sql
-- Obtener TODOS los productos
SELECT * FROM PRODUCTO;

-- Obtener producto específico
SELECT * FROM PRODUCTO WHERE ID = 1;

-- Buscar por nombre exacto
SELECT * FROM PRODUCTO WHERE NOMBRE = 'Laptop';

-- Buscar con LIKE (parcial)
SELECT * FROM PRODUCTO WHERE NOMBRE LIKE '%lap%';

-- Ordenar resultados
SELECT * FROM PRODUCTO ORDER BY PRECIO ASC;
SELECT * FROM PRODUCTO ORDER BY PRECIO DESC;

-- Limitar resultados
SELECT * FROM PRODUCTO LIMIT 10;

-- Contar registros
SELECT COUNT(*) FROM PRODUCTO;
```

### INSERT - Crear datos

```sql
-- Insertar un producto
INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO)
VALUES ('USB', '32GB', 15.99);

-- Insertar múltiples
INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO)
VALUES 
    ('Monitor', '27 pulgadas', 249.99),
    ('Webcam', '1080p', 59.99),
    ('Audífonos', 'Bluetooth', 79.99);
```

### UPDATE - Actualizar datos

```sql
-- Actualizar un campo
UPDATE PRODUCTO 
SET PRECIO = 20.00 
WHERE ID = 1;

-- Actualizar múltiples campos
UPDATE PRODUCTO 
SET NOMBRE = 'Laptop Gaming', 
    DESCRIPCION = '32GB RAM, 1TB SSD',
    PRECIO = 1299.99
WHERE ID = 1;
```

### DELETE - Eliminar datos

```sql
-- Eliminar uno
DELETE FROM PRODUCTO WHERE ID = 1;

-- Eliminar múltiples
DELETE FROM PRODUCTO WHERE PRECIO < 50;

-- Eliminar TODOS (¡cuidado!)
DELETE FROM PRODUCTO;
```

### Otros comandos útiles

```sql
-- Ver estructura de la tabla
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'PRODUCTO';

-- Ver todas las tablas
SELECT TABLE_NAME 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'PUBLIC';
```

---

## ❌ Errores Comunes

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **Login Failed** | JDBC URL incorrecto | Usar `jdbc:h2:mem:testdb` |
| 2 | **Table not found** | JPA no creó las tablas | Verificar `spring.jpa.hibernate.ddl-auto=create` |
| 3 | **Database not found** | La BD no existe | Verificar la URL de conexión |
| 4 | **Connection refused** | La app no está corriendo | Ejecutar `mvn spring-boot:run` |
| 5 | **Empty result set** | No hay datos | Insertar datos o verificar el `DataInitializer` |
| 6 | **Access denied** | Credenciales incorrectas | User: `sa`, Password: (vacío) |

---

## 💡 Tips

### 1. Dónde ver los logs SQL

Si quieres ver las consultas SQL que JPA ejecuta:

```properties
# En application.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Salida en consola:
```sql
    Hibernate: 
        select
            producto0_.id as id1_0_,
            producto0_.descripcion as descripc2_0_,
            producto0_.nombre as nombre3_0_,
            producto0_.precio as precio4_0_ 
        from
            producto producto0_
```

### 2. Dónde están los datos realmente

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           VIDA DE LOS DATOS H2                              │
│                                                                              │
│   INICIA LA APP                                                              │
│        │                                                                     │
│        ▼                                                                     │
│   ┌─────────────────────────┐                                               │
│   │ JPA crea las tablas     │  (CREATE si ddl-auto=create)                  │
│   │ DataInitializer inserta │  (INSERTs de datos iniciales)                │
│   └────────────┬────────────┘                                               │
│                │                                                              │
│                ▼                                                              │
│   ┌─────────────────────────┐                                               │
│   │ DATOS EN MEMORIA        │  ← Solo existen aquí                          │
│   │ ┌─────┬──────────────┐  │                                               │
│   │ │ 1   │ Laptop       │  │                                               │
│   │ │ 2   │ Mouse        │  │                                               │
│   │ └─────┴──────────────┘  │                                               │
│   └────────────┬────────────┘                                               │
│                │                                                              │
│                ▼                                                              │
│   CIERRA LA APP / REINICIA                                                   │
│                │                                                              │
│                ▼                                                              │
│   ┌─────────────────────────┐                                               │
│   │ MEMORIA LIMPIADA        │  ← ¡Todos los datos se pierden!               │
│   │ Tablas destruidas       │                                               │
│   └─────────────────────────┘                                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 3. Mantener datos entre reinicios

Si necesitas persistencia:

```properties
# Para guardar en archivo (no en memoria)
spring.datasource.url=jdbc:h2:file:./data/productos

# Para guardar en memoria pero mantener datos
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

### 4. Consola H2 solo en desarrollo

```properties
# Desactivar en producción
spring.h2.console.enabled=false
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿H2 es una base de datos en memoria o en disco?

- [ ] A) Solo en memoria
- [ ] B) Solo en disco
- [ ] C) Puede ser ambas
- [ ] D) No es una base de datos

### Pregunta 2:
¿Qué URL se usa para H2 Console?

- [ ] A) http://localhost:8080/h2
- [ ] B) http://localhost:8080/h2-console
- [ ] C) http://localhost:8080/console
- [ ] D) http://localhost:8080/database

### Pregunta 3:
¿Qué usuario usa H2 por defecto?

- [ ] A) root
- [ ] B) admin
- [ ] C) sa
- [ ] D) h2

### Pregunta 4:
¿Qué hace `spring.jpa.hibernate.ddl-auto=create`?

- [ ] A) Borra los datos existentes
- [ ] B) Crea las tablas automáticamente
- [ ] C) Conecta a la base de datos
- [ ] D) Inserta datos iniciales

### Pregunta 5:
¿Qué sucede con los datos H2 al reiniciar la aplicación?

- [ ] A) Se mantienen
- [ ] B) Se borran
- [ ] C) Se duplican
- [ ] D) Depende de la configuración

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Conectar a H2 Console

1. Ejecuta la aplicación
2. Abre http://localhost:8080/h2-console
3. Conéctate exitosamente
4. Toma un screenshot

### Ejercicio 2: Explorar datos

Ejecuta estas consultas y anota los resultados:

```sql
-- 1. Ver todos los productos
SELECT * FROM PRODUCTO;

-- 2. Ver solo nombres y precios
SELECT NOMBRE, PRECIO FROM PRODUCTO;

-- 3. Ver productos ordenados por precio
SELECT * FROM PRODUCTO ORDER BY PRECIO DESC;

-- 4. Contar productos
SELECT COUNT(*) FROM PRODUCTO;
```

### Ejercicio 3: Insertar datos

Inserta 3 productos nuevos usando SQL:

```sql
INSERT INTO PRODUCTO (NOMBRE, DESCRIPCION, PRECIO) VALUES ('...', '...', ...);
```

Luego verifica con `SELECT * FROM PRODUCTO;`

### Ejercicio 4: Actualizar datos

1. Actualiza el precio de un producto
2. Actualiza el nombre de otro
3. Verifica los cambios

### Ejercicio 5: Eliminar datos

1. Elimina un producto
2. Verifica con SELECT
3. (Nota: Tus datos se borrarán al reiniciar la app)

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
