# Screenmatch - Biblioteca de Series

Bienvenido a Screenmatch, una aplicación Java + Spring Boot que funciona como biblioteca de información de series de TV. Permite buscar, importar y consultar series, temporadas y episodios usando datos de la API [OMDb](https://www.omdbapi.com/) y almacenarlos en una base de datos PostgreSQL.

## Funcionalidades

- **Búsqueda de series**: Busca series por nombre o categoría usando endpoints REST.
- **Importación automatizada**: Importa una serie (y todas sus temporadas/episodios) a la base de datos desde la API externa OMDb, por nombre o por `imdbID`.
- **Exploración y consulta**: Consulta todas las series, los detalles de cada una, y sus episodios asociados.

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL 13 o superior
- Spring Boot 3.x
- Acceso a [OMDb API](https://www.omdbapi.com/)
- PGAdmin4 (opcional)

## Instalación y ejecución

### 1. Clona este repositorio

git clone https://github.com/C0kke/Screenmatch
cd screenmatch

### 2. Configura la base de datos PostgreSQL

- Crea una base de datos llamada screenmatch.
- Crea un usuario, host y contraseña.

### 3. Configura las variables de entorno en Windows

Debes definir las siguientes variables de entorno en tu sistema:

| Variable     | Ejemplo de valor           |
|--------------|---------------------------|
| `DB_HOST`    | `host:5432`          |
| `DB_NAME`    | `screenmatch`             |
| `DB_USER`    | `usuario`                |
| `DB_PASSWORD`| `contraseña`  |

### 4. Edita application.properties

Asegúrate de tener este bloque (ya está listo para variables de entorno):

spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
server.port=8080

### 5. Ejecuta la aplicación

Desde la raíz del proyecto:

mvn spring-boot:run

O compílala y ejecútala como un .jar:

mvn clean package
java -jar target/screenmatch-0.0.1-SNAPSHOT.jar

## 📚 Endpoints principales

- GET /series – Listar todas las series en la base
- GET /series/{id} – Obtener detalles de una serie
- GET /consola/buscar-series?nombre={nombre} – Buscar series por nombre en OMDb (devuelve lista de coincidencias)
- POST /consola/importar-serie?nombre={nombre} – Importar una serie por nombre
- POST /consola/importar-serie-id?imdbID={imdbID} – Importar una serie por imdbID

Puedes probar los endpoints con Postman, Insomnia, o desde el navegador (para los GET).

---

## 🛠️ Estructura del proyecto

'''
client/
    index.html <-- Aquí se pueden visualizar los datos
src/
  main/
    java/
      com.aluracursos.screenmatch/
        controller/
        service/
        model/
        repository/
    resources/
      application.properties
    ScreenMatchApplication.java <-- Archivo main del proyecto
    ScreenMatchApplicationConsola.java <-- Consola para importar y visualizar datos
'''


> Proyecto de práctica para certificación de Alura y Oracle One.  
> Uso personal y educativo.