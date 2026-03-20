# 🚀 Product API - Spring Boot

API REST de gestión de productos construida con Spring Boot 4.0 para el curso de Spring Boot.

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#-tecnologías)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Modelo de Datos](#-modelo-de-datos)
- [Herramientas de Prueba](#-herramientas-de-prueba)
- [Documentación](#-documentación)
- [Autor](#-autor)

---

## 📝 Descripción

API RESTful para la gestión de productos con operaciones CRUD básicas. Este proyecto sirve como ejemplo práctico para aprender desarrollo de APIs con Spring Boot.

### Funcionalidades

- ✅ Listar todos los productos
- ✅ Obtener producto por ID
- ✅ Crear nuevos productos
- ✅ Inicialización automática con datos de ejemplo

---

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje de programación |
| **Spring Boot** | 4.0.4 | Framework principal |
| **Spring Data JPA** | - | Persistencia de datos |
| **H2 Database** | - | Base de datos en memoria |
| **Maven** | 3.6+ | Gestión de dependencias |
| **Jakarta Persistence** | - | ORM (JPA) |

---

## 📦 Requisitos

Antes de ejecutar este proyecto, asegúrate de tener instalado:

- **JDK 17** o superior
- **Maven 3.6+** (o usar el wrapper incluido `mvnw`)
- **IDE** (IntelliJ, VS Code, Cursor AI)

### Verificar Instalación

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/product-api.git
cd product-api
```

### 2. Ejecutar la Aplicación

```bash
# Usando Maven Wrapper (recomendado)
./mvnw spring-boot:run

# O usando Maven directo
mvn spring-boot:run
```

### 3. Compilar para Producción

```bash
# Generar JAR
./mvnw clean package

# Ejecutar JAR generado
java -jar target/app-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 📁 Estructura del Proyecto

```
app/
├── pom.xml                                    # Configuración Maven
├── README.md                                 # Este archivo
├── HELP.md                                   # Ayuda de Spring
├── src/
│   ├── main/
│   │   ├── java/com/product/app/
│   │   │   ├── AppApplication.java          # Clase principal
│   │   │   ├── DataInitializer.java         # Datos iniciales
│   │   │   ├── controller/
│   │   │   │   └── ProductoController.java  # Endpoints REST
│   │   │   ├── model/
│   │   │   │   └── Producto.java            # Entidad JPA
│   │   │   └── repository/
│   │   │       └── ProductoRepository.java  # Acceso a datos
│   │   └── resources/
│   │       └── application.properties      # Configuración
│   └── test/
│       └── java/com/product/app/
│           └── AppApplicationTests.java     # Pruebas
└── mvnw, mvnw.cmd                           # Maven Wrapper
```

### Arquitectura MVC

```
┌─────────────────────────────────────────────────────────────────┐
│                     ARQUITECTURA DEL PROYECTO                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│    Cliente (HTTP Request)                                       │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │  Controller   │ ProductoController.java                    │
│    │  @RestController                                       │
│    └───────┬───────┘                                           │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │  Repository    │ ProductoRepository.java                   │
│    │  JpaRepository │                                           │
│    └───────┬───────┘                                           │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │  H2 Database  │ Base de datos en memoria                 │
│    └───────────────┘                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🌐 API Endpoints

### Base URL
```
http://localhost:8080/api/productos
```

### Endpoints Disponibles

| Método | Endpoint | Descripción | Ejemplo |
|--------|----------|-------------|---------|
| `GET` | `/api/productos` | Listar todos los productos | [Probar](#listar-todos) |
| `GET` | `/api/productos/{id}` | Obtener producto por ID | [Probar](#obtener-por-id) |
| `POST` | `/api/productos` | Crear nuevo producto | [Probar](#crear-producto) |

---

### 📖 Documentación de Endpoints

#### Listar Todos los Productos

```http
GET /api/productos
```

**Respuesta:**
```json
[
    {
        "id": 1,
        "nombre": "Laptop",
        "descripcion": "Computadora portatil 15 pulgadas",
        "precio": 999.99
    },
    {
        "id": 2,
        "nombre": "Mouse",
        "descripcion": "Mouse inalambrico",
        "precio": 25.50
    }
]
```

#### Obtener Producto por ID

```http
GET /api/productos/{id}
```

**Ejemplo:** `GET /api/productos/1`

**Respuesta (200 OK):**
```json
{
    "id": 1,
    "nombre": "Laptop",
    "descripcion": "Computadora portatil 15 pulgadas",
    "precio": 999.99
}
```

**Respuesta (404 Not Found):**
```json
{
    // Cuerpo vacío
}
```

#### Crear Producto

```http
POST /api/productos
Content-Type: application/json

{
    "nombre": "USB",
    "descripcion": "32GB",
    "precio": 15.99
}
```

**Respuesta (200 OK):**
```json
{
    "id": 6,
    "nombre": "USB",
    "descripcion": "32GB",
    "precio": 15.99
}
```

---

## 💾 Modelo de Datos

### Entidad Producto

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador único (auto-generado) |
| `nombre` | String | Nombre del producto |
| `descripcion` | String | Descripción del producto |
| `precio` | Double | Precio del producto |

### Tabla en Base de Datos

```
PRODUCTO
┌──────────┬──────────────────┬─────────────┐
│   ID     │    NOMBRE        │   PRECIO    │
├──────────┼──────────────────┼─────────────┤
│  1       │ Laptop           │  999.99     │
│  2       │ Mouse            │  25.50      │
│  3       │ Teclado          │  75.00      │
│  4       │ Monitor          │  150.00     │
│  5       │ Auriculares      │  45.99      │
└──────────┴──────────────────┴─────────────┘
```

---

## 🛠️ Herramientas de Prueba

### Opción 1: Navegador Web

Simplemente abre en tu navegador:
```
http://localhost:8080/api/productos
```

### Opción 2: curl (Línea de Comandos)

```bash
# Listar todos los productos
curl http://localhost:8080/api/productos

# Obtener producto por ID
curl http://localhost:8080/api/productos/1

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
```

### Opción 3: Postman

1. Descarga [Postman](https://www.postman.com/downloads/)
2. Crea una nueva colección
3. Añade requests para cada endpoint
4. Ejecuta las requests

### Opción 4: H2 Console (Ver Base de Datos)

Accede a la consola web de H2 para visualizar y consultar la base de datos:

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuario: sa
Contraseña: (vacía)
```

---

## 📚 Documentación del Curso

La documentación completa del curso, incluyendo explicaciones detalladas de cada tema, se encuentra en:

📁 **[Documentación del Curso](./docs-fundamentos/)**

### Contenido de la Documentación

```
docs-fundamentos/
├── README.md                          # Índice general
├── clase-01-introduccion-springboot.md # Clase 1 completa
├── 01-que-es-rest.md                 # Fundamentos REST
├── 02-anatomia-proyecto.md          # Estructura del proyecto
├── 03-flujo-http-spring.md           # Flujo HTTP en Spring
├── 04-modelo-datos.md               # @Entity, JPA
├── 05-repository.md                 # JpaRepository
├── 06-controller.md                 # @RestController
├── 07-h2-console.md                 # Tutorial H2
├── 08-herramientas.md                # curl, Postman
├── 09-glosario-anotaciones.md       # Cheat sheet
├── 10-errores-comunes.md            # Troubleshooting
├── 11-ejercicios-practicos.md      # Prácticas
└── RESUMEN.md                       # Referencia rápida
```

---

## 🔧 Configuración

### Puerto del Servidor

Por defecto, la aplicación usa el puerto **8080**.

Para cambiarlo, edita `src/main/resources/application.properties`:

```properties
server.port=3000
```

### Base de Datos H2

La base de datos H2 está configurada en modo en memoria:

```properties
# URL de la base de datos
spring.datasource.url=jdbc:h2:mem:testdb

# Habilitar consola H2
spring.h2.console.enabled=true
```

---

## 📈 Mejoras Futuras

Este es un proyecto educativo que puede expandirse con:

- [ ] Endpoint PUT para actualizar productos
- [ ] Endpoint DELETE para eliminar productos
- [ ] Capa de Servicio (@Service)
- [ ] Validación de datos (@Valid)
- [ ] Manejo de errores (@ControllerAdvice)
- [ ] Documentación con Swagger/OpenAPI
- [ ] Pruebas unitarias
- [ ] Conexión a PostgreSQL/MySQL
- [ ] Paginación y filtrado

---

## 📄 Licencia

Este proyecto es parte del curso de Spring Boot y se proporciona con fines educativos.

---

## 👤 Autor

**Curso de Spring Boot**

---

## 🙏 Agradecimientos

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework principal
- [Spring Initializr](https://start.spring.io/) - Generación de proyectos
- [Baeldung](https://www.baeldung.com/) - Excelentes tutoriales

---

## ⌨️ Comandos Maven Útiles

```bash
# Compilar
./mvnw compile

# Ejecutar pruebas
./mvnw test

# Limpiar y compilar
./mvnw clean package

# Ver dependencias
./mvnw dependency:tree

# Ejecutar sin tests
./mvnw spring-boot:run -DskipTests
```

---

<p align="center">
  <img src="https://spring.io/images/projects/spring-boot-7f2c603d4fc16e487cd6f5ac2857770f.svg" width="100" alt="Spring Boot">
</p>

<p align="center">
  Desarrollado con Spring Boot para el curso de desarrollo web
</p>

**¡Happy Coding!** 🎉

*Última actualización: Marzo 2026*
