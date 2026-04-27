# 🔍 Sling Search Challenge

Microservicio de búsqueda de hoteles construido con **arquitectura hexagonal (Ports & Adapters)**, aplicando principios
de **Clean Architecture** y patrones de diseño empresariales.

## 🏗️ Patrones de Diseño Utilizados

| Patrón                                              | Descripción                                                                                                  |
|-----------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| **Ports & Adapters (Hexagonal)**                    | Separación del dominio de la infraestructura mediante puertos (interfaces) y adaptadores (implementaciones). |
| **CQRS (Command Query Responsibility Segregation)** | Separación de escritura (vía Kafka) y lectura (consulta directa a BD).                                       |
| **Builder**                                         | Construcción inmutable de objetos de dominio (`Search.builder()`).                                           |
| **Strategy**                                        | Puertos como `SearchHashServicePort` y `EventPublisherPort` permiten intercambiar implementaciones.          |
| **Value Object**                                    | `SearchId` encapsula la identidad del dominio sin ser una entidad.                                           |
| **Repository**                                      | `SearchCountRepositoryPort` abstrae el acceso a datos.                                                       |
| **Adapter**                                         | Cada módulo de infraestructura adapta una tecnología externa al puerto del dominio.                          |

---

## 🛠️ Tecnologías

| Tecnología                  | Versión         | Uso                                         |
|-----------------------------|-----------------|---------------------------------------------|
| **Java**                    | 25              | Lenguaje principal                          |
| **Spring Boot**             | 4.0.6           | Framework base                              |
| **Gradle**                  | Multi-módulo    | Build system                                |
| **PostgreSQL**              | 17              | Base de datos relacional                    |
| **Apache Kafka**            | Confluent 5.5.3 | Mensajería asíncrona (productor/consumidor) |
| **Hibernate / JPA**         | -               | ORM y persistencia                          |
| **Lombok**                  | 1.18.42         | Reducción de boilerplate                    |
| **MapStruct**               | 1.5.5           | Mapeo de objetos entre capas                |
| **Docker / Docker Compose** | -               | Contenerización y orquestación local        |
| **JUnit 5 + Mockito**       | -               | Testing unitario                            |
| **JaCoCo**                  | 0.8.14          | Cobertura de código                         |

---

## 🧅 Arquitectura Hexagonal

El proyecto está organizado en tres capas principales:

```
┌──────────────────────────────────────────────────┐
│                  INFRASTRUCTURE                  │
│  ┌────────────┐              ┌────────────────┐  │
│  │ Entry Points│             │Driven Adapters │  │
│  │ - REST API │              │ - PostgreSQL   │  │
│  │ - Kafka    │              │ - Kafka Prod.  │  │
│  │   Consumer │              │ - SHA-256 Hash │  │
│  └─────┬──────┘              └───────┬────────┘  │
│        │                             │           │
│        │    ┌───────────────────┐    │           │
│        └───►│      DOMAIN       │◄───┘           │
│             │  ┌─────────────┐  │                │
│             │  │   Model     │  │                │
│             │  │  (Puertos)  │  │                │
│             │  └──────┬──────┘  │                │
│             │  ┌──────┴──────┐  │                │
│             │  │  Use Cases  │  │                │
│             │  └─────────────┘  │                │
│             └───────────────────┘                │
└──────────────────────────────────────────────────┘
```

### Estructura de Módulos

```
ms-sling-search/
├── domain/
│   ├── model/             → Entidades, Value Objects, Puertos (interfaces)
│   └── usecase/           → Casos de uso (lógica de negocio pura)
├── infrastructure/
│   ├── entry-points/
│   │   ├── service-web/   → REST Controller (API HTTP)
│   │   └── kafka-consumer/→ Consumidor de eventos Kafka
│   ├── driven-adapters/
│   │   ├── persistence-db/→ Implementación JPA (PostgreSQL)
│   │   ├── kafka-producer/→ Publicador de eventos Kafka
│   │   └── hash/          → Servicio de hashing SHA-256
│   └── helpers/
│       └── json-helper/   → Utilidad de serialización JSON
├── applications/
│   └── app-service/       → Punto de arranque Spring Boot
└── deployment/            → Docker, Docker Compose, scripts
```

### Flujo de Datos

1. **REST API** recibe una solicitud de búsqueda → la transforma al modelo de dominio.
2. **CreateSearchUseCase** genera un hash SHA-256 de la búsqueda y publica un evento vía Kafka.
3. **KafkaConsumer** recibe el evento y persiste/incrementa el contador en PostgreSQL.
4. **GetSearchCountUseCase** consulta el contador de búsquedas por hash.

---

## 🚀 Instrucciones para Correr en Local

### Prerrequisitos

- **Docker** y **Docker Compose** instalados
- **Java 25** (para compilar)
- **Gradle** (se incluye wrapper `./gradlew`)

### Paso 1 — Compilar el proyecto

```bash
./gradlew clean build
```

### Paso 2 — Levantar con Docker Compose

```bash
cd deployment
docker-compose up --build
```

Esto levantará:

| Servicio            | Puerto | Descripción             |
|---------------------|--------|-------------------------|
| **ms-sling-search** | `8080` | Microservicio principal |
| **PostgreSQL**      | `5432` | Base de datos           |
| **Kafka**           | `9092` | Broker de mensajería    |
| **Zookeeper**       | `2181` | Coordinador de Kafka    |

### Paso 3 — Verificar

```bash
curl http://localhost:8080/actuator/health
```

### Paso 4 — Detener

```bash
docker-compose down -v
```

> **Nota:** Las variables de entorno se configuran automáticamente desde `deployment/.env`. No es necesario modificarlas
> para ejecución local con Docker Compose.

---

## 📐 Diagrama de Arquitectura

El diagrama de componentes se encuentra en [`docs/architecture.puml`](docs/architecture.puml).

---

## 📝 Variables de Entorno

| Variable        | Valor por defecto (Docker)                       | Descripción                  |
|-----------------|--------------------------------------------------|------------------------------|
| `DB_URL`        | `jdbc:postgresql://postgres-sling:5432/sling_db` | URL de conexión a PostgreSQL |
| `DB_USERNAME`   | `reactive`                                       | Usuario de BD                |
| `DB_PASSWORD`   | `admin123`                                       | Contraseña de BD             |
| `KAFKA_SERVERS` | `kafka:9092`                                     | Servidores Kafka             |
