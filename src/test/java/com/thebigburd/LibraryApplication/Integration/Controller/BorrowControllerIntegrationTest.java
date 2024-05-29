package com.thebigburd.LibraryApplication.Integration.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Controller.Request.ReturnRequest;
import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class BorrowControllerIntegrationTest {

	@Container
	public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("password");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowRepository borrowRepository;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		borrowRepository.deleteAll();
		bookRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User(null, "john.doe@example.com",  "John","Doe", "password", "123 Main St", "0123 456789", UserRole.ROLE_USER,
			LocalDate.of(1990, 1, 1), 1, 3);
		userRepository.save(user);
		Book bookOne = new Book(null, "First Book", "A blank description", 2000, 0, 2, BookStatus.UNAVAILABLE);
		bookRepository.save(bookOne);
		Borrow borrow = new Borrow(null, bookOne, user, LocalDate.of(2000, 1, 1), null, false, BorrowStatus.BORROWED);
		borrowRepository.save(borrow);
	}

	@Test
	public void testGetUserHistory() throws Exception {
		User user = userRepository.findAll().get(0);
		mockMvc.perform(get("/athena/users/user/" +user.getId() + "/history"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.borrows").isArray())
			.andExpect(jsonPath("$.data.borrows[0].book.name").value("First Book"))
			.andExpect(jsonPath("$.data.borrows[0].borrow_date").value("2000-01-01"))
			.andExpect(jsonPath("$.data.borrows[0].returned").value(false))
			.andExpect(jsonPath("$.data.borrows[0].borrow_status").value("BORROWED"));
	}

	@Test
	public void testGetBookHistory() throws Exception {
		Book book = bookRepository.findAll().get(0);
		mockMvc.perform(get("/athena/library/book/" +book.getId() + "/history"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.borrows").isArray())
			.andExpect(jsonPath("$.data.borrows[0].user.name").value("John"))
			.andExpect(jsonPath("$.data.borrows[0].book.name").value("First Book"))
			.andExpect(jsonPath("$.data.borrows[0].borrowDate").value("2000-01-01"))
			.andExpect(jsonPath("$.data.borrows[0].returned").value(false))
			.andExpect(jsonPath("$.data.borrows[0].borrowStatus").value("BORROWED"));
	}

	@Test
	public void testGetAllBorrowed() throws Exception {
		mockMvc.perform(get("/athena/library/borrow/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.borrows").isArray())
			.andExpect(jsonPath("$.data.borrows[0].user.name").value("John"))
			.andExpect(jsonPath("$.data.borrows[0].book.name").value("First Book"))
			.andExpect(jsonPath("$.data.borrows[0].borrowDate").value("2000-01-01"))
			.andExpect(jsonPath("$.data.borrows[0].returned").value(false))
			.andExpect(jsonPath("$.data.borrows[0].borrowStatus").value("BORROWED"));
	}

	@Test
	public void testBorrowBook() throws Exception {
		User user = userRepository.findAll().get(0);
		bookRepository.save(new Book(null, "New Book", "A new description", 2024, 1, 1, BookStatus.AVAILABLE));
		Book newBook = bookRepository.findAll().get(1);
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(user.getId());
		borrowRequest.setDuration(7);
		borrowRequest.setBorrowDate(LocalDate.of(2024,1,1));

		mockMvc.perform(post("/athena/library/book/" +newBook.getId() + "/borrow")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(borrowRequest)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.borrow.user.name").value("John"))
			.andExpect(jsonPath("$.data.borrow.user.borrowCount").value(2))
			.andExpect(jsonPath("$.data.borrow.book.name").value("New Book"))
			.andExpect(jsonPath("$.data.borrow.book.currentStock").value(0))
			.andExpect(jsonPath("$.data.borrow.book.status").value("UNAVAILABLE"))
			.andExpect(jsonPath("$.data.borrow.borrowDate").value("2024-01-01"))
			.andExpect(jsonPath("$.data.borrow.returnDate").value("2024-01-08"))
			.andExpect(jsonPath("$.data.borrow.returned").value(false))
			.andExpect(jsonPath("$.data.borrow.borrowStatus").value("BORROWED"));
	}

	@Test
	public void testDeleteBorrow() throws Exception {
		Borrow borrow = borrowRepository.findAll().get(0);
		borrow.setReturned(true);
		borrowRepository.save(borrow);
		mockMvc.perform(delete("/athena/library/borrow/delete/" + borrow.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.delete").value(true));
	}

	@Test
	public void testReturnBook() throws Exception {
		User user = userRepository.findAll().get(0);
		Borrow borrow = borrowRepository.findAll().get(0);
		ReturnRequest returnRequest = new ReturnRequest();
		returnRequest.setUserId(user.getId());
		returnRequest.setReturnDate(LocalDate.of(2000, 1, 8));

		mockMvc.perform(put("/athena/library/borrow/return/" + borrow.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(returnRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.return.user.name").value("John"))
			.andExpect(jsonPath("$.data.return.user.borrowCount").value(0))
			.andExpect(jsonPath("$.data.return.book.name").value("First Book"))
			.andExpect(jsonPath("$.data.return.book.currentStock").value(1))
			.andExpect(jsonPath("$.data.return.book.status").value("AVAILABLE"))
			.andExpect(jsonPath("$.data.return.borrowDate").value("2000-01-01"))
			.andExpect(jsonPath("$.data.return.returnDate").value("2000-01-08"))
			.andExpect(jsonPath("$.data.return.returned").value(true))
			.andExpect(jsonPath("$.data.return.borrowStatus").value("RETURNED"));

	}
}
