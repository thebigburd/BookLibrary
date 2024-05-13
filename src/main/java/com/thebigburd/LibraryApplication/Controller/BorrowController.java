package com.thebigburd.LibraryApplication.Controller;


import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Controller.Request.ReturnRequest;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.Response;
import com.thebigburd.LibraryApplication.Service.BorrowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@CrossOrigin(origins = "http://localhost:4000/")
@RequestMapping(path = "athena")
public class BorrowController {

	BorrowServiceImpl borrowService;


	@Autowired
	public BorrowController(BorrowServiceImpl borrowService) {
		this.borrowService = borrowService;
	}


	// Gets a User's list of borrowed books.
	@GetMapping(path = "/users/user/{userId}/history")
	public ResponseEntity<Response> getUserHistory(@PathVariable("userId") Long userId) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("borrows", borrowService.getUserHistory(userId)))
					.message("User's borrow history retrieved successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(
					new Response.Builder()
						.timeStamp(now())
						.message(e.getMessage())
						.statusCode(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND)
						.build()
				);
		}

	}

	@GetMapping(path = "/library/book/{bookId}/history")
	public ResponseEntity<Response> getBookHistory(@PathVariable("bookId") Long bookId) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("borrows", borrowService.getBookHistory(bookId)))
					.message("Book's borrow history retrieved successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(
					new Response.Builder()
					.timeStamp(now())
					.message(e.getMessage())
					.statusCode(HttpStatus.NOT_FOUND.value())
					.status(HttpStatus.NOT_FOUND)
					.build()
				);
		}
	}

	@GetMapping(path = "/library/borrow/list")
	public ResponseEntity<Response> getAllBorrowed() {
		return ResponseEntity.ok(
			new Response.Builder()
				.timeStamp(now())
				.data(Map.of("borrows", borrowService.getAllBorrowed()))
				.message("Borrow List retrieved successfully.")
				.statusCode(HttpStatus.OK.value())
				.status(HttpStatus.OK)
				.build()
		);
	}

	@PostMapping(path = "/library/book/{bookId}/borrow")
	public ResponseEntity<Response> borrowBook(@PathVariable("bookId") Long bookId, @RequestBody BorrowRequest borrowRequest) {
		// Validate Request Parameters

		if (borrowRequest.getBorrowDate() == null) {
			borrowRequest.setBorrowDate(LocalDate.now());  // Use Current Date if not specified.
		}

		if (borrowRequest.getDuration() == 0) {
			return ResponseEntity.badRequest().body(
				new Response.Builder()
					.timeStamp(now())
					.message("Borrow duration cannot be 0 days.")
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST)
					.build()
			);
		}

		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("borrow", borrowService.borrowBook(bookId, borrowRequest)))
					.message("Book has been successfully borrowed!")
					.statusCode(HttpStatus.CREATED.value())
					.status(HttpStatus.CREATED)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
					new Response.Builder()
						.timeStamp(now())
						.message(e.getMessage())
						.statusCode(HttpStatus.BAD_REQUEST.value())
						.status(HttpStatus.BAD_REQUEST)
						.build());
		}
	}

	@DeleteMapping(path = "/library/borrow/delete/{borrowId}")
	public ResponseEntity<Response> deleteBorrow(@PathVariable("borrowId") Long borrowId) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("delete", borrowService.deleteBorrow(borrowId)))
					.message("Borrow Entry deleted successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new Response.Builder()
					.timeStamp(now())
					.status(HttpStatus.NOT_FOUND)
					.statusCode(HttpStatus.NOT_FOUND.value())
					.message(e.getMessage())
					.build());
		}
	}

	@PutMapping(path = "/library/borrow/return/{borrowId}")
	public ResponseEntity<Response> returnBook(@PathVariable("borrowId") Long borrowId,
											   @RequestBody ReturnRequest returnRequest) {

		if (returnRequest.getReturnDate() == null) {
			returnRequest.setReturnDate(LocalDate.now());  // Use Current Date if not specified.
		}
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("return", borrowService.returnBook(borrowId, returnRequest)))
					.message("Book has been returned successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(
					new Response.Builder()
						.timeStamp(now())
						.message(e.getMessage())
						.statusCode(HttpStatus.NOT_FOUND.value())
						.status(HttpStatus.NOT_FOUND)
						.build()
				);
		}
	}
}
