# TP2 · Persistencia, migraciones y arquitectura hexagonal

Evolución del proyecto del TP1. El catálogo de productos sigue siendo de solo lectura y consume DummyJSON. Los favoritos ahora se guardan en PostgreSQL mediante Spring Data JPA; las listas permiten organizarlos y cada favorito pertenece a una lista.

La persistencia usa puertos y adapters para separar el dominio de la infraestructura. Flyway versiona el esquema y Hibernate valida que coincida con las entidades.

## Cómo levantar el proyecto

Requiere Java 25. Usá el Maven Wrapper incluido en el repositorio.

### PostgreSQL con Docker

Desde la raíz del proyecto:

~~~powershell
docker compose up -d
docker compose ps
~~~

El servicio PostgreSQL usa la imagen oficial y expone el puerto 5432. La base, el usuario y la contraseña son `webii_tp2`; están definidos en `docker-compose.yml`. La aplicación usa esos mismos datos en `src/main/resources/application.properties`.

### PostgreSQL local

Si no usás Docker, creá el rol y la base en PostgreSQL:

~~~sql
CREATE ROLE webii_tp2 WITH LOGIN PASSWORD 'webii_tp2';
CREATE DATABASE webii_tp2 OWNER webii_tp2;
~~~

Si usás otras credenciales o puerto, actualizá las propiedades `spring.datasource.*` en `src/main/resources/application.properties`.

### Iniciar la aplicación

~~~powershell
.\mvnw.cmd spring-boot:run
~~~

En macOS o Linux:

~~~sh
./mvnw spring-boot:run
~~~

Al iniciar, Flyway aplica las migraciones pendientes de `src/main/resources/db/migration/`. Hibernate valida el esquema con `spring.jpa.hibernate.ddl-auto=validate`; no lo crea ni lo modifica.

Para revisar las migraciones aplicadas:

~~~powershell
docker compose exec postgres psql -U webii_tp2 -d webii_tp2 -c "SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank;"
~~~

Swagger UI está disponible en [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html); OpenAPI JSON en `/v3/api-docs`.

## Endpoints

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/health` | Chequeo de salud |
| GET | `/ping` | Devuelve `pong` |
| GET | `/api/productos?limit=&skip=` | Catálogo de solo lectura desde DummyJSON |
| GET | `/api/productos/{id}` | Obtener un producto |
| GET / POST | `/api/favoritos` | Listar o crear favoritos |
| GET / PUT / DELETE | `/api/favoritos/{id}` | Obtener, actualizar o eliminar un favorito |
| POST | `/api/listas` | Crear una lista; responde 201 |
| GET | `/api/listas` | Listar listas |
| GET | `/api/listas/{id}` | Obtener una lista |
| GET | `/api/listas/{id}/favoritos` | Listar los favoritos de una lista |
| DELETE | `/api/listas/{id}` | Eliminar una lista vacía; responde 204, o 409 si tiene favoritos |
| POST | `/api/listas/{origenId}/mover-favoritos` | Mover los favoritos al destino y eliminar el origen; responde 204 |

Los endpoints responden 404 cuando el recurso solicitado no existe. Crear o actualizar un favorito requiere `productoId`, `listaId` y `nota`. Crear una lista requiere `nombre`.

## Persistencia y arquitectura: cambios respecto del TP1

En el TP1, `InMemoryFavoritoRepository` implementaba el puerto `FavoritoRepository` y guardaba los favoritos en memoria. En este TP se eliminó esa implementación y se agregaron:

- `FavoritoEntity`: entidad JPA que representa la tabla `favoritos`.
- `FavoritoJpaRepository`: interfaz Spring Data JPA.
- `FavoritoRepositoryAdapter`: convierte entre la entidad JPA y el dominio, y delega las operaciones al repositorio JPA.

`FavoritoRepository` es el puerto: define el contrato que necesita el Service. El adapter es la implementación concreta de ese contrato. Durante el reemplazo de memoria por JPA, el Service y el Controller pudieron seguir usando el mismo puerto sin conocer el cambio de almacenamiento.

Al agregar Listas se amplió la funcionalidad: el dominio `Favorito` y los DTOs `FavoritoRequest` y `FavoritoResponse` ahora incluyen `listaId`; el Service valida la lista y el puerto de Favoritos ofrece una consulta por lista. El Controller de Favoritos conserva sus rutas. La entidad `FavoritoEntity` tiene una relación `@ManyToOne` con `ListaEntity` mediante `lista_id`. No hay una colección `@OneToMany` inversa; los favoritos se consultan por `listaId`.

Listas tiene su propio dominio, puerto, entidad, repositorio JPA, adapter, Service y Controller. Esto permite mantener las reglas de negocio en los Services y el acceso concreto a PostgreSQL detrás de los puertos.

## Migraciones y evolución del esquema

Las migraciones están en `src/main/resources/db/migration/`:

- `V1__create_favorito.sql`: crea `favoritos`. El nombre quedó en singular y Flyway ya la aplicó; no debe renombrarse ni editarse en una base que ya la registró.
- `V2__create_listas.sql`: crea `listas`.
- `V3__add_lista_id_a_favoritos.sql`: agrega la referencia nullable desde favoritos a listas.
- `V4__lista_id_obligatorio.sql`: crea la lista `Sin clasificar` si falta, asigna allí los favoritos que tenían `lista_id` nulo y recién entonces hace obligatoria la columna.

Las migraciones aplicadas se mantienen inmutables. Flyway registra su versión y checksum; cambiar un archivo ya aplicado hace que el historial deje de coincidir con su contenido. Para evolucionar el esquema se agrega una migración con una versión nueva. V4 conserva los datos existentes mediante el backfill antes de aplicar la restricción `NOT NULL`.

## Operación transaccional y atomicidad

`ListaServiceImpl.moverFavoritos` está anotado con `@Transactional`. La operación valida que existan las listas de origen y destino, reasigna los favoritos y elimina la lista de origen dentro de una única transacción.

Esto aplica la atomicidad de ACID: todos los cambios se confirman juntos o se revierten juntos. Sin `@Transactional`, cada guardado podría confirmarse por separado; si una escritura posterior o el borrado de la lista fallara, algunos favoritos podrían quedar en destino mientras otros siguieran en origen y la lista fuente permaneciera.
