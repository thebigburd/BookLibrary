package com.thebigburd.LibraryApplication.Integration.Controller;


import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;


    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl=baseUrl.concat(":").concat(port+"").concat("/app/book");
    }


    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBook() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, false);
        String getBookUrl = baseUrl.concat("/1");

        // Act
        Book result = restTemplate.getForObject(getBookUrl, Book.class);

        // Assert
        assertEquals(expectedBook.getName(), result.getName());
        assertEquals(expectedBook.getDescription(), result.getDescription());
        assertEquals(expectedBook.getPublishYear(), result.getPublishYear());
        assertEquals(expectedBook.isBorrowed(), result.isBorrowed());
    }

    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookList() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, false);
        String getBooklistUrl = baseUrl.concat("/booklist");

        // Act
        ParameterizedTypeReference<List<Book>> responseType = new ParameterizedTypeReference<List<Book>>() {};
        List<Book> response = restTemplate.exchange(getBooklistUrl, HttpMethod.GET, null, responseType).getBody();

        // Assert
        assertEquals(1, response.size());
        assertEquals(1, bookRepository.findAll().size());

        Book result = response.get(0);

        assertEquals(expectedBook.getName(), result.getName());
        assertEquals(expectedBook.getDescription(), result.getDescription());
        assertEquals(expectedBook.getPublishYear(), result.getPublishYear());
        assertEquals(expectedBook.isBorrowed(), result.isBorrowed());
    }

    @DirtiesContext
    @Test
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A Description", 2023, false);
        String addBookUrl = baseUrl.concat("/add");
        HttpEntity<Book> request = new HttpEntity<>(book);

        // Act
        HttpStatus status = restTemplate.postForEntity(addBookUrl, request, Book.class).getStatusCode();


        // Assert
        assertEquals(HttpStatus.OK, status);
        assertEquals(1, bookRepository.findAll().size());

    }

    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBook() {
        // Setup
        String deleteUrl = baseUrl.concat("/delete/1");
        assertEquals(1, bookRepository.findAll().size());


        // Act
        restTemplate.delete(deleteUrl);

        // Assert
        assertEquals(0, bookRepository.findAll().size());

    }

    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook() {
        // Setup
        String updateUrl = UriComponentsBuilder.fromHttpUrl(baseUrl.concat("/update/{userId}"))
                .queryParam("name", "A New Book")
                .queryParam("description","A New Description")
                .queryParam("publishYear", 2007).build().toUriString();

        Book book = new Book(1L, "A New Book", "A New Description", 2007, false);
        HttpEntity<Book> request = new HttpEntity<>(book);

        // Act
        HttpStatus response = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, Book.class,1).getStatusCode();

        // Assert
        assertEquals(HttpStatus.OK, response);

        Book result = bookRepository.findById(1L).get();

        assertEquals(book.getName(), result.getName());
        assertEquals(book.getDescription(), result.getDescription());
        assertEquals(book.getPublishYear(), result.getPublishYear());
    }
}
