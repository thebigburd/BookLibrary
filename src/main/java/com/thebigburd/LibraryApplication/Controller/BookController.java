package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Service.BookService;
import com.thebigburd.LibraryApplication.Entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "app/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable("bookId") Long id) {
        try {
            Book book = bookService.getBook(id);
            return ResponseEntity.ok(book);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(null);
        }
    }

    @GetMapping(path = "booklist")
    public ResponseEntity<List<Book>> getBookList() {
        List<Book> bookList = bookService.getBookList();
        return ResponseEntity.ok(bookList);
    }

    @PostMapping(path = "add")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        try {
            bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable("bookId") Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(path = "update/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable("bookId")Long id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) int publishYear) {
        try {
            bookService.updateBook(id, name, description, publishYear);
            return ResponseEntity.ok("Book updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
