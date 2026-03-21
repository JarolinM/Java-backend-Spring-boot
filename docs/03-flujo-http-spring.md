# 🔄 Flujo HTTP en Spring Boot

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, entenderás paso a paso qué sucede internamente cuando un cliente hace una petición HTTP a tu aplicación Spring Boot.

---

## 📚 Teoría

### ¿Qué es el Front Controller Pattern?

El **Front Controller Pattern** es un patrón de diseño donde **un solo componente** (el DispatcherServlet) recibe TODAS las peticiones entrantes y las distribuye a los controladores específicos.

```
Sin Front Controller (caótico):          Con Front Controller (ordenado):

   ┌──────┐    ┌──────┐                     ┌──────┐
   │ Petr │    │ Juan │                     │ Petr │    │ Juan │
   └──┬───┘    └──┬───┘                     └──┬───┘    └──┬───┘
      │           │                            │          │
   ┌──┴───┐    ┌──┴───┐                      ▼          ▼
   │Ctrl A│    │Ctrl B│                 ┌───────────────┐
   └──────┘    └──────┘                 │ Dispatcher    │
                                        │  Servlet      │
                                        └───┬───────┬───┘
                                            │       │
                                         ┌──┴───┐┌─┴───┐
                                         │Ctrl A│ │Ctrl B│
                                         └──────┘ └──────┘
```

### DispatcherServlet Explicado

El **DispatcherServlet** es el "recepcionista" de Spring Boot. Está disponible **desde el primer momento** gracias a `spring-boot-starter-web`.

#### ¿Qué hace el DispatcherServlet?

```
1. Recibe la petición HTTP del cliente
        ↓
2. Busca el controlador adecuado (HandlerMapping)
        ↓
3. Ejecuta el método del controlador
        ↓
4. Convierte el resultado a JSON (MessageConverter)
        ↓
5. Devuelve la respuesta HTTP al cliente
```

### HandlerMapping

El **HandlerMapping** es como el "índice telefónico" de Spring. Mapea URLs a controladores:

```
┌────────────────────────────────────────────────────────────┐
│                    HandlerMapping                          │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  "/api/productos"      →  ProductoController.listaProductos()│
│  "/api/productos/{id}" →  ProductoController.obtenerPorId() │
│  "/api/productos"      →  ProductoController.crear()       │
│                                                            │
│  "/api/clientes"       →  ClienteController (si existiera)  │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### MessageConverter (JSON)

Spring Boot usa **Jackson** para convertir objetos Java ↔ JSON automáticamente:

```
Java Object                              JSON
┌────────────────┐                  {"nombre": "Laptop",
│ Producto       │                      "precio": 999.99
│ - nombre       │    ──────────────►    }
│ - precio       │
└────────────────┘    Jackson

JSON                                 Java Object
{"nombre": "Laptop",               ┌────────────────┐
 "precio": 999.99                   │ Producto       │
}     ──────────────►              │ nombre = "Laptop"
                                   │ precio = 999.99
                                   └────────────────┘
```

---

## 💻 El Flujo Completo Paso a Paso

Cuando ejecutas este comando:

```bash
curl http://localhost:8080/api/productos
```

Aquí está lo que sucede internamente:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        PASO 1: Cliente envía petición                        │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                          ┌─────────────────┐
                          │ curl / navegador │
                          │ GET /api/productos│
                          └────────┬─────────┘
                                   │
                                   │ TCP/IP
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      PASO 2: DispatcherServlet recibe                        │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                   ┌────────────────────────────────────┐
                   │      localhost:8080                │
                   │  ┌────────────────────────────┐   │
                   │  │     DispatcherServlet     │   │
                   │  │    (Front Controller)      │   │
                   │  └────────────┬───────────────┘   │
                   └───────────────┼───────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                     PASO 3: HandlerMapping busca el Controller                │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                   ┌────────────────────────────────────┐
                   │        HandlerMapping              │
                   │                                    │
                   │  "/api/productos" ───────────────▶ │
                   │                    ProductoController│
                   └─────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    PASO 4: Controller ejecuta el método                       │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
          ┌─────────────────────────────────────────────┐
          │         ProductoController                  │
          │                                             │
          │  @GetMapping                                │
          │  public List<Producto> listaProductos() {  │
          │      return productoRepository.findAll();  │
          │  }                                          │
          └─────────────────────┬───────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                   PASO 5: Repository consulta la base de datos              │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
          ┌─────────────────────────────────────────────┐
          │       ProductoRepository                     │
          │                                             │
          │  productoRepository.findAll()               │
          │           │                                  │
          └───────────┼──────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                     PASO 6: JPA/Hibernate consulta la BD                     │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
          ┌─────────────────────────────────────────────┐
          │              JPA (Hibernate)                │
          │                                             │
          │  SELECT * FROM PRODUCTO;                    │
          └─────────────────────┬───────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    PASO 7: Base de datos devuelve resultados                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
          ┌─────────────────────────────────────────────┐
          │              H2 Database                    │
          │                                             │
          │  ┌────────────────────────────────────┐   │
          │  │ ID │ NOMBRE    │ PRECIO            │   │
          │  ├────┼───────────┼───────────────────┤   │
          │  │ 1  │ Laptop    │ 999.99            │   │
          │  │ 2  │ Mouse     │ 29.99             │   │
          │  │ 3  │ Teclado   │ 79.99             │   │
          │  └────────────────────────────────────┘   │
          └─────────────────────┬──────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                PASO 8: Resultado sube por la cadena de vuelta                  │
└─────────────────────────────────────────────────────────────────────────────┘
                               │
          ┌────────────────────┴────────────────────┐
          │                                        │
          │  List<Producto> ◀── JPA/Hibernate      │
          │         │                               │
          │  Jackson convierte a JSON              │
          │         ▼                               │
          │  [JSON con todos los productos]        │
          │                                        │
          └────────────────────┬────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                  PASO 9: Response HTTP devuelve JSON                         │
└─────────────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
          ┌─────────────────────────────────────┐
          │  HTTP/1.1 200 OK                    │
          │  Content-Type: application/json     │
          │                                     │
          │  [                                   │
          │    {"id":1,"nombre":"Laptop",...},  │
          │    {"id":2,"nombre":"Mouse",...},    │
          │    {"id":3,"nombre":"Teclado",...}   │
          │  ]                                   │
          └─────────────────────────────────────┘
```

---

## 📊 Diagrama de Secuencia UML (ASCII)

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ Cliente  │    │Dispatcher│    │Controller│    │Repository│    │   JPA    │    │    BD    │
└────┬─────┘    └────┬─────┘    └────┬─────┘    └────┬─────┘    └────┬─────┘    └────┬─────┘
     │               │               │               │               │               │
     │               │               │               │               │               │
     │──GET /api/productos ────────>│               │               │               │
     │               │               │               │               │               │
     │               │──dispatch────>│               │               │               │
     │               │               │               │               │               │
     │               │               │──findAll()───>│               │               │
     │               │               │               │               │               │
     │               │               │               │──query───────>│               │
     │               │               │               │               │               │
     │               │               │               │               │──SELECT─────>│
     │               │               │               │               │               │
     │               │               │               │               │<──resultset──│
     │               │               │               │<──List────────│               │
     │               │               │<──List────────│               │               │
     │               │               │               │               │               │
     │               │<──JSON────────│               │               │               │
     │<──200 OK──────│               │               │               │               │
     │               │               │               │               │               │
     │               │               │               │               │               │
     │               │               │               │               │               │
```

**Leyenda:**
- `──>` = Llamada/mensaje enviado
- `<──` = Respuesta/retorno

---

## 📊 Flujo Detallado para POST (Crear)

```
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
```

```
Cliente          Dispatcher         Controller        Repository       JPA              BD
   │                  │                  │                 │               │               │
   │──POST───────────>│                  │                 │               │               │
   │  JSON payload    │                  │                 │               │               │
   │                  │                  │                 │               │               │
   │                  │──dispatch───────>│                 │               │               │
   │                  │                  │                 │               │               │
   │                  │                  │──save(producto)─>│               │               │
   │                  │                  │                 │               │               │
   │                  │                  │                 │──persist──────>│               │
   │                  │                  │                 │               │               │
   │                  │                  │                 │               │──INSERT─────>│
   │                  │                  │                 │               │               │
   │                  │                  │                 │               │<──OK─────────│
   │                  │                  │                 │<──Producto────│               │
   │                  │                  │<──Producto───────│               │               │
   │                  │<──201 Created────│                 │               │               │
   │<──201────────────│                  │                 │               │               │
   │                  │                  │                 │               │               │
```

---

## ❌ Errores Comunes

| # | Error | Causa | Solución |
|---|-------|-------|----------|
| 1 | **404 Not Found** | Ruta mal escrita | Verificar `@RequestMapping` del controller |
| 2 | **NullPointerException** | Repository no inyectado | Añadir `@Autowired` |
| 3 | **Whitelabel Error Page** | Exception no manejada | Añadir `@ExceptionHandler` |
| 4 | **500 Internal Server Error** | Error en la lógica | Revisar stack trace |
| 5 | **400 Bad Request** | JSON malformado | Validar sintaxis JSON |
| 6 | **415 Unsupported Media Type** | Falta header Content-Type | Añadir `-H "Content-Type: application/json"` |

---

## 💡 Tips

### 1. Cómo leer un stack trace:

```
Whitelabel Error Page
 │
 ▼
 Causa: NullPointerException at ProductoController.listaProductos()

 Significa: productoRepository es NULL
 │
 ▼
 Solución: Falta @Autowired
```

### 2. Usa los logs:

```java
System.out.println("===== DEBUG =====");
// o mejor, usa el logger:
Logger logger = LoggerFactory.getLogger(getClass());
logger.info("Productos encontrados: {}", productos.size());
```

### 3. Orden de ejecución de componentes:

```
HTTP Request
    │
    ▼
┌────────────────────────────────┐
│ 1. DispatcherServlet           │
└────────────┬───────────────────┘
             │
             ▼
┌────────────────────────────────┐
│ 2. HandlerMapping (busca URL)  │
└────────────┬───────────────────┘
             │
             ▼
┌────────────────────────────────┐
│ 3. Controller (ejecuta método)│
└────────────┬───────────────────┘
             │
             ▼
┌────────────────────────────────┐
│ 4. Repository (consulta BD)    │
└────────────┬───────────────────┘
             │
             ▼
┌────────────────────────────────┐
│ 5. JPA/Hibernate (SQL)         │
└────────────┬───────────────────┘
             │
             ▼
┌────────────────────────────────┐
│ 6. Base de datos (resultado)   │
└────────────┬───────────────────┘
             │
             ▼
        Response JSON
```

### 4. Comandos curl para probar:

```bash
# GET todos
curl http://localhost:8080/api/productos

# GET uno
curl http://localhost:8080/api/productos/1

# POST crear
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué componente recibe TODAS las peticiones HTTP en Spring Boot?

- [ ] A) Controller
- [ ] B) DispatcherServlet
- [ ] C) HandlerMapping
- [ ] D) Repository

### Pregunta 2:
¿Qué hace HandlerMapping?

- [ ] A) Convierte JSON a objetos
- [ ] B) Mapea URLs a controladores
- [ ] C) Guarda en base de datos
- [ ] D) Ejecuta SQL

### Pregunta 3:
¿Qué librería convierte objetos Java a JSON?

- [ ] A) Hibernate
- [ ] B) JPA
- [ ] C) Jackson
- [ ] D) Maven

### Pregunta 4:
¿En qué orden se ejecutan los componentes?

- [ ] A) Repository → Controller → DispatcherServlet
- [ ] B) DispatcherServlet → HandlerMapping → Controller → Repository
- [ ] C) Controller → Repository → DispatcherServlet
- [ ] D) Repository → DispatcherServlet → Controller

### Pregunta 5:
¿Qué código devuelve un POST exitoso?

- [ ] A) 200
- [ ] B) 201
- [ ] C) 404
- [ ] D) 500

---

## 📋 Ejercicios Propuestos

### Ejercicio 1: Traza el flujo

Dibuja el flujo completo (como el diagrama ASCII) para esta petición:

```bash
curl http://localhost:8080/api/productos/2
```

### Ejercicio 2: Identifica el error

Analiza este código y encuentra el error:

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private ProductoRepository productoRepository;
    
    @GetMapping
    public List<Producto> listaProductos() {
        return productoRepository.findAll();
    }
}
```

**¿Qué falta? ¿Por qué?**

### Ejercicio 3: Simula el flujo

Sin ejecutar código, predice qué devolverá:

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Monitor","descripcion":"27\"","precio":299.99}'
```

**¿Qué código de estado esperas? ¿Qué JSON devuelve?**

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*
