### Delaware Java Desktop - Group project

This project is a Java desktop application build with JavaFX for managing business processes such as clients, suppliers, products, and company details. Our focus while developing this project was to adhere to best practices in Java development, with emphasis on modern design patterns, modular architecture, maintainability and extensibility.

#### Functionalities

- **User Authentication:** Secure login for user access control.
- **Client Management:** View and edit detailed client information.
- **Supplier Management:** Manage suppliers and their products.
- **Product Management:** List, add, and manage products associated with suppliers.
- **Company Detail Screens:** Access and modify business entity information.
- **Sidebar Navigation:** Easy navigation between different functional areas.

#### Technologies Used

- **Java & JavaFX:** Core logic and modern desktop UI.
- **CSS:** Customizes the appearance of JavaFX components.
- **Jakarta Persistence API (JPA):** For object-relational mapping and database persistence.
- **Serializable Interface:** For domain model serialization.

#### Design Patterns & Architectural Principles

- **Singleton Pattern:** Ensures only one instance of critical classes (such as the user session) exists throughout the application, supporting consistent user state management.
- **Builder Pattern:** Used for constructing complex domain entities, often coupled with integrated validation logic to ensure entity consistency and correctness at creation time.
- **Programming to Interfaces:** The codebase is structured to depend on abstractions rather than concrete implementations, allowing for flexible and easily testable code. Interfaces define contracts for services, DAOs, and other components.
- **Repository Pattern:** Manages data access and persistence, especially in the `repository` package, often in tandem with JPA.
- **Data Access Object (DAO) Pattern:** Abstracts and encapsulates all access to the data source, providing a clear separation between business logic and data persistence, and making it easier to switch data sources or implement caching.
- **DTO (Data Transfer Object):** The `dto` package is used for transferring data between layers, reducing coupling and minimizing the exposure of domain models.

#### Project Structure

- `main`: Application entry point and core logic.
- `gui`: All GUI-related files and controllers for screens and dialogs.
- `domein`: Domain models, business logic, and entity builders.
- `repository`: Data access layer, implementing DAO and repository patterns (often using JPA).
- `dto`: Data Transfer Objects for safe, decoupled data exchange.
