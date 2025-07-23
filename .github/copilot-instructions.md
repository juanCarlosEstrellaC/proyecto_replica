
# Nota importante
Este proyecto es un ejemplo didáctico de arquitectura de microservicios, pero utiliza una sola base de datos compartida para todos los microservicios.

# Copilot Instructions for proyecto_replica

## Arquitectura general
- Este proyecto está compuesto por varios microservicios independientes: `app-libro`, `app-autores` y `app-clientes`.
- Cada microservicio tiene su propio módulo, código fuente y configuración de base de datos.
- La comunicación entre microservicios se realiza vía REST, usando clientes declarativos (por ejemplo, `AutorRestClient` en `app-libro`).
- El descubrimiento de servicios y balanceo se gestiona con Consul y Stork, y el enrutamiento externo con Traefik.

## Flujos y convenciones clave
- **Persistencia de relaciones**: Las relaciones entre entidades de diferentes microservicios (por ejemplo, Libros y Autores) se representan mediante IDs, no objetos anidados ni relaciones JPA directas.
- **DTOs y entidades**: Los endpoints REST exponen y consumen DTOs o entidades simples. Los detalles enriquecidos (como nombres de autores) se obtienen consultando el microservicio correspondiente.
- **Endpoints REST**: Cada microservicio expone endpoints REST bajo `/libros`, `/autores`, etc. Los métodos CRUD siguen convenciones estándar.
- **Configuración**: Cada microservicio tiene su propio `application.properties` para base de datos, puertos, Consul, Stork, etc.
- **Registro en Consul**: El ciclo de vida de cada microservicio incluye registro y eliminación en Consul para descubrimiento y healthchecks.

## Ejemplo de flujo de datos
- Para crear un libro con autores:
  1. El microservicio de libros recibe un objeto con los datos del libro y una lista de IDs de autores.
  2. Solo almacena los IDs de autores.
  3. Al consultar un libro, usa esos IDs para obtener los detalles de los autores vía REST.

## Build, test y ejecución
- Usar los scripts Gradle (`gradlew` o `gradlew.bat`) para compilar y ejecutar cada microservicio.
- Los puertos de desarrollo están definidos en cada `application.properties` (por ejemplo, 9090 para libros, 8080 para autores).
- Para pruebas manuales, usar los archivos `.rest` en `src/test-vscode/` o herramientas como Postman.

## Integraciones y dependencias
- Bases de datos PostgreSQL para persistencia.
- Consul para descubrimiento de servicios.
- Stork para balanceo de clientes REST.
- Traefik para enrutamiento externo.
- Micrometer y Prometheus para métricas.

## Archivos y directorios clave
- `app-libro/src/main/java/com/programacion/distribuida/libros/rest/LibroRest.java`: Ejemplo de endpoint REST y patrón de integración con otros servicios.
- `app-libro/src/main/java/com/programacion/distribuida/libros/clients/AutorRestClient.java`: Cliente REST para comunicación entre microservicios.
- `app-libro/src/main/resources/application.properties`: Configuración de microservicio y Stork.
- `depliegue/mi-aplicacion/docker-compose.yml`: Ejemplo de despliegue multi-servicio.

## Notas adicionales
- No crear relaciones JPA directas entre entidades de diferentes microservicios.
- Mantener la lógica de integración en los endpoints REST, no en las entidades.
- Seguir el patrón de "enriquecimiento" de DTOs al consultar datos, no al persistir.
