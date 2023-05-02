package com.thebigburd.LibraryApplication.Integration.Controller;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;



    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl=baseUrl.concat(":").concat(port+"").concat("/app/user");
    }

    @Test
    void getUser() {
//        // Setup
//        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
//        @Sql(statements = "INSERT INTO PEOPLE_TBL (id, email, name, surname, dateOfBirth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')
//        String getUserUrl = baseUrl.concat("/1");
//        HttpEntity<Long> request = new HttpEntity<>(1L);
//
//        // Act
//        restTemplate.postForEntity()

    }
    @Test
    void getUserlist() {
    }

    @Test
    void registerUser() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        String registerUrl = baseUrl.concat("/registerUser");
        HttpEntity<User> request = new HttpEntity<>(user);


        // Act
        ResponseEntity<User> response = restTemplate.postForEntity(registerUrl, request, User.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}