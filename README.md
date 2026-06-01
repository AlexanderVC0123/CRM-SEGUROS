# CRM Seguros

Proyecto de CRM para una agencia de seguros desarrollado como prototipo académico de DAM.

La aplicación permite gestionar clientes, comerciales/agentes, pólizas y usuarios con login. La idea principal es que cada comercial tenga su propia cartera de clientes, mientras que el usuario `ADMIN` pueda administrar toda la información del sistema.

## Tecnologias utilizadas

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Maven
- Lombok

### Frontend

- React
- Vite
- JavaScript
- CSS
- Fetch API

## Funcionalidades actuales

- Login con JWT.
- Roles de usuario:
  - `ADMIN`
  - `AGENTE`
  - `CLIENTE`
- Gestion de clientes.
- Clientes asociados al comercial que los crea.
- El admin puede ver y gestionar todos los clientes.
- Gestion de comerciales/agentes.
- Gestion de usuarios desde endpoints protegidos para admin.
- Gestion basica de pólizas.
- Las pólizas quedan asociadas a cliente, agente y usuario.
- Frontend con layout tipo CRM, menu lateral y vistas principales.

## Roles y permisos

### ADMIN

El usuario admin tiene permisos completos:

- Ver todos los clientes.
- Crear clientes.
- Editar clientes.
- Eliminar clientes.
- Ver comerciales/agentes.
- Crear comerciales/agentes.
- Editar comerciales/agentes.
- Eliminar comerciales/agentes.
- Ver pólizas de todos los usuarios.
- Eliminar pólizas.
- Gestionar usuarios del sistema.

### AGENTE

El comercial o agente solo trabaja con su propia cartera:

- Ver sus clientes.
- Crear clientes asociados a su usuario.
- Editar sus clientes.
- Eliminar sus clientes.
- Ver sus pólizas.
- Crear pólizas para sus clientes.

### CLIENTE

Rol preparado para una posible ampliación futura, por ejemplo un portal de cliente.

## Configuracion de base de datos

El backend usa PostgreSQL. La configuracion esta en:

```txt
crm-seguros/src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crm_seguros
spring.datasource.username=usuario
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

Para desarrollo se esta usando:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Esto permite que Hibernate actualice las tablas automáticamente mientras se desarrolla.

## Como ejecutar el backend

Desde la carpeta del backend:

```bash
cd crm-seguros
./mvnw spring-boot:run
```

En Windows:

```bash
cd crm-seguros
mvnw.cmd spring-boot:run
```

El backend se ejecuta por defecto en:

```txt
http://localhost:8080
```

## Como ejecutar el frontend

Desde la carpeta del frontend:

```bash
cd frontend/crm-seguros-front
npm install
npm run dev
```

El frontend se ejecuta por defecto en:

```txt
http://localhost:5173
```

## Endpoints principales

### Autenticacion

```txt
POST /auth/login
GET  /auth/me
POST /auth/register
```

Nota: el registro ya no debe usarse como registro público. La gestión correcta de usuarios se realiza desde los endpoints de admin.

### Clientes

```txt
GET    /api/clientes
GET    /api/clientes/{id}
POST   /api/clientes
PUT    /api/clientes/{id}
DELETE /api/clientes/{id}
```

### Pólizas

```txt
GET    /api/polizas
GET    /api/polizas/{id}
POST   /api/polizas
DELETE /api/polizas/{id}
```

### Agentes

```txt
GET    /api/agentes
GET    /api/agentes/{id}
POST   /api/agentes
PUT    /api/agentes/{id}
DELETE /api/agentes/{id}
```

### Usuarios y comerciales

```txt
GET    /api/usuarios
GET    /api/usuarios/comerciales
GET    /api/usuarios/{id}
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

Ejemplo para crear un comercial:

```json
{
  "username": "comercial1",
  "password": "1234",
  "rol": "AGENTE"
}
```

## Comandos utiles

### Backend

Ejecutar tests:

```bash
mvnw.cmd test
```

Arrancar backend:

```bash
mvnw.cmd spring-boot:run
```

### Frontend

Arrancar frontend:

```bash
npm run dev
```

Comprobar lint:

```bash
npm run lint
```

Generar build:

```bash
npm run build
```

## Funcionalidades pendientes

- Ficha única de cliente.
- Pestanas internas para pólizas y siniestros del cliente.
- Campos reales de póliza:
  - compania aseguradora
  - numero de póliza
  - ramo
  - prima total
  - estado
  - fecha de efecto
  - fecha de vencimiento
- Alertas de renovación a 60 dias.
- Listado de pólizas que vencen el mes siguiente.
- Módulo de siniestros.
- Pantalla de administración de comerciales en frontend.
- Edición de clientes desde la interfaz.
- Dashboard principal con resumen de cartera.

## Estado del proyecto

El proyecto esta en fase de prototipo. La base de autenticación, roles, clientes, comerciales y pólizas ya está preparada. El siguiente paso recomendado es mejorar el modelo de pólizas y crear el dashboard con alertas de renovación.
