# 🎯 ¿Qué es REST?

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, entenderás qué es REST, cuáles son sus principios fundamentales y cómo se relaciona con las APIs que construiremos en Spring Boot.

---

## 📚 Teoría

### ¿Qué es REST?

**REST** (Representational State Transfer) es un **estilo de arquitectura** para diseñar servicios web. No es un lenguaje ni una biblioteca — es un conjunto de reglas y convenciones.

Piensa en REST como el **idioma universal** que usan las aplicaciones para comunicarse por internet.

```
📱 App Móvil          🌐 API REST           💻 Servidor
   │                      │                      │
   │  "Dame los           │                      │
   │   productos"         │                      │
   │─────────────────────>│                      │
   │                      │  "Busco en BD..."    │
   │                      │─────────────────────>│
   │                      │                      │
   │                      │  [Lista de productos]│
   │                      │<─────────────────────│
   │  [JSON con datos]    │                      │
   │<─────────────────────│                      │
```

### Recursos y URI

En REST, todo es un **recurso**. Un recurso puede ser:
- Un usuario
- Un producto
- Una orden
- etc.

Cada recurso tiene un **identificador único** llamado **URI** (Uniform Resource Identifier).

**Ejemplos de URIs:**
```
https://api.mitienda.com/api/productos        → Lista de productos
https://api.mitienda.com/api/productos/1      → Producto con ID 1
https://api.mitienda.com/api/clientes/5       → Cliente con ID 5
https://api.mitienda.com/api/pedidos/100      → Pedido con ID 100
```

### 6 Principios Fundamentales de REST

| # | Principio | Explicación Simple |
|---|-----------|---------------------|
| 1 | **Cliente-Servidor** | El cliente y el servidor son independientes. El cliente no necesita saber cómo funciona el servidor. |
| 2 | **Sin estado (Stateless)** | Cada petición contiene toda la información necesaria. El servidor no guarda estado entre peticiones. |
| 3 | **Cacheable** | Las respuestas pueden guardarse en caché para mejorar el rendimiento. |
| 4 | **Interfaz Uniforme** | Todos los recursos se acceden de la misma manera (GET, POST, PUT, DELETE). |
| 5 | **Sistema por Capas** | Puede haber intermediarios (proxies, balanceadores) entre cliente y servidor. |
| 6 | **Código bajo Demanda (opcional)** | El servidor puede enviar código ejecutable al cliente. |

---

### Verbos HTTP

Los **verbos HTTP** son las acciones que puedes realizar sobre los recursos. Son como los verbos en español: GET (obtener), POST (crear), PUT (actualizar), DELETE (eliminar).

#### Los 5 verbos principales:

| Verbo | Acción | Ejemplo | ¿Qué hace? |
|-------|--------|---------|------------|
| **GET** | Leer | `GET /api/productos` | Obtiene todos los productos |
| **POST** | Crear | `POST /api/productos` | Crea un nuevo producto |
| **PUT** | Actualizar | `PUT /api/productos/1` | Actualiza el producto 1 |
| **DELETE** | Eliminar | `DELETE /api/productos/1` | Elimina el producto 1 |
| **PATCH** | Modificar parcial | `PATCH /api/productos/1` | Modifica solo algunos campos del producto 1 |

#### Ejemplos prácticos:

```bash
# GET - Obtener todos los productos
GET http://localhost:8080/api/productos

# GET - Obtener un producto específico
GET http://localhost:8080/api/productos/1

# POST - Crear un nuevo producto
POST http://localhost:8080/api/productos
Content-Type: application/json

{
    "nombre": "Laptop",
    "descripcion": "16GB RAM, 512GB SSD",
    "precio": 999.99
}

# PUT - Actualizar un producto
PUT http://localhost:8080/api/productos/1
Content-Type: application/json

{
    "nombre": "Laptop Gaming",
    "descripcion": "32GB RAM, 1TB SSD",
    "precio": 1299.99
}

# DELETE - Eliminar un producto
DELETE http://localhost:8080/api/productos/1
```

### Códigos de Estado HTTP

Los códigos de estado son números que el servidor devuelve para indicar qué pasó con la petición.

#### Códigos más importantes:

| Código | Nombre | Significado | Cuándo usarlo |
|--------|--------|-------------|---------------|
| **200** | OK | Todo bien | La petición fue exitosa |
| **201** | Created | Creado | Se creó un nuevo recurso |
| **204** | No Content | Sin contenido | La acción fue exitosa pero no hay respuesta |
| **400** | Bad Request | Petición incorrecta | El JSON está malformado o faltan datos |
| **401** | Unauthorized | No autorizado | Necesitas iniciar sesión |
| **403** | Forbidden | Prohibido | No tienes permisos |
| **404** | Not Found | No encontrado | El recurso no existe |
| **500** | Internal Server Error | Error del servidor | Falló algo en el servidor |

#### Visualización de códigos:

```
    2xx ✓ ÉXITO
    ┌─────┐
    │ 200 │  OK - Todo bien
    │ 201 │  Created - Recurso creado
    │ 204 │  No Content - Sin respuesta
    └─────┘

    4xx ✗ ERROR DEL CLIENTE
    ┌─────┐
    │ 400 │  Bad Request - Petición inválida
    │ 401 │  Unauthorized - No autenticado
    │ 403 │  Forbidden - Sin permisos
    │ 404 │  Not Found - Recurso no existe
    └─────┘

    5xx ✗ ERROR DEL SERVIDOR
    ┌─────┐
    │ 500 │  Internal Server Error - Falló el servidor
    │ 502 │  Bad Gateway - Falló el proxy
    └─────┘
```

### Formato JSON en REST

**JSON** (JavaScript Object Notation) es el formato estándar para intercambiar datos en REST.

#### Estructura de un JSON:

```json
{
    "nombre": "Laptop Gaming",
    "descripcion": "16GB RAM, 512GB SSD",
    "precio": 1299.99,
    "disponible": true,
    "categoria": "Electrónica"
}
```

#### Colección de objetos:

```json
[
    {
        "id": 1,
        "nombre": "Laptop",
        "precio": 999.99
    },
    {
        "id": 2,
        "nombre": "Mouse",
        "precio": 29.99
    },
    {
        "id": 3,
        "nombre": "Teclado",
        "precio": 79.99
    }
]
```

---

## 💻 Código: Cómo se mapea la teoría con Spring Boot

En nuestro proyecto, Spring Boot maneja automáticamente todo esto:

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // GET /api/productos → listaTodos()
    @GetMapping
    public List<Producto> listaProductos() {
        return productoRepository.findAll();
    }

    // GET /api/productos/{id} → obtenerPorId(id)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/productos → crear(producto)
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto guardado = productoRepository.save(producto);
        return ResponseEntity.status(201).body(guardado);
    }
}
```

---

## 📊 Diagrama: Arquitectura Cliente → API REST → Recursos

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENTE                                         │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐                       │
│  │ Navegador  │    │ App Móvil   │    │  curl       │                       │
│  │ (Chrome)   │    │ (Android)   │    │ (Terminal)  │                       │
│  └──────┬──────┘    └──────┬──────┘    └──────┬──────┘                       │
└─────────┼─────────────────┼─────────────────┼───────────────────────────────┘
          │                 │                 │
          │    REQUEST (HTTP)                 │
          │    GET /api/productos             │
          │    POST /api/productos            │
          └───────────────┼───────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        API REST (Spring Boot)                               │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    DispatcherServlet                                 │    │
│  │  ┌───────────┐    ┌──────────────┐    ┌───────────────────────┐    │    │
│  │  │HandlerMap │───▶│ Controller   │───▶│ Repository             │    │    │
│  │  │   ping    │    │ ProductoCtrl │    │ ProductoRepository    │    │    │
│  │  └───────────┘    └──────────────┘    └───────────┬───────────┘    │    │
│  └────────────────────────────────────────────────────┼────────────────┘    │
│                                                         │                   │
└─────────────────────────────────────────────────────────┼───────────────────┘
                                                          │
                                                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           BASE DE DATOS                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                      H2 (en memoria)                                │    │
│  │  ┌─────────────────┐                                               │    │
│  │  │   PRODUCTO      │                                               │    │
│  │  ├─────────────────┤                                               │    │
│  │  │ ID │ NOMBRE │...│                                               │    │
│  │  │ 1  │ Laptop  │   │                                               │    │
│  │  │ 2  │ Mouse   │   │                                               │    │
│  │  └─────────────────┘                                               │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes

| # | Error | ¿Por qué pasa? | Solución |
|---|-------|----------------|----------|
| 1 | **Confundir PUT vs POST** | PUT crea un recurso específico, POST puede crear en cualquier ubicación | PUT = actualizar/crear en URL exacta. POST = crear nuevo |
| 2 | **Usar código 200 para crear** | Después de crear, el estándar dice 201 | Usa `ResponseEntity.status(201)` |
| 3 | **Código 404 en todos lados** | La ruta no existe o está mal escrita | Verifica el `@RequestMapping` del controller |
| 4 | **JSON malformado** | Falta una coma, comilla o llave | Usa un validador JSON online |
| 5 | **Enviar datos incorrectos** | El tipo de dato no coincide | Verifica tipos: String vs Number vs Boolean |

---

## 💡 Tips para Estudiantes

### 1. Recuerda los verbos con la analogía CRUD:
```
CREATE  → POST
READ    → GET
UPDATE  → PUT
DELETE  → DELETE
```

### 2. Códigos fáciles de recordar:
- **2xx** = Todo bien (SUCCESS)
- **4xx** = Tu error (CLIENT mistake)
- **5xx** = Su error (SERVER mistake)

### 3. Siempre verifica:
- La URL esté correcta
- El verbo HTTP sea el apropiado
- El Content-Type sea `application/json`
- El JSON esté bien formado

### 4. Usa herramientas:
- **Postman** o **Insomnia** para probar APIs
- **curl** desde la terminal
- **Navegador** para GET simples

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué significa REST?

- [ ] A) Un lenguaje de programación
- [ ] B) Un estilo de arquitectura para servicios web
- [ ] C) Una base de datos
- [ ] D) Un framework Java

### Pregunta 2:
¿Qué verbo HTTP usas para CREAR un nuevo recurso?

- [ ] A) GET
- [ ] B) PUT
- [ ] C) POST
- [ ] D) DELETE

### Pregunta 3:
¿Qué código de estado devuelve un recurso NO ENCONTRADO?

- [ ] A) 200
- [ ] B) 201
- [ ] C) 400
- [ ] D) 404

### Pregunta 4:
¿Cuál es el formato estándar para enviar datos en REST?

- [ ] A) XML
- [ ] B) HTML
- [ ] C) JSON
- [ ] D) CSV

### Pregunta 5:
¿Qué verbo HTTP es idempotente (puedes llamarlo múltiples veces con el mismo resultado)?

- [ ] A) POST
- [ ] B) PUT
- [ ] C) GET
- [ ] D) A y B

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Identifica el verbo correcto

Para cada escenario, indica qué verbo HTTP usarías:

1. Obtener la lista de todos los usuarios ___________
2. Registrar un nuevo usuario ___________
3. Actualizar el email de un usuario ___________
4. Eliminar un usuario ___________
5. Ver los detalles de un producto específico ___________

### Ejercicio 2: Identifica el código de estado

Indica qué código de estado (200, 201, 400, 404, 500) devolverías:

1. Pides un producto con ID 45, pero no existe ___________
2. Creaste un nuevo cliente exitosamente ___________
3. Intentas crear un usuario pero el JSON está mal ___________
4. Pides la lista de categorías y funciona bien ___________
5. El servidor tiene un bug y crashea ___________

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
