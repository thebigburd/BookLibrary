package com.thebigburd.LibraryApplication.Integration.Controller;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;



    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl=baseUrl.concat(":").concat(port+"").concat("/app/user");
    }

    @Test
    @Sql(statements = "INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUser() {
        // Setup
        User expectedUser = new User(1L, "johndoe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        expectedUser.setAge(33);
        String getUserUrl = baseUrl.concat("/1");

        // Act
        User result = restTemplate.getForObject(getUserUrl, User.class);

        // Assert
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getSurname(), result.getSurname());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(33, result.getAge());

    }
    @Test
    @Sql(statements = "INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserlist() {
        // Setup
        User expectedUser = new User(1L, "johndoe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        expectedUser.setAge(33);
        String getUserlistUrl = baseUrl.concat("/userlist");

        // Act
        ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {};
        List<User> response = restTemplate.exchange(getUserlistUrl, HttpMethod.GET, null, responseType).getBody();

        // Assert
        assertEquals(1, response.size());
        assertEquals(1, userRepository.findAll().size());

        User result = response.get(0);

        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getSurname(), result.getSurname());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(33, result.getAge());
    }

    @DirtiesContext
    @Test
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerUser() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        String registerUrl = baseUrl.concat("/registerUser");
        HttpEntity<User> request = new HttpEntity<>(user);


        // Act
        HttpStatus status = restTemplate.postForEntity(registerUrl, request, User.class).getStatusCode();

        // Assert
        assertEquals(HttpStatus.OK, status);
        assertEquals(1, userRepository.findAll().size());

    }

    @Test
    @Sql(statements = "INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteUser() {
        // Setup
        String deleteUrl = baseUrl.concat("/delete/1");
        assertEquals(1, userRepository.findAll().size());


        // Act
        restTemplate.delete(deleteUrl);

        // Assert
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser() {
        // Setup
        String updateUrl = UriComponentsBuilder.fromHttpUrl(baseUrl.concat("/update/{userId}"))
                .queryParam("name", "Jane")
                .queryParam("email","janedoe@example.com")
                .queryParam("surname", "Doe").build().toUriString();

        User user = new User(1L, "janedoe@example.com", "Jane", "Doe", LocalDate.of(1990, 1, 1));
        HttpEntity<User> request = new HttpEntity<>(user);

        // Act
        HttpStatus response = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, User.class,1).getStatusCode();

        // Assert
        assertEquals(HttpStatus.OK, response);

        User result = userRepository.findById(1L).get();

        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSurname(), result.getSurname());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(33, result.getAge());

    }
}