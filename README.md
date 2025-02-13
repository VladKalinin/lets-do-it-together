# lets-do-it-together


## Auth Service

Auth Service is a microservice responsible for authentication and user session management in the **Let's Do It Together** project. It provides endpoints for user registration, login, token management, and user information retrieval.

### Tech Stack

- **Java**
- **Spring Boot** (REST API)
- **Spring Security** (Authentication & Authorization)
- **JWT** (JSON Web Token for authentication)
- **Redis** (Token storage & caching)
- **MongoDB** (NoSQL database for user data)
- **Docker** (Containerization)
- **Docker Compose** (Service orchestration)
- **Swagger** (API Documentation)

## API Endpoints
### Authentication
- **POST** `/register` - Register a new user
- **POST** `/login` - Login
- **POST** `/refresh` - Refresh Token
- **POST** `/logout` - Logout
## API Documentation
Once the service is running, Swagger documentation can be accessed at:
```
http://localhost:8090/swagger-ui.html
```


### User Info
- **GET** `/me` - Get current user details

---

## Running Locally
### Prerequisites
- **Docker & Docker Compose** installed
### Start the Service
```sh
docker-compose up --build
```
### Stop the Service

```sh
docker-compose down
```
