# Supervision – Todo backend (Spring Boot)

Simple Spring Boot REST API demo to manage todos.

The application uses a PostgreSQL database running in a Docker container. Data is managed via an ORM (Spring Data JPA / Hibernate).

## Prerequisites

* Java 17+
* Maven
* Docker + Docker Compose

Check versions:

```bash
java -version
mvn -v
docker -v
```

## Run PostgreSQL (Docker)

From the backend project root (`supervision/backend/`):

```bash
docker compose up -d
```

PostgreSQL is exposed on port `5432` and the database is initialized automatically.

## Run the application

From the backend project root (`supervision/backend/`):

```bash
mvn spring-boot:run
```

The application starts on:

```
http://localhost:8080
```

Main API endpoint:

```
http://localhost:8080/api/todos
```

## Test the API

A `curl.sh` script is provided.

From the backend project root:

```bash
bash ./curl.sh
```

The script:

* creates todos
* lists todos
* updates a todo
* deletes a todo

## Verify data in PostgreSQL

You can directly inspect the todos stored in PostgreSQL using the following command:

```bash
docker exec -it supervision-postgres psql -U supervision -d supervision
```

Then run:

```sql
SELECT * FROM todos;
```
