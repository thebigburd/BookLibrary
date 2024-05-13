package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.Response;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.*;

@RestController
@CrossOrigin(origins = "http://localhost:4000/")
@RequestMapping(path = "athena/users")
public class UserController {

	private final UserServiceImpl userService;

	@Autowired
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@GetMapping(path = "/user/{userId}")
	public ResponseEntity<Response> getUser(@PathVariable("userId") Long id) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("user", userService.getUser(id)))
					.message("User retrieved successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping(path = "/list")
	public ResponseEntity<Response> getUserlist() {
		return ResponseEntity.ok(
			new Response.Builder()
				.timeStamp(now())
				.data(Map.of("users", userService.getUserlist()))
				.message("User list retrieved successfully.")
				.statusCode(HttpStatus.OK.value())
				.status(HttpStatus.OK)
				.build()
		);
	}

	@PostMapping(path = "/register")
	public ResponseEntity<Response> addUser(@RequestBody User newUser) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(
					new Response.Builder()
						.timeStamp(now())
						.data(Map.of("user", userService.addUser(newUser)))
						.message("User successfully registered!")
						.statusCode(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED)
						.build()
				);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status((HttpStatus.BAD_REQUEST))
				.body(new Response.Builder()
					.message(e.getMessage())
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST)
					.build()
				);
		}
	}

	@DeleteMapping(path = "/delete/{userId}")
	public ResponseEntity<Response> deleteUser(@PathVariable("userId") Long id) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("delete", userService.deleteUser(id)))
					.message("User removed successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new Response.Builder()
					.timeStamp(now())
					.message(e.getMessage())
					.statusCode(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND)
					.build());
		}
	}

	@PutMapping(path = "/update/{userId}")
	public ResponseEntity<Response> updateUser(@PathVariable("userId") Long id, @RequestBody UserRequest userRequest) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("update", userService.updateUser(id, userRequest)))
					.message("User updated successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new Response.Builder()
					.timeStamp(now())
					.message(e.getMessage())
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST)
					.build());
		}
	}

}
