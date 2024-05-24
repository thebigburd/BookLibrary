package com.thebigburd.LibraryApplication.Integration.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.User;
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
public class UserControllerIntegrationTest {

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
	private BorrowRepository borrowRepository;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		borrowRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User();
		user.setName("John");
		user.setEmail("john.doe@example.com");
		user.setSurname("Doe");
		user.setPassword("password");
		user.setAddress("123 Main St");
		user.setPhone("0123 456789");
		user.setDateOfBirth(LocalDate.of(1990, 1, 1));
		userRepository.save(user);
	}

	@Test
	public void testGetUser() throws Exception {
		User user = userRepository.findAll().get(0);
		mockMvc.perform(get("/athena/users/user/" + user.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.user.name").value("John"))
			.andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
			.andExpect(jsonPath("$.data.user.surname").value("Doe"))
			.andExpect(jsonPath("$.data.user.address").value("123 Main St"))
			.andExpect(jsonPath("$.data.user.phone").value("0123 456789"))
			.andExpect(jsonPath("$.data.user.dateOfBirth").value("1990-01-01"));
	}

	@Test
	public void testGetUserList() throws Exception {
		mockMvc.perform(get("/athena/users/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.users").isArray())
			.andExpect(jsonPath("$.data.users[0].name").value("John"))
			.andExpect(jsonPath("$.data.users[0].email").value("john.doe@example.com"))
			.andExpect(jsonPath("$.data.users[0].surname").value("Doe"))
			.andExpect(jsonPath("$.data.users[0].address").value("123 Main St"))
			.andExpect(jsonPath("$.data.users[0].phone").value("0123 456789"))
			.andExpect(jsonPath("$.data.users[0].dateOfBirth").value("1990-01-01"));
	}

	@Test
	public void testAddUser() throws Exception {
		UserRequest newUser = new UserRequest();
		newUser.setName("Jane");
		newUser.setEmail("jane.doe@example.com");
		newUser.setSurname("Doe");
		newUser.setPassword("password");
		newUser.setAddress("456 Main St");
		newUser.setPhone("0111 111222");
		newUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

		mockMvc.perform(post("/athena/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newUser)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.user.name").value("Jane"))
			.andExpect(jsonPath("$.data.user.email").value("jane.doe@example.com"))
			.andExpect(jsonPath("$.data.user.surname").value("Doe"))
			.andExpect(jsonPath("$.data.user.address").value("456 Main St"))
			.andExpect(jsonPath("$.data.user.phone").value("0111 111222"))
			.andExpect(jsonPath("$.data.user.dateOfBirth").value("2000-01-01"));
	}

	@Test
	public void testDeleteUser() throws Exception {
		User user = userRepository.findAll().get(0);
		mockMvc.perform(delete("/athena/users/delete/" + user.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.delete").value(true));
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = userRepository.findAll().get(0);
		UserRequest userRequest = new UserRequest();
		userRequest.setName("Joe");
		userRequest.setSurname("Mana");
		userRequest.setPassword("newpassword");
		userRequest.setAddress("789 Main St");
		userRequest.setPhone("0987 654321");
		userRequest.setDateOfBirth(LocalDate.of(1990, 2, 2));

		mockMvc.perform(put("/athena/users/update/" + user.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.update.name").value("Joe"))
			.andExpect(jsonPath("$.data.update.email").value("john.doe@example.com"))
			.andExpect(jsonPath("$.data.update.surname").value("Mana"))
			.andExpect(jsonPath("$.data.update.password").value("newpassword"))
			.andExpect(jsonPath("$.data.update.address").value("789 Main St"))
			.andExpect(jsonPath("$.data.update.phone").value("0987 654321"))
			.andExpect(jsonPath("$.data.update.dateOfBirth").value("1990-02-02"));

	}
}
