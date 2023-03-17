package com.thebigburd.LibraryApplication.Book;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "/{bookId}")
    public Book getBook(@PathVariable("bookId") Long id) {
        return bookService.getBook(id);
    }

    @GetMapping(path = "booklist")
    public List<Book> getBookList() {
        return bookService.getBookList();
    }

    @PostMapping(path = "new")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @DeleteMapping(path = "delete/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping(path = "update/{bookId}")
    public void updateBook(@PathVariable("bookId")Long id,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) int publishYear) {
        bookService.updateBook(id, name, description, publishYear);
    }
}
