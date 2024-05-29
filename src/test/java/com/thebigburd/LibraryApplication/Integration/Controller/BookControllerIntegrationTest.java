package com.thebigburd.LibraryApplication.Integration.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class BookControllerIntegrationTest {

	@Container
	public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("password");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowRepository borrowRepository;

	@Autowired
	private BookServiceImpl bookService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		borrowRepository.deleteAll();
		bookRepository.deleteAll();
		Book bookOne = new Book(null, "First Book", "A blank description", 2000, 1, 1, BookStatus.AVAILABLE);
		System.out.println( bookRepository.save(bookOne)) ;
		Book bookTwo = new Book(null, "Second Book", "Another description", 2001, 1, 2, BookStatus.UNAVAILABLE);
		System.out.println( bookRepository.save(bookTwo)) ;
	}

	@Test
	public void testGetBook() throws Exception {
		Book book = bookRepository.findAll().get(0);
		mockMvc.perform(get("/athena/library/book/" + book.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.book.name").value("First Book"))
			.andExpect(jsonPath("$.data.book.description").value("A blank description"))
			.andExpect(jsonPath("$.data.book.publishYear").value(2000))
			.andExpect(jsonPath("$.data.book.currentStock").value(1))
			.andExpect(jsonPath("$.data.book.totalStock").value(1))
			.andExpect(jsonPath("$.data.book.status").value("AVAILABLE"));
	}

	@Test
	public void testGetBookList() throws Exception {
		mockMvc.perform(get("/athena/library/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.books").isArray())
			.andExpect(jsonPath("$.data.books[0].name").value("First Book"))
			.andExpect(jsonPath("$.data.books[1].name").value("Second Book"));
	}

	@Test
	public void testAddBook() throws Exception {
		BookRequest newBook = new BookRequest();
		newBook.setName("New Book");
		newBook.setDescription("New description");
		newBook.setPublishYear(2024);
		newBook.setCurrentStock(9);
		newBook.setTotalStock(10);
		newBook.setStatus(BookStatus.AVAILABLE);

		mockMvc.perform(post("/athena/library/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newBook)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.book.name").value("New Book"))
			.andExpect(jsonPath("$.data.book.description").value("New description"))
			.andExpect(jsonPath("$.data.book.publishYear").value(2024))
			.andExpect(jsonPath("$.data.book.currentStock").value(9))
			.andExpect(jsonPath("$.data.book.totalStock").value(10))
			.andExpect(jsonPath("$.data.book.status").value("AVAILABLE"));
	}

	@Test
	public void testDeleteBook() throws Exception {
		Book book = bookRepository.findAll().get(0);
		mockMvc.perform(delete("/athena/library/delete/" + book.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.delete").value(true));
	}

	@Test
	public void testUpdateBook() throws Exception {
		Book book = bookRepository.findAll().get(0);
		BookRequest bookRequest = new BookRequest();
		bookRequest.setName("Updated Book");
		bookRequest.setDescription("Updated description");
		bookRequest.setPublishYear(2025);
		bookRequest.setCurrentStock(4);
		bookRequest.setTotalStock(5);
		bookRequest.setStatus(BookStatus.UNAVAILABLE);

		mockMvc.perform(put("/athena/library/update/" + book.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.update.name").value("Updated Book"))
			.andExpect(jsonPath("$.data.update.description").value("Updated description"))
			.andExpect(jsonPath("$.data.update.publishYear").value(2025))
			.andExpect(jsonPath("$.data.update.currentStock").value(4))
			.andExpect(jsonPath("$.data.update.totalStock").value(5))
			.andExpect(jsonPath("$.data.update.status").value("UNAVAILABLE"));
	}
}
