# 🛠️ Herramientas para Probar APIs: curl y Postman

## 🎯 Objetivo de Aprendizaje

Al finalizar este tema, sabrás usar curl desde la terminal y Postman como interfaz visual para probar todos los endpoints de tu API REST.

---

## 📚 Teoría

### ¿Qué es curl?

**curl** (Client URL) es una herramienta de **línea de comandos** para transferir datos usando diversas protocolos (HTTP, HTTPS, FTP, etc.).

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           CURL EN ACCIÓN                                     │
│                                                                              │
│   Terminal:                                                                  │
│   ─────────                                                                  │
│   $ curl http://localhost:8080/api/productos                                │
│                                                                              │
│   Response:                                                                  │
│   ────────                                                                  │
│   [{"id":1,"nombre":"Laptop","precio":999.99},                              │
│    {"id":2,"nombre":"Mouse","precio":29.99}]                               │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                                                                      │   │
│   │   curl = comando                                                     │   │
│   │       http://... = URL                                               │   │
│   │              ↓                                                        │   │
│   │              Se hace un GET por defecto                              │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ¿Qué es Postman?

**Postman** es una aplicación de **escritorio** (con versión web) que permite probar APIs de forma visual. Es ideal para desarrollo y debugging.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         POSTMAN INTERFAZ                                     │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  Collections  │  Environments  │                                    │   │
│   ├─────────────────────────────────────────────────────────────────────┤   │
│   │                                                                     │   │
│   │   Collections:                                                      │   │
│   │   └─ 🌐 Mi API                                                       │   │
│   │       ├─ GET  /api/productos                                        │   │
│   │       ├─ GET  /api/productos/{id}                                  │   │
│   │       └─ POST /api/productos                                        │   │
│   │                                                                     │   │
│   ├─────────────────────────────────────────────────────────────────────┤   │
│   │                                                                     │   │
│   │   GET /api/productos                                        [Send] │   │
│   │   ┌─────────────────────────────────────────────────────────────┐   │   │
│   │   │ Params | Headers | Body | Auth                              │   │   │
│   │   └─────────────────────────────────────────────────────────────┘   │   │
│   │                                                                     │   │
│   │   Response:                                                         │   │
│   │   ┌─────────────────────────────────────────────────────────────┐   │   │
│   │   │ Status: 200 OK  │  Time: 45ms  │  Size: 234 bytes          │   │   │
│   │   ├─────────────────────────────────────────────────────────────┤   │   │
│   │   │ Body | Cookies | Headers | Test Results                    │   │   │
│   │   ├─────────────────────────────────────────────────────────────┤   │   │
│   │   │ [JSON] [Headers] [Cookies] [Timeline]                       │   │   │
│   │   │                                                             │   │   │
│   │   │ {                                                            │   │   │
│   │   │   "id": 1,                                                   │   │   │
│   │   │   "nombre": "Laptop",                                        │   │   │
│   │   │   "precio": 999.99                                           │   │   │
│   │   │ }                                                            │   │   │
│   │   └─────────────────────────────────────────────────────────────┘   │   │
│   │                                                                     │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ¿curl o Postman? ¿Cuál usar?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     CURL vs POSTMAN                                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   CURL                              POSTMAN                                  │
│   ────                              ───────                                  │
│                                                                              │
│   ✅ Ventajas:                     ✅ Ventajas:                              │
│   - Ya viene instalado (Linux/Mac) - Interfaz visual intuitiva             │
│   - Ideal para scripts             - Fácil guardar requests                 │
│   - Ligero                         - Colecciones organizadas               │
│   - Perfecto para CI/CD            - Historial de requests                  │
│   - Compartir en documentación    - Environments (dev/prod)               │
│                                    - Tests automatizados                     │
│                                                                              │
│   ❌ Desventajas:                  ❌ Desventajas:                          │
│   - Curva de aprendizaje           - Más pesado                             │
│   - Difícil recordar flags         - Necesita instalación                   │
│   - Sin historial visual           - Requiere más clicks                     │
│                                                                              │
│   ──────────────────────────────────────────────────────────────────────    │
│                                                                              │
│   RECOMENDACIÓN:                                                             │
│                                                                              │
│   Usa curl para:                          Usa Postman para:                  │
│   • Scripts automatizados                  • Desarrollo diario               │
│   • Documentación                          • Testing manual                 │
│   • Compartir comandos                     • Organizar por proyectos         │
│   • Integración en CI/CD                   • Colaboración en equipo          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 💻 Ejemplos con curl

### Estructura básica de curl

```bash
curl [opciones] [URL]
```

**Opciones importantes:**
```
-X METODO    Especifica el método HTTP (GET, POST, PUT, DELETE)
-H           Añade un header (Content-Type, Authorization)
-d           Body de la petición (para POST/PUT)
-v           Verbose (muestra todo el request/response)
-s           Silent (oculta progreso)
-o archivo   Guarda output en archivo
```

---

### Ejemplo 1: GET - Listar todos los productos

```bash
curl http://localhost:8080/api/productos
```

**Output esperado:**
```json
[
  {"id":1,"nombre":"Laptop","descripcion":"16GB RAM","precio":999.99},
  {"id":2,"nombre":"Mouse","descripcion":"Inalámbrico","precio":29.99},
  {"id":3,"nombre":"Teclado","descripcion":"Mecánico","precio":79.99}
]
```

---

### Ejemplo 2: GET - Obtener un producto por ID

```bash
curl http://localhost:8080/api/productos/1
```

**Output esperado:**
```json
{"id":1,"nombre":"Laptop","descripcion":"16GB RAM","precio":999.99}
```

---

### Ejemplo 3: GET - Con formato JSON (usando jq)

```bash
# Linux/Mac con jq instalado
curl -s http://localhost:8080/api/productos | jq
```

```bash
# Windows (sin jq)
curl -s http://localhost:8080/api/productos
```

---

### Ejemplo 4: POST - Crear un nuevo producto

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
```

**Desglose:**
```
-X POST              → Método HTTP POST
http://localhost:8080/api/productos   → URL del endpoint
-H "Content-Type: application/json"   → Header requerido para JSON
-d '{"nombre":"USB",...}'              → Body con los datos en JSON
```

**Output esperado:**
```json
{"id":6,"nombre":"USB","descripcion":"32GB","precio":15.99}
```

---

### Ejemplo 5: POST - Con producto complejo

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Monitor Gamer",
    "descripcion": "27 pulgadas, 144Hz, Curvo",
    "precio": 349.99
  }'
```

---

### Ejemplo 6: PUT - Actualizar un producto

```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Pro","descripcion":"32GB RAM, 1TB SSD","precio":1299.99}'
```

---

### Ejemplo 7: DELETE - Eliminar un producto

```bash
curl -X DELETE http://localhost:8080/api/productos/1
```

**No devuelve body** (solo status)

---

### Ejemplo 8: Ver Headers de Response

```bash
curl -i http://localhost:8080/api/productos/1
```

**Output:**
```
HTTP/1.1 200 OK
Content-Type: application/json
Date: Thu, 20 Mar 2025 10:30:00 GMT
Content-Length: 78

{"id":1,"nombre":"Laptop","descripcion":"16GB RAM","precio":999.99}
```

---

### Ejemplo 9: Guardar Response en archivo

```bash
curl -s http://localhost:8080/api/productos > productos.json
cat productos.json
```

---

### Ejemplo 10: Enviar a un API con autenticación

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_AQUI" \
  -d '{"nombre":"USB","descripcion":"32GB","precio":15.99}'
```

---

## 💻 Usando Postman

### Paso 1: Descargar e Instalar

1. Ve a https://www.postman.com/downloads/
2. Descarga la versión para tu sistema operativo
3. Instala y abre Postman

---

### Paso 2: Crear una Request

```
1. Click en "+ New" o "New Request"
2. Selecciona el método HTTP (GET, POST, PUT, DELETE)
3. Ingresa la URL
4. Click en "Send"
```

---

### Paso 3: Configurar Headers

```
1. Ve a la pestaña "Headers"
2. Añade:
   Key: Content-Type
   Value: application/json
3. Si necesitas auth:
   Key: Authorization
   Value: Bearer tu_token_aqui
```

---

### Paso 4: Configurar el Body (para POST/PUT)

```
1. Ve a la pestaña "Body"
2. Selecciona "raw"
3. En el dropdown derecho, selecciona "JSON"
4. Escribe tu JSON:
   
   {
     "nombre": "USB",
     "descripcion": "32GB",
     "precio": 15.99
   }
```

---

### Paso 5: Ver la Response

```
1. Después de "Send", mira la sección de abajo
2. Verás:
   - Status: 201 Created
   - Time: 45ms
   - Size: 85 bytes
   - Body: el JSON de respuesta
```

---

### Paso 6: Guardar en Collection

```
1. Click en "Save"
2. Nombre: "Crear Producto"
3. Collection: (selecciona o crea una nueva)
4. Request Name: POST /api/productos
5. Click "Save"
```

---

### Ejemplo: GET todos los productos en Postman

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  Request: GET /api/productos                                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Method: GET                                                                │
│  URL: http://localhost:8080/api/productos                                    │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │  Params | Headers | Body | Auth                                        ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                                                              │
│                                              [Send]                          │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │  Status: 200 OK  |  Time: 32ms  |  Size: 245 bytes                     ││
│  ├─────────────────────────────────────────────────────────────────────────┤│
│  │  Body | Cookies | Headers | Test Results                               ││
│  ├─────────────────────────────────────────────────────────────────────────┤│
│  │                                                                         ││
│  │  [Pretty] [Raw] [Preview]                                               ││
│  │                                                                         ││
│  │  1│ [                                                               ││
│  │  2│   {                                                             ││
│  │  3│     "id": 1,                                                     ││
│  │  4│     "nombre": "Laptop",                                          ││
│  │  5│     "descripcion": "16GB RAM",                                   ││
│  │  6│     "precio": 999.99                                             ││
│  │  7│   },                                                              ││
│  │  8│   {                                                               ││
│  │  9│     "id": 2,                                                      ││
│  │ 10│     "nombre": "Mouse",                                            ││
│  │ 11│     "precio": 29.99                                               ││
│  │ 12│   }                                                               ││
│  │ 13│ ]                                                                 ││
│  │                                                                         ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Comparación curl vs Postman

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        COMPARACIÓN DETALLADA                                  │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   curl                                                   Postman             │
│   ────                                                   ───────             │
│                                                                              │
│   ══════════════════════════════════════════════════════════════════════    │
│                                                                              │
│   GET http://localhost:8080/api/productos             1. Método: GET        │
│                                                    2. URL: [escribir]       │
│                                                    3. Send                   │
│   ══════════════════════════════════════════════════════════════════════    │
│                                                                              │
│   curl -X POST http://localhost:8080/api/productos   1. Método: POST        │
│        -H "Content-Type: application/json"          2. Tab: Headers        │
│        -d '{"nombre":"USB"}'                         3. Key: Content-Type   │
│                                                    4. Value: application/json│
│                                                    5. Tab: Body             │
│                                                    6. Type: JSON            │
│                                                    7. Body: {"nombre":"USB"}│
│                                                    8. Send                   │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## ❌ Errores Comunes

| # | Error con curl | Causa | Solución |
|---|----------------|-------|----------|
| 1 | **command not found** | curl no instalado | Instalar curl o usar Postman |
| 2 | **404 Not Found** | URL incorrecta | Verificar el endpoint |
| 3 | **400 Bad Request** | JSON malformado | Verificar comillas y comas |
| 4 | **415 Unsupported Media Type** | Falta Content-Type | Añadir `-H "Content-Type: application/json"` |
| 5 | **Connection refused** | App no corriendo | Ejecutar `mvn spring-boot:run` |
| 6 | **Empty response** | Error en el servidor | Revisar logs de Spring |

---

## 💡 Tips

### 1. Tip para Windows con JSON multilínea

```bash
# Windows: usa comillas dobles y escapa las internas
curl -X POST http://localhost:8080/api/productos ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"USB\",\"precio\":15.99}"
```

### 2. Alias útiles

Añade a tu `.bashrc` o `.zshrc`:

```bash
# Crear alias para tu API
alias api-productos='curl -s http://localhost:8080/api/productos'
alias api-producto='curl -s http://localhost:8080/api/productos/'

# Luego úsalos así:
api-productos
api-producto 1
```

### 3. Ver Request completo con verbose

```bash
curl -v -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"USB"}'
```

Verás algo como:
```
> POST /api/productos HTTP/1.1
> Host: localhost:8080
> Content-Type: application/json
>
< HTTP/1.1 201 Created
```

### 4. jq para formatear JSON (Linux/Mac)

```bash
# Instalar jq
sudo apt install jq  # Ubuntu/Debian
brew install jq     # Mac

# Usar jq para formatear
curl -s http://localhost:8080/api/productos | jq

# Filtrar campos
curl -s http://localhost:8080/api/productos | jq '.[].nombre'

# Filtrar por condición
curl -s http://localhost:8080/api/productos | jq '.[] | select(.precio > 50)'
```

---

## ❓ Autoevaluación

### Pregunta 1:
¿Qué flag de curl especifica el método HTTP?

- [ ] A) -u
- [ ] B) -X
- [ ] C) -d
- [ ] D) -v

### Pregunta 2:
¿Qué header es obligatorio para enviar JSON en POST?

- [ ] A) Accept
- [ ] B) Content-Type: application/json
- [ ] C) Authorization
- [ ] D) Host

### Pregunta 3:
¿Qué flag de curl añade un header?

- [ ] A) -d
- [ ] B) -X
- [ ] C) -H
- [ ] D) -v

### Pregunta 4:
¿Cuál es la ventaja principal de Postman sobre curl?

- [ ] A) Es más rápido
- [ ] B) Interfaz visual
- [ ] C) Es gratis
- [ ] D) Funciona offline

### Pregunta 5:
¿Qué comando curl lista todos los productos?

- [ ] A) curl -POST http://localhost:8080/api/productos
- [ ] B) curl -GET http://localhost:8080/api/productos
- [ ] C) curl http://localhost:8080/api/productos
- [ ] D) curl -DELETE http://localhost:8080/api/productos

---

## 📋 Taller Práctico: Probar Todos los Endpoints

### Requisitos Previos

1. Asegúrate de que la aplicación esté corriendo
2. Ten Postman abierto (o terminal con curl)

### Tarea 1: GET - Listar productos

```
curl http://localhost:8080/api/productos
```

Cuenta cuántos productos hay.

### Tarea 2: GET - Obtener uno

```
curl http://localhost:8080/api/productos/1
```

¿Qué devuelve? ¿Cuál es el producto?

### Tarea 3: POST - Crear producto

```
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Webcam HD","descripcion":"1080p","precio":49.99}'
```

¿Qué código de estado devuelve? ¿Qué producto creó?

### Tarea 4: PUT - Actualizar

```
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Pro","descripcion":"32GB","precio":1299.99}'
```

¿Qué cambio?

### Tarea 5: DELETE - Eliminar

```
curl -X DELETE http://localhost:8080/api/productos/2
```

¿Qué código devuelve?

### Tarea 6: Verificar eliminación

```
curl http://localhost:8080/api/productos/2
```

¿El producto 2 todavía existe?

### Tarea 7: GET con producto inexistente

```
curl http://localhost:8080/api/productos/9999
```

¿Qué código devuelve? ¿404?

### Bonus: Guardar colección en Postman

1. Crea una nueva Collection llamada "API Productos"
2. Añade cada endpoint como request
3. Organízalos:
   - GET - Listar todos
   - GET - Obtener por ID
   - POST - Crear
   - PUT - Actualizar
   - DELETE - Eliminar
4. Comparte la colección con tu equipo

---

*Las respuestas a los ejercicios están en el archivo `11-ejercicios-practicos.md`*

*Autor: Jarolin Matos Martinez*
