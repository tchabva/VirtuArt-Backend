# VirtuArt-Backend

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Backend for the VirtuArt Exhibit Curator. This project provides a RESTful API for managing virtual art exhibitions, users, and artwork data from multiple museum APIs.

## Features
- User and exhibition management
- Integration with external museum APIs
- OAuth2 authentication (Google)
- H2 (in-memory) and PostgreSQL database support

## Technologies Used
- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring Security & OAuth2
- Lombok
- H2 Database (development)
- PostgreSQL (production)
- Maven
- JUnit & Mockito (testing)

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
```

## Contributing
Contributions are welcome! If you'd like to contribute, please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
