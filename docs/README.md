# 📚 Curso de Spring Boot - Documentación de Fundamentos

Bienvenido al repositorio de documentación del curso de Spring Boot. Aquí encontrarás materiales educativos diseñados para estudiantes que quieren aprender a construir APIs REST profesionales con Spring Boot.

---

## 🎯 Objetivo del Curso

Aprender paso a paso a crear aplicaciones web robustas con Spring Boot, desde los conceptos básicos hasta temas avanzados de producción.

---

## 📖 Clase 1: Introducción a Spring Boot

### 📋 Contenido de la Clase

La **Clase 1** cubre los fundamentos esenciales para comenzar con Spring Boot:

| Tema | Descripción |
|------|-------------|
| **Preparación del Entorno** | Instalación de JDK 17+, IDE, herramientas de prueba |
| **¿Qué es Spring Boot?** | Historia, ventajas, comparación con otras tecnologías |
| **Spring Initializr** | Creación de proyectos con la herramienta oficial |
| **Estructura por Capas** | Arquitectura MVC: Controller → Service → Repository |
| **Configuración** | application.properties y propiedades comunes |
| **Principios REST** | Recursos, URIs, verbos HTTP, códigos de estado |
| **Primera API** | Creación del primer endpoint GET con @RestController |
| **Herramientas de IA** | Uso de Cursor AI y asistentes para desarrollo |

### 🎯 Objetivos de Aprendizaje

Al finalizar esta clase, serás capaz de:

- ✅ Comprender qué es Spring Boot, sus ventajas y por qué es tan utilizado
- ✅ Generar y configurar tu primer proyecto Spring Boot
- ✅ Entender y aplicar una estructura de carpetas profesional
- ✅ Describir los principios básicos de las APIs REST
- ✅ Construir y probar un endpoint REST básico que retorne datos en JSON
- ✅ Utilizar herramientas de IA para asistirte en tareas de desarrollo

---

## 📁 Estructura de la Documentación

```
docs-fundamentos/
│
├── 📄 README.md                          # Este archivo - Índice general
│
├── 📚 CLASE 01: FUNDAMENTOS
│   └── clase-01-introduccion-springboot.md   # Documento completo de la Clase 1
│
├── 📚 MÓDULOS TEÓRICOS
│   ├── 01-que-es-rest.md                 # Fundamentos REST
│   ├── 02-anatomia-proyecto.md          # Estructura del proyecto
│   ├── 03-flujo-http-spring.md           # Flujo de peticiones HTTP
│   ├── 04-modelo-datos.md               # @Entity, JPA, mapeo
│   ├── 05-repository.md                 # JpaRepository, acceso a datos
│   ├── 06-controller.md                 # @RestController, endpoints
│   ├── 07-h2-console.md                 # Tutorial de H2 Console
│   └── 08-herramientas.md                # curl, Postman
│
├── 📚 REFERENCIA RÁPIDA
│   ├── 09-glosario-anotaciones.md       # Cheat sheet de anotaciones
│   ├── 10-errores-comunes.md            # Troubleshooting
│   └── RESUMEN.md                       # Hoja de referencia rápida
│
└── 📝 EVALUACIÓN
    └── 11-ejercicios-practicos.md       # Preguntas y ejercicios
```

---

## 🗺️ Ruta de Aprendizaje Sugerida

### 📗 Nivel 1: Fundamentos (Clase 1)

```
Paso 1 → Leer: clase-01-introduccion-springboot.md
Paso 2 → Seguir: Tutorial de Spring Initializr
Paso 3 → Crear: Tu primer proyecto Spring Boot
Paso 4 → Practicar: Crear endpoint /saludo
Paso 5 → Probar: Con navegador y curl
```

### 📘 Nivel 2: Expansión (Próximas Clases)

```
Próximos temas:
├── Servicio REST de Productos (CRUD completo)
├── Conexión a Base de Datos (H2, PostgreSQL)
├── Capa de Servicio (@Service)
├── Validación de Datos (@Valid)
└── Manejo de Errores (@ControllerAdvice)
```

### 📕 Nivel 3: Producción (Avanzado)

```
Temas avanzados:
├── Documentación con Swagger/OpenAPI
├── Testing con JUnit y Mockito
├── Seguridad con Spring Security
├── Caching con Redis
└── Despliegue en la nube
```

---

## 🚀 Inicio Rápido

### 1️⃣ Crear Proyecto con Spring Initializr

```
1. Ve a https://start.spring.io/
2. Configura:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.x (latest stable)
   - Group: com.tuorganizacion
   - Artifact: mi-primer-proyecto
   - Dependencies: Spring Web
3. Click en "Generate"
4. Descomprime y abre en tu IDE
```

### 2️⃣ Tu Primer Endpoint

Crea el archivo `SaludoController.java`:

```java
package com.tuorganizacion.miproyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class SaludoController {

    @GetMapping("/saludo")
    public Map<String, String> obtenerSaludo() {
        return Map.of("mensaje", "¡Hola REST desde Spring Boot!");
    }
}
```

### 3️⃣ Ejecutar y Probar

```bash
# Ejecuta desde tu IDE o con:
./mvnw spring-boot:run

# Prueba en el navegador:
http://localhost:8080/saludo

# O con curl:
curl http://localhost:8080/saludo
```

**Respuesta esperada:**
```json
{
    "mensaje": "¡Hola REST desde Spring Boot!"
}
```

---

## 🔗 Recursos Oficiales

| Recurso | URL |
|---------|-----|
| **Documentación Spring Boot** | https://docs.spring.io/spring-boot/docs/current/reference/html/ |
| **Spring Initializr** | https://start.spring.io/ |
| **Spring Guides** | https://spring.io/guides |
| **Baeldung Tutoriales** | https://www.baeldung.com/ |

---

## 🛠️ Herramientas Recomendadas

### IDEs

| IDE | Descripción | Enlace |
|-----|-------------|--------|
| **IntelliJ IDEA** | El más popular para Java/Spring | https://www.jetbrains.com/idea/ |
| **VS Code** | Ligero con extensiones Java | https://code.visualstudio.com/ |
| **Cursor AI** | Con IA integrada | https://cursor.sh/ |

### Herramientas de Prueba

| Herramienta | Descripción | Enlace |
|------------|-------------|--------|
| **Postman** | Cliente API completo | https://www.postman.com/downloads/ |
| **Insomnia** | Alternativa a Postman | https://insomnia.rest/download |
| **curl** | Línea de comandos (incluido en Windows 10+) | - |

### Bases de Datos

| DB | Descripción | Uso |
|----|-------------|-----|
| **H2** | Base de datos en memoria | Desarrollo/Pruebas |
| **MySQL** | Base de datos relacional popular | Producción |
| **PostgreSQL** | Base de datos avanzada | Producción |

---

## ❓ FAQ - Preguntas Frecuentes

### ¿Qué JDK necesito?
**Java 17 o superior (se recomienda LTS: 17 o 21)**

### ¿Puedo usar Gradle en vez de Maven?
**Sí**, Spring Initializr permite elegir entre Maven y Gradle. Esta documentación usa Maven por simplicidad.

### ¿Necesito instalar Tomcat?
**No**, Spring Boot incluye servidores embebidos (Tomcat por defecto).

### ¿Qué pasa si me aparece "Whitelabel Error Page"?
**Es normal** si no hay ningún endpoint configurado. Significa que el servidor está funcionando.

---

## 📝 Glosario Rápido

| Término | Definición |
|---------|------------|
| **REST** | Estilo arquitectónico para APIs web |
| **Spring Boot** | Framework que simplifica el desarrollo Spring |
| **Endpoint** | URL específica que responde a una petición |
| **JSON** | Formato de datos para intercambiar información |
| **Maven** | Herramienta para gestionar dependencias |
| **JPA** | Java Persistence API - estándar para ORM |

---

## 🤝 Contribuir

Este es un proyecto educativo en evolución. Si encuentras errores o quieres mejorar el contenido:

1. Crea un branch: `git checkout -b mejora/contenido`
2. Haz tus cambios
3. Commit: `git commit -m "Mejora: ..."`
4. Push: `git push origin mejora/contenido`
5. Crea una Pull Request

---

## 📄 Licencia

Este material fue creado para fines educativos.

---

## 🎓 Sobre el Curso

- **Instructor:** [Tu nombre]
- **Nivel:** Principiante a Intermedio
- **Duración:** [X] clases
- **Prerrequisitos:** Conocimientos básicos de Java

---

*¡Éxito con tu aprendizaje de Spring Boot!* 🚀

**Última actualización:** Marzo 2026