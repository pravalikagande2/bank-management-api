// File: src/test/java/com/bank/controller/AccountControllerIT.java

package com.bank.controller;

import com.bank.dto.AccountCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the AccountController.
 * @SpringBootTest loads the full application context.
 * @Testcontainers enables JUnit 5 support for Testcontainers.
 * @AutoConfigureMockMvc provides a MockMvc instance to make requests to our API.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class AccountControllerIT {

    // This will create a new MySQL Docker container for each test run
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");

    // This dynamically sets the database properties for our Spring application
    // to connect to the Testcontainer instead of the local database.
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    // Injected by Spring to perform HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // A utility to convert Java objects to JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountAndRetrieveIt() throws Exception {
        // --- Part 1: Create a new account ---

        // Arrange: Create a request object
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerName("John Doe");
        createRequest.setAccountType("Integration Test");
        createRequest.setInitialDeposit(new BigDecimal("2500.00"));

        // Act & Assert: Perform the POST request
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()) // Expect HTTP 201
                .andExpect(jsonPath("$.accountId").value(1)) // Expect the first account to have ID 1
                .andExpect(jsonPath("$.customerName").value("John Doe"));


        // --- Part 2: Retrieve the newly created account ---

        // Act & Assert: Perform the GET request to the new account's URL
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(2500.00));
    }
}
