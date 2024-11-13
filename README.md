# Spring Boot RESTful API for Statistics Update and Caching

This project is a Spring Boot RESTful API designed to update statistics in a MongoDB database and cache responses at specified intervals. The application integrates with Redis for caching and uses JWT for user authorization. For storing users, PostgreSQL is used.


## Features

1. **User Registration**: Allows new users to register.
2. **User Authentication**: Users can authenticate using email and password.
3. **Statistics Retrieval**: Retrieve statistics by a specified date or date range, ASIN (or list of ASINs), or total statistics across all dates and ASINs. All responses are cached.
4. **Statistics Update**: Update statistics at regular intervals using data from the `test_report.json` file. If any changes occur in the file, the corresponding data in the database is updated.

> Note: All endpoints, except for registration and authentication, are accessible only to authenticated users.

## Setup and Configuration

### Prerequisites

Ensure the following software is installed:

- Docker
- Docker Compose

### Project Structure

- **MongoDB**: The database used for storing statistics.
- **PostgreSQL**: The database used for storing users.
- **Redis**: Used for caching responses.
- **Spring Boot Application**: Handles the API logic for user management and statistics retrieval.
- **test_report.json**: A static file used to initialize and update the database with sample data.

### Running the Application with Docker Compose

This project is configured to run entirely through Docker Compose, which sets up the necessary containers for MongoDB, PostgreSQL, Redis, and the Spring Boot application.

1. Clone this repository to your local machine.

2. Build the Docker images and start the containers:

    ```bash
    docker-compose up --build
    ```

   This command will:
   - Build and start the Spring Boot application.
   - Set up MongoDB, PostgreSQL, and Redis containers.
   - Automatically load the `test_report.json` file into the MongoDB database.
   - Run Liquibase to initialize the PostgreSQL database with the required schema and roles.


3. Access the application at [http://localhost:8080](http://localhost:8080).

### Swagger UI

You can view all available API endpoints and interact with them using Swagger UI at the following address:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### MongoDB Data Viewer

To view all available data stored in the MongoDB database, you can access the following URL:

[http://localhost:8081/](http://localhost:8081/)

### Endpoints

#### 1. **POST /api/open/auth/register**
- **Description**: Register a new user with email, first name, last name, and password.
- **Request Body**:
    ```json
    {
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "password": "password123"
    }
    ```

#### 2. **POST /api/open/auth/login**
- **Description**: Authenticate the user and receive a JWT token.
- **Request Body**:
    ```json
    {
        "email": "john.doe@example.com",
        "password": "password123"
    }
    ```

- **Response**:
    ```json
    {
        "token": "JWT_TOKEN"
    }
    ```

#### 3. **GET /api/closed/reports/date**
- **Description**: Get statistics by a specified date or date range (cached).
- **Query Parameters**:
   - `startDate`
   - `endDate` (optional)

#### 4. **GET /api/closed/reports/asin**
- **Description**: Get statistics by a specified ASIN or list of ASINs (cached).
- **Query Parameters**:
   - `asin` (single or comma-separated list)

#### 5. **GET /api/closed/reports/date/all**
- **Description**: Get total statistics across all dates (cached).

#### 6. **GET /api/closed/reports/asin/all**
- **Description**: Get total statistics across all ASINs (cached).

#### 7. **POST /api/closed/reports/update-db**
- **Description**: Manually trigger the statistics update from the `test_report.json` file (for debugging or testing).

#### 8. **Scheduled Updates**
- **Description**: The statistics are updated automatically from the `test_report.json` file every 5 minutes (configurable).

### Caching

The application caches responses for statistics retrieval endpoints to improve performance. Caching is managed via Redis, and all cached responses are stored with a default expiration time (configurable).

### Database Initialization

On startup, the application loads the initial data into MongoDB from the `test_report.json` file, which can be found in the `app/db` directory. This data is used to populate the statistics in the database.

Additionally, the PostgreSQL database is initialized using Liquibase migrations, which create the necessary tables and roles for the user management system.

### Security

JWT tokens are used for securing endpoints. After registering and logging in, users receive a token which must be included in the `Authorization` header for all requests that require authentication.

Example request:
```bash
GET /statistics/date?startDate=2024-01-01&endDate=2024-12-31
Authorization: Bearer JWT_TOKEN