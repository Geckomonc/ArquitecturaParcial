# Feature 4: Comunicaciones y Notificaciones – InnoSistemas

Este proyecto forma parte del sistema InnoSistemas y permite gestionar comunicaciones internas, eventos y notificaciones mediante una API GraphQL desarrollada con Spring Boot. Incluye autenticación JWT, monitoreo con Prometheus y Grafana, y se encuentra completamente contenerizado con Docker.

---

## Objetivos

### Objetivo General
Construir un backend modular que exponga operaciones de consulta y mutación mediante GraphQL, orientado a la gestión de empleados y proyectos.

### Objetivos Específicos

- Definir un esquema GraphQL modular y documentado.
- Implementar resolvers para queries y mutations en Java.
- Integrar persistencia usando Spring Data JPA con PostgreSQL.
- Exponer la interfaz interactiva mediante GraphiQL o Altair.
- Garantizar la interoperabilidad mediante configuración CORS.
- Preparar el backend para contenerización futura con Docker.

---

## Tecnologías y herramientas utilizadas

| Herramienta        | Descripción                                                  |
|--------------------|--------------------------------------------------------------|
| **Spring Boot**    | Framework principal del backend con autenticación y lógica   |
| **GraphQL Java**   | Definición de esquema, queries y mutations                   |
| **PostgreSQL**     | Base de datos relacional                                     |
| **JWT**            | Autenticación y autorización segura                          |
| **Docker**         | Contenerización de todos los servicios                       |
| **Prometheus**     | Recolección de métricas desde el backend                     |
| **Grafana**        | Visualización de métricas desde Prometheus                   |
| **Docker Compose** | Orquestación de contenedores                                 |

---

## Arquitectura del sistema

- **Backend (Spring Boot)**  
  - API GraphQL protegida por JWT.
  - Servicios para proyectos y empleados.
  - Métricas expuestas vía `/actuator/prometheus`.

- **Base de datos PostgreSQL**  
  - Almacena entidades como `Proyecto`, `Empleado`, `Rol`, etc.

- **Monitoreo**
  - Prometheus recolecta métricas del backend.
  - Grafana visualiza estas métricas con dashboards personalizados.

---

## Estructura del Backend

src/
├── controller/
├── dto/
├── entity/
├── repository/
├── resolver/
├── security/
├── service/
└── resources/
├── application.properties
└── graphql/
└── schema.graphqls

## Esquema GraphQL (Documentado)


# Representa un proyecto dentro de la organización
type Proyecto {
    id: ID!
    nombre: String!           # Nombre del proyecto
    descripcion: String       # Descripción opcional
    fechaInicio: String!      # Fecha de inicio (YYYY-MM-DD)
    fechaFin: String!         # Fecha de finalización (YYYY-MM-DD)
    estado: String!           # Estado del proyecto (activo, finalizado, etc.)
    empleadosAsignados: [ProyectoEmpleado!]!  # Empleados asignados al proyecto
}

# Relación entre un empleado y su rol dentro de un proyecto
type ProyectoEmpleado {
    empleado: Empleado!       # Información del empleado
    rol: String!              # Rol del empleado en el proyecto
}

# Información básica de un empleado
type Empleado {
    id: ID!
    nombre: String!
    apellido: String!
    cedula: String!           # Identificación única del empleado
}

# Entrada para registrar un nuevo empleado dentro de un proyecto
input EmpleadoInput {
    nombre: String!
    apellido: String!
    cedula: String!
    rol: String!              # Rol asignado al empleado
}

# Mutaciones para crear un nuevo proyecto con sus empleados
type Mutation {
    addProyecto(
        nombre: String!,
        descripcion: String!,
        fechaInicio: String!,
        fechaFin: String!,
        estado: String!,
        empleados: [EmpleadoInput!]!
    ): Proyecto
}

# Consulta para obtener proyectos por cédula de empleado
type Query {
    proyectosPorCedulaEmpleado(cedula: String!): [Proyecto]
}

## Dockerización

FROM maven:3.8.1-openjdk-17 AS build

COPY pom.xml /app/
COPY src /app/src/

WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

COPY --from=build /app/target/labegr-0.0.1-SNAPSHOT.jar /usr/local/lib/labegr.jar

EXPOSE 8088

ENTRYPOINT ["java","-jar", "/usr/local/lib/labegr.jar"]

## Docker-compose
version: '3.8'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: noraver
      POSTGRES_DB: parcial_db
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  labegr:
    build: .
    ports:
      - "8088:8088"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/parcial_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: noraver
      SPRING_DATASOURCE_DRIVER_CLASS_NAME: org.postgresql.Driver
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_JPA_SHOW_SQL: "true"
      SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL: "true"
      SPRING_JACKSON_TIME_ZONE: America/Bogota
      SPRING_JACKSON_LOCALE: es_CO

  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"
    depends_on:
      - prometheus

volumes:
  postgres-data:


## Prometheus.yml

global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-actuator'
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ['labegr:8088']



