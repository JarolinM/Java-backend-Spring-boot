# Clase 1: Introducción a Spring Boot y Estructura de Proyectos

## 📋 Tabla de Contenidos
1. [Antes de Empezar](#antes-de-empezar)
2. [Bienvenida e Introducción](#bienvenida)
3. [¿Qué es Spring Boot?](#que-es-spring-boot)
4. [Creación del Proyecto](#creacion-proyecto)
5. [Estructura Profesional](#estructura)
6. [Configuración Básica](#configuracion)
7. [Servicios REST y Primera API](#primera-api)
8. [Herramientas de IA](#herramientas-ia)
9. [Conclusiones](#conclusiones)

---

<a name="antes-de-empezar"></a>
## 1. Antes de Empezar: Preparación del Entorno

Para seguir esta clase y practicar, asegúrate de tener instalado lo siguiente:

### 🛠️ Software Requerido

| Software | Versión | Descripción |
|----------|---------|-------------|
| **Java Development Kit (JDK)** | 17+ (LTS recomendado) | Compilador y runtime de Java |
| **IDE** | - | Cursor AI, IntelliJ IDEA, VS Code |
| **Navegador Web** | - | Chrome, Firefox, Edge, Safari |
| **Herramienta API** (opcional) | - | Postman, Insomnia, o curl |

### 💻 Instalación de JDK

```bash
# Verificar instalación
java -version
javac -version
```

### 🔌 Herramientas para probar APIs

- **Postman**: https://www.postman.com/downloads/
- **Insomnia**: https://insomnia.rest/download
- **curl**: Incluido en Windows 10+ y Linux/macOS

---

<a name="bienvenida"></a>
## 2. Bienvenida e Introducción

### 🎯 Objetivos de Aprendizaje

Al finalizar esta sesión, serás capaz de:

- ✅ Comprender qué es Spring Boot, sus ventajas y por qué es tan utilizado
- ✅ Generar y configurar tu primer proyecto Spring Boot
- ✅ Entender y aplicar una estructura de carpetas profesional
- ✅ Describir los principios básicos de las APIs REST
- ✅ Construir y probar un endpoint REST básico que retorne datos en JSON
- ✅ Utilizar herramientas de IA (como Cursor AI) para asistirte en tareas de desarrollo

### 📅 Agenda del Día

1. ¿Qué es Spring Boot?
2. Creación del proyecto con Spring Initializr
3. Estructura recomendada de carpetas
4. Configuración básica (application.properties)
5. Introducción a REST y creación de nuestra primera API GET
6. Asistencia con herramientas de IA (Cursor AI)
7. Conclusiones y próximos pasos

---

<a name="que-es-spring-boot"></a>
## 3. ¿Qué es Spring Boot y por qué usarlo?

### 📖 Contexto Histórico

Anteriormente, desarrollar con Java EE o incluso con el Spring Framework clásico podía ser complejo debido a:

- Gran cantidad de configuración necesaria (XMLs)
- Despliegue en servidores externos
- Gestión de dependencias manual

### 🚀 ¿Qué es Spring Boot?

**Spring Boot** nació para simplificar drásticamente el desarrollo de aplicaciones Spring. Piensa en él como una capa por encima del Spring Framework tradicional que te permite **"arrancar" (boot) rápidamente**.

### 🔑 Conceptos Clave

```
┌─────────────────────────────────────────────────────────────────┐
│                    CONCEPTOS CLAVE DE SPRING BOOT               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Convention over Configuration                               │
│     "Convención sobre Configuración"                            │
│     → Spring Boot asume lo que necesitas                        │
│                                                                 │
│  2. Auto-configuración                                          │
│     → Configura automáticamente basado en tus dependencias      │
│                                                                 │
│  3. Servidores Embebidos                                        │
│     → Tomcat, Jetty incluídos (no necesitas servidor externo)   │
│                                                                 │
│  4. Starters                                                    │
│     → Dependencias pre-configuradas (ej: spring-boot-starter-web)│
│                                                                 │
│  5. Actuator                                                    │
│     → Monitoreo y gestión de la aplicación                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### ✅ Ventajas Clave

| Ventaja | Descripción |
|---------|-------------|
| **Mayor Productividad** | Desarrollas más rápido gracias a la auto-configuración |
| **Ideal para Microservicios** | Diseño JAR ejecutable y ligero |
| **Ecosistema Spring** | Acceso a Data, Security, Cloud, etc. |
| **Gran Comunidad** | Tecnología madura con mucho soporte |

### 📊 Comparación con Otras Tecnologías

```
┌────────────────────┬──────────────────────────────────────────────┐
│   Tecnología       │   Diferencia con Spring Boot                 │
├────────────────────┼──────────────────────────────────────────────┤
│ Spring Clásico     │ Boot simplifica la configuración manual/XML  │
├────────────────────┼──────────────────────────────────────────────┤
│ Java EE/Jakarta    │ Boot es más ligero y rápido de configurar    │
├────────────────────┼──────────────────────────────────────────────┤
│ Node.js/Django     │ Spring Boot ofrece tipado fuerte + ecosistema│
├────────────────────┼──────────────────────────────────────────────┤
│ Quarkus/Micronaut  │ Alternativas optimizadas; Boot es líder      │
└────────────────────┴──────────────────────────────────────────────┘
```

---

<a name="creacion-proyecto"></a>
## 4. Creación del Proyecto con Spring Initializr

### 🌐 ¿Qué es Spring Initializr?

Es una herramienta web que genera el esqueleto básico de tu proyecto Spring Boot.

### 📝 Pasos para Crear el Proyecto

```
1. Abre tu navegador → https://start.spring.io/

2. Configura tu proyecto:

   ┌─────────────────────────────────────┐
   │ Project:          Maven            │
   │ Language:         Java            │
   │ Spring Boot:      4.0.x (latest)  │
   │ Group:            com.midominio   │
   │ Artifact:         mi-proyecto     │
   │ Package name:     com.midominio.. │
   │ Packaging:        Jar             │
   │ Java:             17 o 21         │
   └─────────────────────────────────────┘

3. Añade dependencia: Spring Web

4. Click en "Generate" → Descarga ZIP
```

### 💻 Importar en tu IDE

```bash
# 1. Descomprime el archivo ZIP
# 2. Abre tu IDE (IntelliJ, VS Code, Cursor)
# 3. Import/Open proyecto existente
# 4. Selecciona la carpeta raíz (donde está pom.xml)
# 5. Espera a que Maven descargue dependencias
```

### 📁 Estructura Generada

```
mi-proyecto/
├── pom.xml                          # Dependencias Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/midominio/miproyecto/
│   │   │       └── MiProyectoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/...
└── mvnw, mvnw.cmd                   # Maven wrapper
```

### 🎯 La Clase Principal

```java
package com.midominio.miproyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // ← Anotación clave
public class MiProyectoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiProyectoApplication.class, args);
    }
}
```

### 📖 ¿Qué hace @SpringBootApplication?

```
@SpringBootApplication = @Configuration 
                       + @EnableAutoConfiguration 
                       + @ComponentScan

Significa:
├── "Esta es la clase principal"
├── "Configura automáticamente basándote en las dependencias"
└── "Escanea clases con anotaciones (@Controller, @Service, etc.)"
```

### ▶️ Primera Ejecución

```bash
# Ejecuta desde tu IDE (clic derecho → Run)
# o desde terminal:

./mvnw spring-boot:run

# La consola mostrará:
# Started MiProyectoApplication in X.XXX seconds
# Tomcat started on port(s): 8080
```

### 🌐 Prueba en el Navegador

```
http://localhost:8080  → Whitelabel Error Page (¡Esto es normal!)

Significa:
✅ Servidor arrancado correctamente
❌ No hay endpoint configurado (lo haremos a continuación)
```

---

<a name="estructura"></a>
## 5. Estructura Profesional de Carpetas

### 🎯 Importancia de la Estructura

| Beneficio | Descripción |
|-----------|-------------|
| **Separación de Responsabilidades** | Cada capa tiene un rol específico |
| **Mantenibilidad** | Código más fácil de entender y modificar |
| **Escalabilidad** | Componentes escalables independientemente |
| **Colaboración** | Varios desarrolladores sin conflictos |

### 🏗️ Modelo de Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                     ARQUITECTURA EN CAPAS                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│    Cliente (Navegador/App)                                      │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │  Controller   │ ← Recibe peticiones HTTP                   │
│    └───────┬───────┘                                           │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │   Service     │ ← Lógica de negocio                        │
│    └───────┬───────┘                                           │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │  Repository   │ ← Acceso a datos                           │
│    └───────┬───────┘                                           │
│            │                                                    │
│            ▼                                                    │
│    ┌───────────────┐                                           │
│    │ Base de Datos │ ← H2, MySQL, PostgreSQL...                │
│    └───────────────┘                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📁 Paquetes a Crear

En tu IDE, dentro del paquete base (`com.midominio.curso`), crea:

```java
src/main/java/com/midominio/curso/
├── config/           // Configuraciones (@Configuration)
├── controller/      // Controladores REST (@RestController)
├── model/           // Entidades y DTOs (@Entity)
├── repository/      // Acceso a datos (JpaRepository)
├── service/        // Lógica de negocio (@Service)
└── MiProyectoApplication.java
```

### 📋 Descripción de Cada Paquete

| Paquete | Responsabilidad | Anotación Típica |
|---------|-----------------|------------------|
| `controller` | Recibir peticiones HTTP, delegar, formatear respuesta | `@RestController` |
| `service` | Lógica de negocio, validaciones | `@Service` |
| `repository` | Interactuar con base de datos | `@Repository` |
| `model` | Representar datos (entidades, DTOs) | `@Entity`, record |
| `config` | Configuraciones personalizadas | `@Configuration` |

---

<a name="configuracion"></a>
## 6. Configuración Básica: application.properties

### 📍 Ubicación

```
src/main/resources/application.properties
```

### 💻 Ejemplo: Cambiar Puerto

```properties
# Cambiar el puerto del servidor
server.port=8081

# Nombre de la aplicación (útil para logs)
spring.application.name=MiPrimerProyecto

# Puerto alternativo
# server.port=3000
```

### ▶️ Probar el Cambio

```bash
# 1. Ejecuta la aplicación
# 2. Abre: http://localhost:8081

# (8080 ya no funcionará)
```

### 📝 Otras Propiedades Comunes

```properties
# Base de datos (próximas clases)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Logging
logging.level.org.springframework=INFO
```

---

<a name="primera-api"></a>
## 7. Servicios REST y Primera API con Spring Boot

### 📖 ¿Qué es REST?

**REST** = **RE**presentational **S**tate **T**ransfer

Es un estilo arquitectónico para diseñar sistemas distribuidos (servicios web).

### 🏛️ Principios REST

```
┌─────────────────────────────────────────────────────────────────┐
│                    6 PRINCIPIOS REST                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Cliente-Servidor                                            │
│     → Separación de responsabilidades                          │
│                                                                 │
│  2. Sin Estado (Stateless)                                     │
│     → Cada petición contiene toda la información              │
│                                                                 │
│  3. Cacheable                                                   │
│     → Las respuestas pueden cachearse                          │
│                                                                 │
│  4. Interfaz Uniforme                                          │
│     → Recursos identificados por URIs                          │
│     → Verbos HTTP estándar                                    │
│                                                                 │
│  5. Mensajes Autodescriptivos                                   │
│     → Encabezados HTTP describen el contenido                  │
│                                                                 │
│  6. Sistema en Capas                                           │
│     → Soporta intermediarios (proxies, balanceadores)          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 🔗 Recursos y URIs

| Recurso | URI | Descripción |
|---------|-----|-------------|
| Productos | `/productos` | Colección de productos |
| Producto 5 | `/productos/5` | Producto específico |
| Usuarios | `/usuarios` | Colección de usuarios |

### 📡 Verbos HTTP

```
┌────────┬──────────────┬────────────────────────────────────────┐
│ Verbo  │ URI          │ Descripción                            │
├────────┼──────────────┼────────────────────────────────────────┤
│ GET    │ /productos   │ Leer todos los productos               │
│ GET    │ /productos/5 │ Leer producto con ID 5                 │
│ POST   │ /productos   │ Crear nuevo producto                   │
│ PUT    │ /productos/5 │ Actualizar producto 5 (completo)      │
│ DELETE │ /productos/5 │ Eliminar producto 5                   │
└────────┴──────────────┴────────────────────────────────────────┘
```

### 📊 Códigos de Estado HTTP

```
┌──────┬──────────────────────────────────────────────────────────┐
│ 2xx  │ ÉXITO                                                     │
├──────┼──────────────────────────────────────────────────────────┤
│ 200  │ OK - Solicitud exitosa                                   │
│ 201  │ Created - Recurso creado                                 │
│ 204  │ No Content - Éxito sin contenido (DELETE)               │
├──────┼──────────────────────────────────────────────────────────┤
│ 4xx  │ ERROR DEL CLIENTE                                        │
├──────┼──────────────────────────────────────────────────────────┤
│ 400  │ Bad Request - Petición inválida                          │
│ 404  │ Not Found - Recurso no encontrado                        │
│ 409  │ Conflict - Conflicto de datos                             │
├──────┼──────────────────────────────────────────────────────────┤
│ 5xx  │ ERROR DEL SERVIDOR                                       │
├──────┼──────────────────────────────────────────────────────────┤
│ 500  │ Internal Server Error - Error inesperado                │
│ 503  │ Service Unavailable - Servicio no disponible             │
└──────┴──────────────────────────────────────────────────────────┘
```

### 🎯 Anotaciones para REST en Spring Boot

| Anotación | Descripción |
|-----------|-------------|
| `@RestController` | Combina `@Controller` + `@ResponseBody` |
| `@GetMapping` | Responde a GET |
| `@PostMapping` | Responde a POST |
| `@PutMapping` | Responde a PUT |
| `@DeleteMapping` | Responde a DELETE |
| `@PatchMapping` | Responde a PATCH |

---

## 🎯 Demo: Creando Nuestro Primer Endpoint

### 💻 Crear SaludoController

En el paquete `controller`, crea:

```java
package com.midominio.curso.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController  // 1️⃣ Marca esta clase como controlador REST
public class SaludoController {

    // 2️⃣ Mapea GET /saludo a este método
    @GetMapping("/saludo")
    public Map<String, String> obtenerSaludo() {
        // 3️⃣ Spring Boot convierte automáticamente a JSON
        return Map.of("mensaje", "¡Hola REST desde Spring Boot!");
    }
}
```

### 📝 Explicación Paso a Paso

```
┌─────────────────────────────────────────────────────────────────┐
│                    EXPLICACIÓN DEL CÓDIGO                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  @RestController                                                │
│  ├── Combina @Controller (manejador web)                       │
│  └── + @ResponseBody (retorna datos, no vista)                  │
│                                                                 │
│  @GetMapping("/saludo")                                         │
│  ├── Responde SOLO a peticiones HTTP GET                       │
│  └── Ruta: /saludo                                              │
│                                                                 │
│  public Map<String, String>                                     │
│  ├── Tipo de retorno: Map (colección clave-valor)               │
│  └── Spring usa Jackson para convertir a JSON                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 🔄 Conversión Automática a JSON

```java
// Java Map
Map.of("mensaje", "¡Hola REST!")

// Se convierte automáticamente a:
{
    "mensaje": "¡Hola REST!"
}
```

### ▶️ Ejecutar y Probar

```bash
# 1. Ejecuta la aplicación

# 2. En el navegador:
http://localhost:8080/saludo

# 3. Respuesta JSON:
{
    "mensaje": "¡Hola REST desde Spring Boot!"
}
```

### 💻 Probar con curl

```bash
# Petición GET simple
curl http://localhost:8080/saludo

# Con headers (ver Content-Type)
curl -i http://localhost:8080/saludo

# Respuesta:
# HTTP/1.1 200 
# Content-Type: application/json
# {"mensaje":"¡Hola REST desde Spring Boot!"}
```

---

<a name="herramientas-ia"></a>
## 8. Uso de Herramientas de IA para Asistencia

### 🤖 Herramientas Disponibles

- **Cursor AI**: https://cursor.sh/
- **GitHub Copilot**: https://github.com/features/copilot
- **ChatGPT**: https://chat.openai.com/

### 💡 Usos Prácticos

```
┌─────────────────────────────────────────────────────────────────┐
│                    USOS DE IA EN DESARROLLO                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  📝 Generar Documentación (Javadoc)                            │
│     → Coloca cursor sobre método/clase                          │
│     → Usa comando de IA para generar documentación             │
│                                                                 │
│  📖 Explicar Código                                             │
│     → Selecciona código desconocido                             │
│     → Pide a IA que te explique qué hace                       │
│                                                                 │
│  💻 Generar Esqueletos de Código                                │
│     → "Genera un método POST en este controlador"              │
│     → Siempre revisa y adapta el código                        │
│                                                                 │
│  🐛 Depurar Errores                                             │
│     → Pega el error/stack trace                                 │
│     → Pide explicación y posibles soluciones                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### ⚠️ Precauciones

```
┌─────────────────────────────────────────────────────────────────┐
│                    ⚠️ PRECAUCIONES IMPORTANTES                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✅ Ventajas:                                                   │
│     → Acelera tareas repetitivas                                │
│     → Ayuda a entender conceptos rápidamente                    │
│                                                                 │
│  ❌ Precauciones:                                               │
│     → La IA puede equivocarse                                  │
│     → Siempre verifica el código generado                       │
│     → No sustituye el aprendizaje y comprensión                 │
│     → Adapta las sugerencias a tu contexto                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

<a name="conclusiones"></a>
## 9. Conclusiones y Próximos Pasos

### ✅ Resumen de lo Aprendido

```
┌─────────────────────────────────────────────────────────────────┐
│                    🎯 RESUMEN DE LA CLASE                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✅ Entendimos qué es Spring Boot y sus ventajas               │
│  ✅ Creamos un proyecto desde cero con Spring Initializr        │
│  ✅ Definimos una estructura de paquetes por capas             │
│  ✅ Configuramos propiedades en application.properties          │
│  ✅ Aprendimos los principios básicos de REST                  │
│  ✅ Construimos nuestro primer endpoint GET                     │
│  ✅ Exploramos herramientas de IA para asistencia               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 📋 Cumplimos los Objetivos

| Objetivo | Estado |
|----------|--------|
| Comprender qué es Spring Boot | ✅ |
| Generar proyecto con Initializr | ✅ |
| Estructura de carpetas profesional | ✅ |
| Describir principios REST | ✅ |
| Construir endpoint REST básico | ✅ |
| Usar herramientas de IA | ✅ |

---

### 📚 Próximos Pasos Sugeridos

```
Nivel 2 - Expansión
├── Implementar las otras capas (Service, Repository)
├── Conectar a base de datos (H2, PostgreSQL)
├── Implementar otros verbos HTTP (POST, PUT, DELETE)
└── Añadir validación de datos

Nivel 3 - Producción
├── Manejo de errores (@ControllerAdvice)
├── Documentación de API (Swagger/OpenAPI)
├── Testing (JUnit, Mockito)
└── Seguridad (Spring Security)
```

---

### 🔗 Recursos Útiles

| Recurso | URL |
|---------|-----|
| **Documentación Oficial Spring Boot** | https://docs.spring.io/spring-boot/docs/current/reference/html/ |
| **Spring Guides** | https://spring.io/guides |
| **Baeldung (Tutoriales)** | https://www.baeldung.com/ |
| **Spring Initializr** | https://start.spring.io/ |

---

### 💬 ¡Felicitaciones!

¡Has dado tus primeros pasos con Spring Boot! Ahora puedes:

1. ✅ Crear un proyecto Spring Boot
2. ✅ Entender la estructura por capas
3. ✅ Construir APIs REST básicas

**¡Sigue practicando y nos vemos en la próxima clase!**

---

*Documento creado para el curso de Spring Boot - Clase 1*
*Última actualización: Marzo 2026*

*Autor: Jarolin Matos Martinez*
