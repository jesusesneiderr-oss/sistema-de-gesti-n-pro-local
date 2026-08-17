# 🚀 GestiónPro Local

Sistema de gestión desarrollado para **Comidas Rápidas Chucho**, orientado a facilitar la administración de clientes, productos, ventas e inventario mediante una solución de software organizada y escalable.

---

## 📌 Descripción del proyecto

**GestiónPro Local** es un sistema desarrollado como proyecto de formación en el programa de **Análisis y Desarrollo de Software del SENA**.

El proyecto busca apoyar la gestión de pequeños negocios mediante la digitalización de procesos que tradicionalmente pueden realizarse de forma manual.

La aplicación permite administrar información de clientes y productos, realizar operaciones CRUD y establecer comunicación entre una aplicación Java y una base de datos MySQL mediante **JDBC**.

---

## 🎯 Objetivo

Desarrollar una solución de software que permita centralizar y administrar información importante de **Comidas Rápidas Chucho**, aplicando principios de programación orientada a objetos, bases de datos, arquitectura por capas y control de versiones.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| JDBC | Conexión con la base de datos |
| MySQL | Sistema gestor de base de datos |
| Jakarta Servlet | Desarrollo del módulo web |
| Apache Tomcat | Servidor de aplicaciones |
| Postman | Pruebas de la API |
| Git | Control de versiones |
| GitHub | Repositorio remoto |
| Visual Studio Code | Entorno de desarrollo |

---

## 🏗️ Arquitectura del sistema

El proyecto utiliza una estructura organizada por capas para facilitar el mantenimiento y evolución del software.

```text
GestiónPro Local
│
├── src
│   └── com.gestionprolocal
│       │
│       ├── conexion
│       │   └── ConexionBD.java
│       │
│       ├── dao
│       │   ├── ClienteDAO.java
│       │   └── ProductoDAO.java
│       │
│       ├── modelo
│       │   ├── Cliente.java
│       │   └── Producto.java
│       │
│       ├── servlet
│       │   ├── ClienteServlet.java
│       │   └── ProductoServlet.java
│       │
│       └── principal
│           └── Main.java
│
├── lib
│   ├── mysql-connector-j-9.7.0.jar
│   └── servlet-api.jar
│
├── webapp
│   └── index.html
│
└── README.md
```

### Flujo de funcionamiento

```
Frontend
   ↓
Servlet
   ↓
DAO
   ↓
JDBC
   ↓
MySQL
```

---

## 👥 Módulo de gestión de clientes

El módulo de clientes permite administrar la información básica de los clientes registrados en el sistema.

### Funcionalidades

- Registrar clientes.
- Consultar clientes.
- Actualizar clientes.
- Eliminar clientes.

Estas funcionalidades corresponden a las operaciones CRUD: **CREATE, READ, UPDATE, DELETE**.

### 🌐 API Web — Clientes

El módulo de clientes se encuentra disponible mediante el servlet:

```
/clientes
```

**GET — Consultar clientes**
```
GET /clientes
```
Permite consultar los clientes registrados en la base de datos.

**POST — Registrar cliente**
```
POST /clientes
```
Parámetros: `nombre`, `telefono`, `correo`

**PUT — Actualizar cliente**
```
PUT /clientes
```
Parámetros: `id`, `nombre`, `telefono`, `correo`

**DELETE — Eliminar cliente**
```
DELETE /clientes?id=1
```
Permite eliminar un cliente mediante su identificador.

### 🗄️ Base de datos — Clientes

Tabla: `clientes`

| Campo | Descripción |
|---|---|
| id | Identificador único del cliente |
| nombre | Nombre del cliente |
| telefono | Número telefónico |
| correo | Correo electrónico |

---

## 📦 Módulo de gestión de productos

El módulo de productos permite administrar el inventario básico de productos ofrecidos por el negocio.

### Funcionalidades

- Registrar productos.
- Consultar productos.
- Actualizar productos.
- Eliminar productos.

Estas funcionalidades corresponden a las operaciones CRUD: **CREATE, READ, UPDATE, DELETE**.

### 🌐 API Web — Productos

El módulo de productos se encuentra disponible mediante el servlet:

```
/productos
```

**GET — Consultar productos**
```
GET /productos
```
Permite consultar los productos registrados en la base de datos.

**POST — Registrar producto**
```
POST /productos
```
Parámetros: `nombre`, `precio`, `stock`

**PUT — Actualizar producto**
```
PUT /productos
```
Parámetros: `id`, `nombre`, `precio`, `stock`

**DELETE — Eliminar producto**
```
DELETE /productos?id=1
```
Permite eliminar un producto mediante su identificador.

### 🗄️ Base de datos — Productos

Tabla: `productos`

| Campo | Descripción |
|---|---|
| id | Identificador único del producto |
| nombre | Nombre del producto |
| precio | Precio unitario del producto |
| stock | Cantidad disponible en inventario |

---

## 🔌 Conexión mediante JDBC

La comunicación entre la aplicación Java y MySQL se realiza mediante JDBC utilizando el controlador:

```
mysql-connector-j-9.7.0.jar
```

La clase encargada de establecer la conexión es `ConexionBD.java`, ubicada en el paquete `com.gestionprolocal.conexion`.

La conexión utiliza la siguiente dirección:

```
jdbc:mysql://localhost:3306/gestionprolocal
```

La aplicación establece la conexión con la base de datos, ejecuta las operaciones necesarias y posteriormente cierra la conexión.

La conexión fue comprobada directamente desde la terminal de Visual Studio Code utilizando Java 17 y el controlador MySQL Connector/J.

**Resultado de la prueba:**
```
======================================
 CONEXION EXITOSA A GESTIONPRO LOCAL
======================================
Conexion cerrada correctamente.
```

---

## 💻 Estructura del desarrollo

| Paquete | Contenido | Función |
|---|---|---|
| `com.gestionprolocal.conexion` | `ConexionBD.java` | Establece la comunicación entre Java y MySQL |
| `com.gestionprolocal.dao` | `ClienteDAO.java`, `ProductoDAO.java` | Ejecutan las operaciones sobre la base de datos |
| `com.gestionprolocal.modelo` | `Cliente.java`, `Producto.java` | Representan los datos utilizados por el sistema |
| `com.gestionprolocal.servlet` | `ClienteServlet.java`, `ProductoServlet.java` | Reciben las solicitudes HTTP y las comunican con la capa DAO |

Para ejecutar las consultas se utiliza `PreparedStatement`, permitiendo trabajar con parámetros y mejorar la seguridad de las operaciones realizadas sobre la base de datos.

---

## 🧪 Pruebas realizadas

Las funcionalidades fueron probadas utilizando **Postman** y posteriormente verificadas directamente en MySQL.

### Resultados obtenidos — Clientes

| Funcionalidad | Método | Resultado |
|---|---|---|
| Registrar cliente | POST | Correcto |
| Consultar clientes | GET | Correcto |
| Actualizar cliente | PUT | Correcto |
| Eliminar cliente | DELETE | Correcto |
| Conexión con MySQL | JDBC | Correcto |

### Resultados obtenidos — Productos

| Funcionalidad | Método | Resultado |
|---|---|---|
| Registrar producto | POST | Correcto |
| Consultar productos | GET | Correcto |
| Actualizar producto | PUT | Correcto |
| Eliminar producto | DELETE | Correcto |
| Conexión con MySQL | JDBC | Correcto |

Las pruebas permitieron comprobar que los datos enviados mediante la aplicación son procesados correctamente y que los cambios se reflejan en la base de datos, tanto para el módulo de clientes como para el de productos.

---

## 📐 Estándares de codificación

Durante el desarrollo se aplicaron estándares para mantener un código organizado y fácil de comprender.

**Clases** — Nombres descriptivos en PascalCase. Ejemplo: `ClienteDAO`, `ProductoDAO`

**Métodos** — Convención camelCase. Ejemplo: `registrarCliente()`, `registrarProducto()`

**Variables** — Nombres descriptivos en camelCase. Ejemplo: `nombreCliente`, `precioProducto`

**Paquetes** — Se escriben en minúsculas. Ejemplo: `com.gestionprolocal.dao`

**Comentarios** — El código contiene comentarios para explicar las principales funciones y operaciones implementadas.

---

## 🔐 Seguridad

Para las consultas a la base de datos se utiliza `PreparedStatement`, evitando construir directamente las consultas SQL con los valores recibidos por el usuario.

Ejemplo:

```java
String sql = "INSERT INTO clientes (nombre, telefono, correo) VALUES (?, ?, ?)";
```

Esto permite trabajar con parámetros de manera controlada y reduce el riesgo de inyección SQL.

---

## 📦 Control de versiones

El proyecto utiliza **Git** como herramienta de control de versiones y **GitHub** como repositorio remoto.

El uso de Git permite:

- Registrar cambios realizados en el proyecto.
- Mantener un historial de versiones.
- Recuperar versiones anteriores.
- Organizar el proceso de desarrollo.
- Mantener una copia remota del proyecto.

**Repositorio:** [Sistema de GestiónPro Local](https://github.com/jesusesneiderr-oss/sistema-de-gesti-n-pro-local)

---

## 📚 Relación con el ciclo de desarrollo de software

La implementación del módulo toma como referencia los artefactos desarrollados durante las etapas anteriores del proyecto, entre ellos:

- Historias de usuario.
- Casos de uso.
- Diagrama de clases.
- Diseños y prototipos.
- Arquitectura del sistema.
- Modelo de datos.
- Plan de trabajo.
- Requerimientos funcionales.

Estos elementos sirven como base para realizar la codificación y mantener coherencia entre los requerimientos y las funcionalidades implementadas.

---

## 🎓 Propósito académico

El proyecto hace parte del proceso de formación del programa de **Análisis y Desarrollo de Software del SENA**.

Durante su desarrollo se aplican conocimientos relacionados con:

- Programación orientada a objetos.
- Desarrollo web.
- Bases de datos.
- MySQL.
- JDBC.
- Java.
- Jakarta Servlet.
- Operaciones CRUD.
- Arquitectura de software.
- Pruebas funcionales.
- Control de versiones.

---

## 👨‍💻 Autor

**Jesús Esneider Rojas González**

- Programa: Análisis y Desarrollo de Software
- Institución: SENA
- Proyecto: GestiónPro Local
- Negocio: Comidas Rápidas Chucho

---

## 🚧 Estado del proyecto

**En desarrollo**

Los módulos de gestión de clientes y productos cuentan con conexión funcional a MySQL mediante JDBC y operaciones CRUD verificadas mediante Postman y MySQL.

El proyecto continuará evolucionando mediante la incorporación de nuevos módulos y funcionalidades orientadas a la administración de ventas, inventario y demás procesos del negocio.