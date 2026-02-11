# Supervision – Todo backend (Spring Boot)

Simple Spring Boot REST API demo to manage todos.

The application uses a PostgreSQL database running in a Docker container. Data is managed via an ORM (Spring Data JPA / Hibernate).

---

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

---

## Run PostgreSQL, Prometheus and Grafana (Docker)

From the backend project root (`supervision/backend/`):

```bash
docker compose up -d
```

This will start:

* **PostgreSQL** (database)
* **Prometheus** (metrics collection)
* **Grafana** (metrics visualization)

PostgreSQL is exposed on port `5432` and the database is initialized automatically.

---

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

---

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

---

## Verify data in PostgreSQL

You can directly inspect the todos stored in PostgreSQL using the following command:

```bash
docker exec -it supervision-postgres psql -U supervision -d supervision
```

Then run:

```sql
SELECT * FROM todos;
```

To reset the table, run:
```sql
TRUNCATE TABLE todos;
```

---

## Monitoring – Prometheus & Grafana

The application exposes metrics via Spring Boot Actuator at:

```
http://localhost:8080/actuator/prometheus
```

### Prometheus

Prometheus UI is available at:

```
http://localhost:9090
```

To verify that the backend is correctly scraped:

* Open `Status` → `Targets`
* The `supervision-backend` target should be **UP**

Prometheus configuration is defined in `prometheus.yml`.

### Grafana

Grafana UI is available at:

```
http://localhost:3000
```

Default credentials:

```
username: admin
password: admin
```

### Configure Grafana (first launch)

1. Log in to Grafana
2. Add Prometheus as a data source
    * URL: `http://prometheus:9090`
3. Import the Dashboard.
```json
{
  "title": "Supervision - Dashboard",
  "timezone": "browser",
  "schemaVersion": 38,
  "version": 1,
  "refresh": "5s",
  "time": { "from": "now-15m", "to": "now" },
  "panels": [
    {
      "id": 1,
      "type": "timeseries",
      "title": "Requests per second",
      "gridPos": { "h": 8, "w": 12, "x": 0, "y": 0 },
      "targets": [
        {
          "refId": "A",
          "expr": "rate(http_server_requests_seconds_count[1m])"
        }
      ]
    },
    {
      "id": 2,
      "type": "timeseries",
      "title": "Average response time (ms)",
      "gridPos": { "h": 8, "w": 12, "x": 12, "y": 0 },
      "targets": [
        {
          "refId": "A",
          "expr": "1000 * (rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m]))"
        }
      ]
    },
    {
      "id": 3,
      "type": "timeseries",
      "title": "CPU usage",
      "gridPos": { "h": 8, "w": 12, "x": 0, "y": 8 },
      "targets": [
        {
          "refId": "A",
          "expr": "process_cpu_usage"
        }
      ]
    },
    {
      "id": 4,
      "type": "timeseries",
      "title": "Heap memory used (MB)",
      "gridPos": { "h": 8, "w": 12, "x": 12, "y": 8 },
      "targets": [
        {
          "refId": "A",
          "expr": "jvm_memory_used_bytes{area=\"heap\"} / 1024 / 1024"
        }
      ]
    }
  ]
}
```

#### What the Dashboard Shows

The dashboard contains 4 key metrics:

* **Requests per second (RPS)** – How many HTTP requests the backend handles per second.
* **Average response time (ms)** – How long the API takes to respond.
* **CPU usage** – How much CPU the JVM process is using (0 to 1 = 0% to 100%).
* **Heap memory (MB)** – Java heap memory currently used.


### Run the JMeter load test

```bash
jmeter -n -t test-plan.jmx -l results.jtl
```