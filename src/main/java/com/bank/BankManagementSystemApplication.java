// File: src/main/java/com/bank/BankManagementSystemApplication.java

package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * The @SpringBootApplication annotation enables auto-configuration, component scanning,
 * and other key Spring Boot features.
 */
@SpringBootApplication
public class BankManagementSystemApplication {

    public static void main(String[] args) {
        // This single line starts the entire embedded web server (like Tomcat)
        // and initializes the Spring application context.
        SpringApplication.run(BankManagementSystemApplication.class, args);
        System.out.println("\nBank Management System API is running!");
        System.out.println("Access API documentation or endpoints at http://localhost:8080");
    }
}
