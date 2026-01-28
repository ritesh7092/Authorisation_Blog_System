# Secure Role-Based Authentication & Authorization System

A production-ready REST API backend built with **Spring Boot 3**, **Spring Security**, **SQLite**, and **JWT**. This system implements secure user registration/login and role-based access control (RBAC) for managing blog posts.

## 🚀 Key Features

*   **Authentication**:
    *   Secure User Signup & Signin.
    *   Stateless JWT (JSON Web Token) authentication.
    *   BCrypt password hashing (strength 10+).
*   **Authorization**:
    *   **RBAC**: Role-Based user access (`USER`, `ADMIN`).
    *   **Method-Level Security**: Protected endpoints using `@PreAuthorize`.
    *   **Ownership Security**: Users can only edit their own blogs; Admins can manage all.
*   **Database**: Zero-conf generic **SQLite** database.
*   **Error Handling**: Global exception handling with clean JSON error responses.

## 🛠️ Tech Stack

*   **Java 17**
*   **Spring Boot 3.2.x** (Web, Security, Data JPA)
*   **SQLite** (via `sqlite-jdbc` & Hibernate Dialect)
*   **JJWT** (for JWT generation/validation)
*   **Maven**

## ⚙️ Configuration

The application is configured in `src/main/resources/application.properties`.

*   **Database**: `jdbc:sqlite:auth_system.db` (Created automatically in project root)
*   **JWT Secret**: Configured via `app.jwtSecret`. **Important**: Change this for production use!
*   **Server Port**: `8080` (Default)

## 📦 Installation & Run

### Prerequisites
*   JDK 17 or higher
*   Maven 3.6+

### Steps
1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd Authorisation_System
    ```

2.  **Build the project**:
    ```bash
    mvn clean package
    ```

3.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

The API will be available at `http://localhost:8080`.

## 🧪 Verification

A PowerShell script is included for automated testing on Windows.

1.  Start the server (`mvn spring-boot:run`).
2.  Open a new terminal and run:
    ```powershell
    .\verify_api.ps1
    ```

This script tests the entire flow: Signup (User/Admin), Login (Token retrieval), Blog CRUD, and Security checks.

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/signup` | Register a new user (`USER` or `ADMIN`) | No |
| `POST` | `/api/auth/signin` | Login and receive JWT token | No |

**Signup Payload:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

### Blog Management

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/blogs` | Get all blogs | No (Public) |
| `POST` | `/api/blogs` | Create a new blog | Yes (`USER`, `ADMIN`) |
| `PUT` | `/api/blogs/{id}` | Update a blog | Yes (Owner or `ADMIN`) |
| `DELETE` | `/api/blogs/{id}` | Delete a blog | Yes (`ADMIN` only) |

**Blog Payload:**
```json
{
  "title": "My First Blog",
  "content": "This is a secure blog post."
}
```

## 🔒 Security Implementation Details

1.  **JwtAuthenticationFilter**: Intercepts requests, extracts JWT from `Authorization: Bearer <token>` header, and sets the Security Context.
2.  **UserDetailsServiceImpl**: Loads user data from SQLite for authentication.
3.  **Encrypted Passwords**: All passwords are hashed using BCrypt before storage.
4.  **CORS**: Configured to allow cross-origin requests (customizable in `AuthController`/`BlogController`).

## 📁 Project Structure

```
src/main/java/com/authsystem
├── controller       # REST Controllers (Auth, Blog)
├── model            # JPA Entities (User, Blog)
├── repository       # Spring Data Repositories
├── security         # JWT Utils, Filter, Config
├── payload          # DTOs (Requests/Responses)
├── exception        # Global Error Handling
└── AuthSystemApplication.java  # Main Entry Point
```
