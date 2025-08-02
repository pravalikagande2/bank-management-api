Bank Management System API with Fraud Detection & CI/CD
This project is a full-featured, enterprise-grade Bank Management System built with Java and Spring Boot. It provides a complete RESTful API for standard banking operations and includes a sophisticated fraud detection module. The entire project is containerized with Docker and features a complete CI/CD pipeline managed by Jenkins.

Features
Core Banking API (/api/accounts)
POST /: Create a new customer account.

GET /{id}: Retrieve account details and balance.

POST /{id}/deposit: Deposit funds into an account.

POST /{id}/withdraw: Withdraw funds from an account.

POST /transfer: Transfer funds between two accounts.

GET /{id}/transactions: View a complete transaction history for an account.

Fraud Detection Module
The system automatically checks every transaction for the following suspicious patterns:

High Transaction Frequency: Flags accounts with an abnormally high number of transactions within a short, 5-minute sliding window.

Anomalous Transaction Amount: Flags transactions that are significantly larger (5x) than the user's historical average transaction amount.

Technology Stack & Architecture
This project is built using a modern, industry-standard technology stack.

Backend: Java 11, Spring Boot

API: Spring Web (RESTful API)

Database: Spring Data JPA, Hibernate, MySQL

Testing:

Unit Tests: JUnit 5, Mockito

Integration Tests: Spring Boot Test, Testcontainers (with MySQL)

Containerization: Docker

CI/CD: Jenkins, Git, GitHub

CI/CD Pipeline (Jenkins)
The project includes a complete Continuous Integration and Continuous Deployment (CI/CD) pipeline configured in a Jenkinsfile. The pipeline automates the following stages on every git push to the main branch:

Checkout: Securely checks out the latest source code from GitHub using SSH keys.

Build: Compiles the Java code and packages the application into a .jar file using Maven.

Test: Runs all unit and integration tests to ensure code quality and prevent regressions.

Deploy:

Builds a new Docker image from the Dockerfile.

Stops and removes the old running application container.

Runs the new Docker image as a container, deploying the latest version of the application.

How to Run Locally
Using Docker (Recommended)
Ensure Docker Desktop is running.

Build the image: docker build -t bank-management-api .

Run the container (ensure your local MySQL is running):

docker run -d --name bank-api-container -p 8088:8088 \
-e "SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/bank_system" \
-e "SPRING_DATASOURCE_USERNAME=root" \
-e "SPRING_DATASOURCE_PASSWORD=your_password" \
bank-management-api

Using Maven
Ensure a local MySQL server is running.

Update the database credentials in src/main/resources/application.properties.

Run the application: mvn spring-boot:run
