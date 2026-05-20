# VirtuArt-Backend

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Backend for the VirtuArt Exhibit Curator. This project provides a RESTful API for managing virtual art exhibitions, users, and artwork data from multiple museum APIs.

This project is currently aggregating collections from the following museum APIs:
- [The Art Institute of Chicago](https://www.artic.edu/)
- [The Cleveland Museum of Art](https://www.clevelandart.org/home)

This backend serves the [VirtuArt Frontend](https://github.com/tchabva/virtuart-frontend), an Android application for curating virtual art exhibits.

## Features
- User and exhibition management
- Integration with external museum APIs
- OAuth2 authentication (Google)
- H2 (in-memory) and PostgreSQL database support

## Technology Stack
- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security & OAuth2
- Lombok
- H2 Database (development)
- PostgreSQL (production)
- Maven
- JUnit 5, Mockito, AssertJ (testing)
- Spring Boot Test, Spring Security Test, Reactor Test

## Prerequisites
- Java 21+
- Maven 3.8+
- (For production) PostgreSQL database

## Getting Started

### 1. Clone the repository
```bash
git clone <repo-url>
cd VirtuArt-Backend
```

### 2. Configure Environment Variables

#### Development (H2 In-Memory Database)
Set the following environment variables (can be in your IDE or shell):
- `H2_USERNAME` (e.g., `sa`)
- `H2_PASSWORD` (e.g., `password`)
- `BE_GOOGLE_CLIENT_ID` (Google OAuth2 Client ID for backend verification)

#### Production (PostgreSQL)
Set the following environment variables or update `src/main/resources/application-prod.properties`:
- `DB_URI` (e.g., `jdbc:postgresql://localhost:5432/virtuart`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `BE_GOOGLE_CLIENT_ID` (Google OAuth2 Client ID for backend verification)

### 3. Build the Project
```bash
./mvnw clean install
```

### 4. Run the Application

#### Option 1: Command Line (Maven Wrapper)
- **Development (default profile: H2):**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Production (PostgreSQL):**
  ```bash
  SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
  ```

#### Option 2: Run from IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
1. Import the project as a Maven project.
2. Set the required environment variables in your Run/Debug configuration (see above).
3. Select `VirtuArtBackendApplication` as the main class.
4. Run or Debug the application.
   - For production, set the `SPRING_PROFILES_ACTIVE` environment variable to `prod` in your configuration.

#### Option 3: Build and Run JAR
```bash
./mvnw package
java -jar target/VirtuArt-Backend-0.0.1-SNAPSHOT.jar
```
- For production: `SPRING_PROFILES_ACTIVE=prod java -jar target/VirtuArt-Backend-0.0.1-SNAPSHOT.jar`

## API Endpoints
All endpoints are available under the `/api/v1/` prefix.

| Method   | Endpoint                                        | Description                                     |
|----------|-------------------------------------------------|-------------------------------------------------|
| `GET`    | `/artworks/{source}`                            | Get paginated artworks from a specific source.  |
| `GET`    | `/artworks/{source}/{id}`                       | Get a single artwork by ID.                     |
| `POST`   | `/artworks/search`                              | Perform a basic search for artworks.            |
| `POST`   | `/artworks/search/advanced`                     | Perform an advanced search for artworks.        |
| `GET`    | `/exhibitions`                                  | Get all exhibitions for the authenticated user. |
| `POST`   | `/exhibitions`                                  | Create a new exhibition.                        |
| `GET`    | `/exhibitions/{id}`                             | Get details for a specific exhibition.          |
| `DELETE` | `/exhibitions/{id}`                             | Delete an exhibition.                           |
| `POST`   | `/exhibitions/{id}`                             | Add an artwork to an exhibition.                |
| `DELETE` | `/exhibitions/{id}/{apiId}/{source}`            | Remove an artwork from an exhibition.           |

## Testing

The project includes a unit and integration test suite covering controllers, services, and DTO mapping. Tests use the `dev` profile (H2 in-memory database) and do not require external museum APIs or a running PostgreSQL instance.

### Run All Tests
```bash
./mvnw test
```

Tests are also executed automatically as part of a full build:
```bash
./mvnw clean install
```

### Run a Specific Test Class
```bash
./mvnw test -Dtest=ArtworkServiceImplTest
```

### Run a Single Test Method
```bash
./mvnw test -Dtest=ArtworkServiceImplTest#invalidSourceThrowsException
```

### Run Tests from Your IDE
Import the project as a Maven project, then run individual test classes or methods using your IDE's JUnit 5 runner (IntelliJ IDEA, Eclipse, VS Code, etc.).

### Test Stack
| Library | Purpose |
|---------|---------|
| JUnit 5 | Test framework (`@Test`, `@Nested`, `@DisplayName`) |
| Mockito | Mocking dependencies (`@Mock`, `@InjectMocks`, `@Spy`) |
| AssertJ | Fluent exception and assertion checks |
| Spring Boot Test | Integration testing and application context loading |
| Spring Security Test | Security-related test support |
| Reactor Test | Reactive stream testing support |

Mockito is configured via the Maven Surefire plugin to support Java 21 inline mocking of final classes and methods.

### Test Structure
```
src/test/java/uk/techreturners/VirtuArt
├── VirtuArtBackendApplicationTests.java   // Spring context load test
├── controller
│   └── ArtworkControllerTest.java         // Controller unit tests
├── service
│   ├── ArtworkServiceImplTest.java        // Artwork service (AIC & CMA routing, pagination, lookup)
│   ├── ExhibitionServiceImplTest.java     // Exhibition CRUD, ownership, and artwork management
│   └── ExhibitionItemServiceImplTest.java // Exhibition item create/lookup logic
└── util
    └── DTOMapperTest.java                 // AIC API response → DTO mapping
```

### Test Coverage by Layer

| Layer | Test Class | What Is Covered |
|-------|------------|-----------------|
| Integration | `VirtuArtBackendApplicationTests` | Spring application context loads with the `dev` profile |
| Controller | `ArtworkControllerTest` | Paginated artwork endpoint responses and HTTP status codes |
| Service | `ArtworkServiceImplTest` | Source routing (`aic` / `cma`), DAO delegation, pagination mapping, artwork lookup, and error handling |
| Service | `ExhibitionServiceImplTest` | List/create/update/delete exhibitions, add/remove artworks, user ownership checks, and access control |
| Service | `ExhibitionItemServiceImplTest` | Get-or-create exhibition items, field mapping from artwork DTOs, and repository interactions |
| Util | `DTOMapperTest` | AIC image URL construction, artwork DTO mapping, paginated response mapping, and pagination flags |

### Testing Conventions
- **Unit tests** use `@ExtendWith(MockitoExtension.class)` with mocked collaborators — no database or HTTP calls.
- **Integration test** uses `@SpringBootTest` with `@ActiveProfiles("dev")` to verify the full application wiring against H2.
- Tests are organised with `@Nested` inner classes and `@DisplayName` for readable, grouped output.
- Service tests that implement `DTOMapper` use `@Spy` on the service under test to verify mapper method calls.

## Project Structure
```
src/main/java/uk/techreturners/VirtuArt
├── config          // Spring Security and other configurations
├── controller      // REST API controllers
├── exception       // Custom exception classes
├── model           // JPA entities, DTOs, and API models
├── repository      // Data Access Objects (DAO) and JPA repositories
├── service         // Business logic and services
└── util            // Utility classes

src/test/java/uk/techreturners/VirtuArt
├── controller      // Controller unit tests
├── service         // Service unit tests
└── util            // Utility/mapper unit tests
```

## License
This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.