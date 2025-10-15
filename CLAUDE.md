# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java JPA/Hibernate demonstration project (JPA07) that implements a charity donation tracking system. It showcases a three-tier architecture with entities (Donor, Charity, Donation), a generic DAO pattern, and service layer implementations.

## Build and Development Commands

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew run
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests com.company.BasicTests

# Run a specific test method
./gradlew test --tests com.company.BasicTests.testCreateDeleteCharity
```

### Cleaning Build Artifacts
```bash
./gradlew clean
```

## Architecture

### Domain Model
- **AbstractItem**: Base class for all entities, provides `id`, `dateCreated`, and `lastUpdated` fields with automatic timestamp management via JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`)
- **Donor**, **Charity**, **Donation**: Domain entities extending AbstractItem
- **Charity** entity uses a JSON column (`attributes` field) for flexible key-value storage using `@JdbcTypeCode(SqlTypes.JSON)`
- Relationships: Charity has many Donations, Donor has many Donations (one-to-many with cascade operations)

### Data Access Layer (DAO Pattern)
- **BaseTemplateDAO**: Generic interface defining CRUD operations for all entity types
- **BaseTemplateDAOImpl**: Generic implementation using JPA Criteria API for type-safe queries
- Entity-specific DAOs (DonorDAO, CharityDAO, DonationDAO) extend BaseTemplateDAOImpl
- Supports multiple query methods:
  - Direct SQL with named parameters via `getBySQL()` and `updateBySQL()`
  - Criteria-based queries with FilterDescription objects
  - Map-based parameter queries

### Service Layer
- **AbstractService**: Base class managing EntityManager lifecycle and transaction handling via `doWork()` method
- Services (DonorService, CharityService, DonationService) extend AbstractService
- Each service wraps DAO operations in transactions using the `doWork(Consumer<EntityManager>)` pattern
- Services use AtomicReference pattern to capture results from EntityManager transactions
- All services must call `close()` when done to properly close the EntityManagerFactory

### Database Configuration
- **EntityManagerFactoryProvider**: Singleton pattern for EntityManagerFactory creation
- Uses HikariCP connection pool configured via `db.properties` file (not in repo, must be created locally)
- Hibernate configuration programmatically registers entity classes (Donor, Charity, Donation)
- Database settings:
  - Dialect: MariaDBDialect
  - HBM2DDL: update (auto-creates tables)
  - Show SQL: true (for debugging)

### Filter and Query System
- **FilterDescription**: Encapsulates query criteria with field, operator, and value
- **FilterOperator**: Enum supporting eq, like, gt, lt, gte, lte, timestamp_gte, timestamp_lte
- **SortDescription** and **SortDirection**: For ordering query results
- **ValueReference**: Wrapper for complex value references in queries

## Database Setup

Create a `db.properties` file in the project root (this file is git-ignored):
```properties
jdbcUrl=jdbc:mysql://localhost:3306/your_database
user=your_username
password=your_password
```

The application uses MariaDB/MySQL dialect and will auto-create tables on first run (HBM2DDL=update).

## Code Style

- **Indentation**: 2 spaces (enforced by .editorconfig)
- **Charset**: UTF-8
- **Line endings**: LF
- **Lombok**: Heavily used for @Data, @Slf4j, @NoArgsConstructor, @AllArgsConstructor
- Entity classes manually implement `toString()` for debugging output

## Key Development Patterns

### Transaction Management
All database operations must be wrapped in the `doWork()` method which handles:
- EntityManager creation
- Transaction begin/commit/rollback
- Proper resource cleanup
- Exception handling with logging

Example:
```java
public Donor create(Donor donor) {
    final AtomicReference<Donor> created = new AtomicReference<>();
    doWork(em -> created.set(dao.create(donor, em)));
    return created.get();
}
```

### Adding New Entities
When creating new domain entities:
1. Extend AbstractItem for automatic id/timestamp management
2. Annotate with @Entity and @Table
3. Register in EntityManagerFactoryProvider.getEMF() via `configuration.addAnnotatedClass()`
4. Create a DAO extending BaseTemplateDAOImpl
5. Create a Service extending AbstractService

### Query Building
Use FilterDescription objects for dynamic queries rather than writing SQL directly:
```java
List<FilterDescription> filters = new ArrayList<>();
filters.add(new FilterDescription("age", FilterOperator.gte, 35));
List<Donor> results = donorService.getByCriteria(filters, limit, offset);
```

## Dependencies

- Java 17
- Hibernate 6.6.8.Final
- HikariCP 6.2.1 (connection pooling)
- MySQL Connector 9.2.0
- Lombok (via Freefair plugin)
- JUnit Jupiter 5.12.0
- Jackson Databind 2.18.2
- SLF4J 1.7.32 with simple logger
