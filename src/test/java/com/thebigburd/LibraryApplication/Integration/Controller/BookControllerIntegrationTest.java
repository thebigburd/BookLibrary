package com.thebigburd.LibraryApplication.Integration.Controller;


import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl="http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;


    @BeforeEach
    public void setUp(){
        restTemplate = new RestTemplate();
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
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(getBookUrl, Book.class);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBook.getName(), responseEntity.getBody().getName());
        assertEquals(expectedBook.getDescription(), responseEntity.getBody().getDescription());
        assertEquals(expectedBook.getPublishYear(), responseEntity.getBody().getPublishYear());
        assertEquals(expectedBook.isBorrowed(), responseEntity.getBody().isBorrowed());
    }

    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookList() {
        // Setup
        Book expectedBook = new Book(1L, "A Book", "A Description", 2023, false);
        String getBooklistUrl = baseUrl.concat("/booklist");

        // Act
        ParameterizedTypeReference<List<Book>> responseType = new ParameterizedTypeReference<List<Book>>() {
        };
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(getBooklistUrl, HttpMethod.GET, null, responseType);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(1, bookRepository.findAll().size());

        Book responseBody = responseEntity.getBody().get(0);
        assertEquals(expectedBook.getName(), responseBody.getName());
        assertEquals(expectedBook.getDescription(), responseBody.getDescription());
        assertEquals(expectedBook.getPublishYear(), responseBody.getPublishYear());
        assertEquals(expectedBook.isBorrowed(), responseBody.isBorrowed());
    }

    @Test
    @Sql(statements = "DELETE FROM book WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A Description", 2023, false);
        String addBookUrl = baseUrl.concat("/add");
        HttpEntity<Book> request = new HttpEntity<>(book);

        // Act
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(addBookUrl, request, String.class);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO book (id, name, description, publish_year, borrowed) VALUES (1, 'A Book', 'A Description', 2023, False)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBook() {
        // Setup
        String deleteUrl = baseUrl.concat("/delete/1");
        assertEquals(1, bookRepository.findAll().size());

        // Act
       ResponseEntity<String> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, String.class);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        ResponseEntity<String> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, String.class,1);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Book result = bookRepository.findById(1L).get();

        assertEquals(book.getName(), result.getName());
        assertEquals(book.getDescription(), result.getDescription());
        assertEquals(book.getPublishYear(), result.getPublishYear());
    }
}
