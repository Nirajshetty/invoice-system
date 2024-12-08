# **Invoice System - Spring Boot Application**
**Overview**

The Invoice System is a simple application built with Spring Boot that allows users to create invoices, make payments on invoices, and process overdue invoices. The system provides RESTful APIs to manage invoice creation, payment, and overdue processing.

## Key Features
- **Invoice Creation**: Create new invoices with specific amounts and due dates.
- **Invoice Payments**: Pay invoices partially or fully, updating the invoice status accordingly.
- **Overdue Processing**: Automatically process overdue invoices, applying late fees and generating new invoices when necessary.
- **Swagger UI**: Easy access to API documentation via Swagger.
- **Docker Support**: The application can be run as a Docker container for easy deployment.

---

## Base URL
The application is available at:

http://localhost:8080

Swagger UI documentation can be accessed at:

http://localhost:8080/swagger-ui/index.html#/

---

## API Endpoints

### 1. Create Invoice
**POST** `/invoices`  
This endpoint allows you to create a new invoice.  

**Request Body**:
```json
{
  "amount": 199.99,
  "due_date": "2021-09-11"
}
```

**Response**:
```json
{
  "id": "1234"
}
```

•	Status Code: 201 Created

### 2. Get All Invoices
**GET** `/invoices`
This endpoint retrieves all invoices in the system.
**Response**:
```json
[
  {
    "id": "1234",
    "amount": 199.99,
    "paid_amount": 0,
    "due_date": "2021-09-11",
    "status": "pending|paid|void"
  }
]
```
•	Status Code: 200 OK
### 3. Pay Invoice
**POST** `/invoices/{invoiceId}/payments`
This endpoint allows you to pay a portion of an invoice.
**Request Body**:
```json
{
  "amount": 159.99
}
```
If the invoice is fully paid, it will be marked as paid.
•	Status Code: 200 OK (Invoice is updated)
### 4. Process Overdue Invoices
**POST** `/invoices/process-overdue`
This endpoint processes all pending invoices that are overdue. It adds a late fee and creates new invoices if necessary.
**Request Body**:
```json
{
  "late_fee": 10.5,
  "overdue_days": 10
}
```
•	Status Code: 200 OK

## Improvements
The following improvements have been made to the application:
-	**Validation**: Input data is validated using Spring's validation mechanisms.
-	**Swagger UI**: Interactive API documentation using Swagger UI, which is accessible at http://localhost:8080/swagger-ui/index.html#/.
-	**Global Exception Handler**: A global exception handler that ensures proper error handling across the application.
-	**OpenAPI Documentation**: OpenAPI annotations have been used to generate detailed API documentation.
-	**Service Interfaces**: Proper separation of concerns with service interfaces for managing invoice operations.
-	**Custom Exception Handling**: A custom exception is used for cases like exceeding the maximum allowed payments for an invoice.
-	**Mapper Classes**: Mapper classes are used to handle the conversion between DTOs and domain objects.
-	**Unit Tests**: Tests are written using Mockito to mock dependencies and test the domain model.

## Getting Started
Follow the steps below to get the application up and running:
### 1. Prerequisites
-	Java 17 or later
-	Maven
-	Docker (optional, for Docker container setup)
### 2. Build the Application
Run the following command to clean and package the application:
mvn clean package
### 3. Run the Application with Docker
To run the application inside a Docker container, execute the following commands:
#### 1.	Build and start the Docker container:
docker-compose up -d --build
#### 2.	Access the application: Once the container is up, the application will be available at:
http://localhost:8080
Swagger UI will be accessible at:
http://localhost:8080/swagger-ui/index.html#/
### 4. Run the Application Locally
Alternatively, you can run the application locally without Docker by using the java -jar command:
#### 1.	Build the application:
mvn clean package
#### 2.	Run the application:
java -jar target/invoice-system-0.0.1-SNAPSHOT.jar
The application will be available at:
http://localhost:8080
### 5. Unit Tests
Unit tests have been written using Mockito to ensure the correctness of the domain logic. To run the tests, use the following command:
mvn test


## Directory Structure
```
src/
├── main/
│   ├── java/com/eg/invoice/
│   │   ├── Controller/   # Handles incoming API requests
│   │   ├── Service/      # Contains the business logic
│   │   ├── Model/        # Represents domain entities like Invoice
│   │   ├── Repository/   # Interfaces for database operations (not implemented)
│   │   ├── Exception/    # Custom exceptions for specific scenarios
│   │   ├── Mapper/       # Classes to map between DTOs and domain models
│   │   ├── Dto/          # Data Transfer Object classes
│   │   ├── Enums/        # Constants
│   │   └── Entity/       # Entity class
│   └── resources/
│       └── application.yml  # Spring Boot application configuration
└── test/
    └── java/com/eg/invoice/ # Unit tests for the domain logic using Mockito
```

