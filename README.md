# TP3 · Seguridad, persistencia y autorización

API Spring Boot 4.1.1 y Java 25 para catálogo de productos, favoritos y listas. La persistencia usa PostgreSQL y Flyway. Spring Security protege las operaciones de listas y favoritos mediante JWT stateless; el catálogo, la salud, Swagger y autenticación son públicos.

## Requisitos y ejecución

Se necesita Java 25. Para pruebas de integración se necesita Docker con el daemon disponible. Maven se ejecuta con el wrapper incluido:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd test
```

`test` ejecuta pruebas unitarias (`*Test`). `verify` agrega las pruebas de integración `*IT` con PostgreSQL Testcontainers y aplica todas las migraciones Flyway en una base efímera, sin depender de una base local.

Para levantar PostgreSQL local:

```powershell
docker compose up -d
```

Variables de entorno (cada variable tiene el valor local de desarrollo indicado como default en `application.properties`):

| Variable | Default | Uso |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/webii_tp2` | URL JDBC |
| `DB_USERNAME` | `webii_tp2` | Usuario de PostgreSQL |
| `DB_PASSWORD` | `webii_tp2` | Contraseña de PostgreSQL |
| `JWT_SECRET` | valor solo para desarrollo, incluido en `application.properties` | Clave HMAC de al menos 32 bytes; usar un secreto aleatorio externo en cada entorno real |
| `JWT_DURATION_MS` | `3600000` | Vigencia del token en milisegundos |

```powershell
$env:JWT_SECRET = 'reemplazar-por-un-secreto-aleatorio-de-32-bytes-o-mas'
$env:DB_URL = 'jdbc:postgresql://localhost:5432/webii_tp2'
.\mvnw.cmd spring-boot:run
```

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html). Incluye el esquema bearer JWT para probar rutas protegidas. OpenAPI JSON: `/v3/api-docs`.

## Autenticación y autorización

- `POST /api/auth/registro` recibe `email` y `password`, guarda BCrypt y asigna el rol `USER`; devuelve token e identidad. Email duplicado: 409.
- `POST /api/auth/login` verifica credenciales y devuelve un JWT con el id como `sub` y el rol como claim. Credenciales inválidas: 401.
- Enviar el token como `Authorization: Bearer <token>`. `/api/auth/**`, `/api/productos/**`, `/health`, `/ping` y Swagger son públicos. Listas y favoritos requieren token.
- Usuarios `USER` solo acceden a sus listas y favoritos; `ADMIN` puede acceder a todos. Un recurso existente de otro usuario devuelve 403; un id inexistente devuelve 404. En actualizaciones y movimientos se verifica tanto el recurso actual/origen como el destino.
- El registro público no permite elegir `ADMIN`. El alta de administradores es una tarea operativa: crear el usuario con un hash BCrypt seguro directamente en la tabla `usuarios`, siguiendo el proceso controlado del entorno.

| Método | Ruta | Acceso |
|---|---|---|
| GET | `/health`, `/ping` | Público |
| GET | `/api/productos`, `/api/productos/{id}` | Público |
| POST | `/api/auth/registro`, `/api/auth/login` | Público |
| GET / POST | `/api/favoritos` | Autenticado y limitado por propietario |
| GET / PUT / DELETE | `/api/favoritos/{id}` | Autenticado y limitado por propietario |
| GET / POST | `/api/listas` | Autenticado y limitado por propietario |
| GET / DELETE | `/api/listas/{id}` | Autenticado y limitado por propietario |
| GET | `/api/listas/{id}/favoritos` | Autenticado y limitado por propietario |
| POST | `/api/listas/{origenId}/mover-favoritos` | Autenticado; verifica origen y destino |

## Modelo y migraciones

Usuario sigue el patrón domain, port y adapter JPA. `Lista` contiene `usuarioId`. El componente `ListaOwnership` obtiene la identidad desde Spring Security y valida la propiedad de cada recurso; los Services invocan esa regla porque conocen las operaciones de negocio y las listas implicadas. Los Controllers solo delegan. `moverFavoritos` es transaccional para mantener juntas la reasignación y la eliminación del origen.

V1–V4 se mantienen sin cambios. V5 crea usuarios. V6 agrega `listas.usuario_id` como FK obligatoria. Las listas previas se asignan a `legacy-migration@invalid.local`, un usuario técnico sin credenciales utilizables. Así se preservan listas y favoritos existentes sin concedérselos a la primera persona que se registre. Este usuario tampoco tiene contraseña inicial divulgada ni endpoint de autenticación alternativo. Las cuentas normales creadas posteriormente solo reciben ownership de sus propias listas.

## CI y despliegue

`.github/workflows/ci.yml` instala Java 25 y ejecuta `./mvnw verify`; el runner de GitHub Actions aporta Docker para Testcontainers. En GitHub, se puede abrir la pestaña **Actions** y seleccionar el workflow **CI**: el tilde verde indica que `verify` terminó bien; una cruz roja permite abrir el job y revisar la salida del paso `Verify (unit + integration)`. La última comprobación local ejecutada en esta rama fue `./mvnw clean verify`: pasó con 3 pruebas unitarias y 4 de integración. Todavía no existe una corrida remota ni un link de Actions porque esta rama se mantiene local.

### Pasos de despliegue pendientes

El despliegue público no se ejecutó para respetar la instrucción de no subir ni publicar esta solución. Cuando se decida publicarla, el procedimiento previsto para un proveedor que admita Java 25 y PostgreSQL administrado es:

1. Crear una base PostgreSQL y guardar su URL JDBC, usuario y contraseña como variables protegidas del servicio.
2. Publicar esta rama en el repositorio y crear un servicio Java conectado a ella. Usar `./mvnw clean package` como comando de build y `java -jar target/demo-0.0.1-SNAPSHOT.jar` como comando de inicio.
3. Configurar `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y un `JWT_SECRET` aleatorio de al menos 32 bytes; `JWT_DURATION_MS` es opcional. Al iniciar, Flyway debe aplicar V1–V6 a la base remota.
4. Revisar los logs de inicio y verificar `GET /health` en la URL pública. Luego comprobar que un endpoint protegido devuelve 401 sin token y 200 con un JWT válido.

Estos pasos son instrucciones pendientes, no evidencia de un despliegue realizado. La URL pública, la corrida remota de CI y sus capturas quedan pendientes hasta autorizar la publicación.
