package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Response;
import com.thebigburd.LibraryApplication.Service.BookServiceImpl;
import com.thebigburd.LibraryApplication.Model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.*;


@RestController
@RequestMapping(path = "athena/library")
public class BookController {

	private final BookServiceImpl bookService;


	@Autowired
	public BookController(BookServiceImpl bookService) {
		this.bookService = bookService;
	}

	@GetMapping(path = "/book/{bookId}")
	public ResponseEntity<Response> getBook(@PathVariable("bookId") Long id) {
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("book", bookService.getBook(id)))
					.message("Book retrieved successfully.")
					.statusCode(HttpStatus.OK.value())
					.status(HttpStatus.OK)
					.build()
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(null);
		}
	}

	@GetMapping(path = "/list")
	public ResponseEntity<Response> getBooklist() {
		return ResponseEntity.ok(
			new Response.Builder()
				.timeStamp(now())
				.data(Map.of("books", bookService.getBooklist()))
				.message("Book list retrieved successfully.")
				.statusCode(HttpStatus.OK.value())
				.status(HttpStatus.OK)
				.build()
		);
	}

	@PostMapping(path = "/add")
	public ResponseEntity<Response> addBook(@RequestBody Book book) {
		// Perhaps make a AddBookRequest class, remove status and id fields. To prevent overwriting by using same id.
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(
					new Response.Builder()
						.timeStamp(now())
						.data(Map.of("book", bookService.addBook(book)))
						.message("'" + book.getName() + "' has been added succesfully.")
						.statusCode(HttpStatus.CREATED.value())
						.status(HttpStatus.CREATED)
						.build()
				);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status((HttpStatus.BAD_REQUEST))
				.body(
					new Response.Builder()
						.timeStamp(now())
						.message(e.getMessage())
						.statusCode(HttpStatus.BAD_REQUEST.value())
						.status(HttpStatus.BAD_REQUEST)
						.build()
				);
		}
	}

	@DeleteMapping(path = "/delete/{bookId}")
	public ResponseEntity<Response> deleteBook(@PathVariable("bookId") Long id) {
		// Perhaps make delete only possible when Book's stock has been returned?
		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("delete", bookService.deleteBook(id)))
					.message("Book removed successfully.")
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

	@PutMapping(path = "/update/{bookId}")
	public ResponseEntity<Response> updateBook(@PathVariable("bookId") Long id, @RequestBody BookRequest bookRequest) {

		// Validate Stock Parameters if Provided
		if ((bookRequest.getCurrentStock() != null && bookRequest.getCurrentStock() < 0) ||
			(bookRequest.getTotalStock() != null && bookRequest.getTotalStock() < 0)) {
			return ResponseEntity.badRequest().body(
				new Response.Builder()
					.timeStamp(now())
					.message("Current Stock and Total Stock cannot be negative.")
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST)
					.build()
			);
		}
		// Validate updated currentStock is less than or equal to updated totalStock
		if (bookRequest.getCurrentStock() != null && bookRequest.getTotalStock() != null &&
			bookRequest.getCurrentStock() > bookRequest.getTotalStock()) {
			return ResponseEntity.badRequest().body(
				new Response.Builder()
					.timeStamp(now())
					.message("Total Stock cannot be less than Current Stock.")
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.status(HttpStatus.BAD_REQUEST)
					.build()
			);
		}

		try {
			return ResponseEntity.ok(
				new Response.Builder()
					.timeStamp(now())
					.data(Map.of("update", bookService.updateBook(id, bookRequest)))
					.message("Book updated successfully.")
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
