package com.thebigburd.LibraryApplication.Integration.Controller;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BorrowControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @BeforeEach
    public void setUp(){
        restTemplate = new RestTemplate();
        baseUrl=baseUrl.concat(":").concat(port+"").concat("/app/");
    }

    @Test
    @Sql(scripts = "classpath:borrowscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM borrow WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserBorrowed() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, true);
        String getBorrowUrl = baseUrl.concat("users/1/borrowlist");

        // Act
        ParameterizedTypeReference<List<BorrowDTO>> responseType = new ParameterizedTypeReference<List<BorrowDTO>>() {};
        ResponseEntity<List<BorrowDTO>> responseEntity = restTemplate.exchange(getBorrowUrl, HttpMethod.GET, null, responseType);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());

        BorrowDTO result = responseEntity.getBody().get(0);
        assertEquals(expectedBook.getName(), result.getBook().getName());
        assertEquals(expectedBook.getDescription(), result.getBook().getDescription());
        assertEquals(expectedBook.getPublishYear(), result.getBook().getPublishYear());
        assertEquals(expectedBook.isBorrowed(), result.getBook().isBorrowed());

        assertEquals(LocalDate.of(2023,01,01), result.getBorrowDate());
        assertEquals(LocalDate.of(2023,01,10), result.getReturnDate());


    }

    @Test
    @Sql(scripts = "classpath:borrowscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM borrow WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookBorrowedHistory() {
        // Setup
        User expectedUser = new User(1L, "johndoe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        String getBorrowUrl = baseUrl.concat("book/history/1");

        // Act
        ParameterizedTypeReference<List<Borrow>> responseType = new ParameterizedTypeReference<List<Borrow>>() {};
        ResponseEntity<List<Borrow>> responseEntity = restTemplate.exchange(getBorrowUrl, HttpMethod.GET, null, responseType);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());

        Borrow result = responseEntity.getBody().get(0);
        assertEquals(expectedUser.getName(), result.getUser().getName());
        assertEquals(expectedUser.getSurname(), result.getUser().getSurname());
        assertEquals(expectedUser.getEmail(), result.getUser().getEmail());
        assertEquals(expectedUser.getDateOfBirth(), result.getUser().getDateOfBirth());
        assertEquals(33, result.getUser().getAge());

        assertEquals(LocalDate.of(2023,01,01), result.getBorrowDate());
        assertEquals(LocalDate.of(2023,01,10), result.getReturnDate());
    }

    @Test
    @Sql(scripts = "classpath:borrowscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM borrow WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBorrowed() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, true);
        User expectedUser = new User(1L, "johndoe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        expectedUser.setAge(33);
        String getAllBorrowUrl = baseUrl.concat("borrow/list");

        // Act
        ParameterizedTypeReference<List<Borrow>> responseType = new ParameterizedTypeReference<List<Borrow>>() {};
        ResponseEntity<List<Borrow>> responseEntity = restTemplate.exchange(getAllBorrowUrl, HttpMethod.GET, null, responseType);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());

        Borrow result = responseEntity.getBody().get(0);

        assertEquals(expectedUser.getName(), result.getUser().getName());
        assertEquals(expectedUser.getSurname(), result.getUser().getSurname());
        assertEquals(expectedUser.getEmail(), result.getUser().getEmail());
        assertEquals(expectedUser.getDateOfBirth(), result.getUser().getDateOfBirth());
        assertEquals(33, result.getUser().getAge());

        assertEquals(expectedBook.getName(), result.getBook().getName());
        assertEquals(expectedBook.getDescription(), result.getBook().getDescription());
        assertEquals(expectedBook.getPublishYear(), result.getBook().getPublishYear());
        assertEquals(expectedBook.isBorrowed(), result.getBook().isBorrowed());

        assertEquals(LocalDate.of(2023,01,01), result.getBorrowDate());
        assertEquals(LocalDate.of(2023,01,10), result.getReturnDate());
    }

    @Test
    @Sql(statements = "INSERT INTO people (id, email, name, surname, date_of_birth) VALUES (1, 'johndoe@example.com', 'John', 'Doe', '1990-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False);", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM borrow WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void borrowBook() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, true);
        User expectedUser = new User(1L, "johndoe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        expectedUser.setAge(33);
        Borrow borrow = new Borrow(1L, expectedBook, expectedUser, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10));
        String borrowUrl = baseUrl.concat("borrow/1");
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("bookId", 1L);
        params.add("userId", 1L);
        params.add("borrowDate", "2023-01-01");
        params.add("duration", 9);
        HttpEntity<?> request = new HttpEntity<Object>(params);

        // Act
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(borrowUrl, request, String.class);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, borrowRepository.findAll().size());

        Borrow result = borrowRepository.findAll().get(0);

        assertEquals(expectedUser.getName(), result.getUser().getName());
        assertEquals(expectedUser.getSurname(), result.getUser().getSurname());
        assertEquals(expectedUser.getEmail(), result.getUser().getEmail());
        assertEquals(expectedUser.getDateOfBirth(), result.getUser().getDateOfBirth());
        assertEquals(33, result.getUser().getAge());

        assertEquals(expectedBook.getName(), result.getBook().getName());
        assertEquals(expectedBook.getDescription(), result.getBook().getDescription());
        assertEquals(expectedBook.getPublishYear(), result.getBook().getPublishYear());
        assertEquals(expectedBook.isBorrowed(), result.getBook().isBorrowed());

        assertEquals(LocalDate.of(2023,01,01), result.getBorrowDate());
        assertEquals(LocalDate.of(2023,01,10), result.getReturnDate());
    }

    @Test
    @Sql(scripts = "classpath:borrowscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBorrow() {
        // Setup
        String deleteUrl = baseUrl.concat("borrow/delete/{borrowId}");
        assertEquals(1, borrowRepository.findAll().size());

        // Act
        ResponseEntity<String> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE,null, String.class, 1);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, borrowRepository.findAll().size());

    }

    @Test
    @Sql(scripts = "classpath:borrowscript.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM borrow WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM people WHERE id='1'",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void returnBook() {
        // Setup
        String returnUrl = baseUrl.concat("borrow/return/{borrowId}");
        HttpEntity<Long> request = new HttpEntity<Long>(1L);
        assertTrue(bookRepository.findById(1L).get().isBorrowed());

        // Act
        ResponseEntity<String> responseEntity = restTemplate.exchange(returnUrl, HttpMethod.PUT, request, String.class,1);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Book returnedBook = bookRepository.findById(1L).get();
        Borrow updatedBorrow = borrowRepository.findById(1L).get();

        assertFalse(returnedBook.isBorrowed());
        assertEquals(LocalDate.now(), updatedBorrow.getReturnDate());

    }
}