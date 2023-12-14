package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.BookController;
import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void getBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false);
        when(bookService.getBook(1L)).thenReturn(book);

        // Act
        ResponseEntity<Book> result = bookController.getBook(1L);

        // Assert
        assertEquals(book, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getBookFailsIfBookNotExist() {
        // Setup
        when(bookService.getBook(1L)).thenThrow(new IllegalArgumentException("Book does not exist with the id 1."));

        // Act
        ResponseEntity<Book> result = bookController.getBook(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(null, result.getBody());
    }

    @Test
    public void getBookList() {
        // Setup
        List<Book> bookList = Arrays.asList(
                new Book(1L, "A Book", "A blank description", 2005, false ),
                new Book(2L, "A Second Book", "A blank description", 2007, false )
        );
        when(bookService.getBookList()).thenReturn(bookList);

        // Act
        ResponseEntity<List<Book>> result = bookController.getBookList();

        // Assert
        assertEquals(bookList, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );

        // Act
        ResponseEntity<String> result = bookController.addBook(book);

        // Assert
        verify(bookService).addBook(book);
        assertEquals(HttpStatus.CREATED,result.getStatusCode() );
        assertEquals("Book added successfully.", result.getBody());
    }

    @Test
    public void addBookFailsIfExists() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false);
        doThrow(new IllegalArgumentException("This book already exists in the library.")).when(bookService).addBook(book);

        // Act
        ResponseEntity<String> result = bookController.addBook(book);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("This book already exists in the library.", result.getBody());
    }

    @Test
    public void deleteBook() {
        // Act
        ResponseEntity<String> result = bookController.deleteBook(1L);

        // Assert
        verify(bookService).deleteBook(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book deleted successfully.", result.getBody());
    }

    @Test
    public void deleteBookFailsIfNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Book with id 1 does not exist.")).when(bookService).deleteBook(1L);

        // Act
        ResponseEntity<String> result = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Book with id 1 does not exist.", result.getBody());
    }

    @Test
    public void updateBook() {
        // Act
        ResponseEntity<String> result = bookController.updateBook(1L, "A Book", "A blank description", 2005);

        // Assert
        verify(bookService).updateBook(1L, "A Book", "A blank description", 2005);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book updated successfully", result.getBody());
    }

    @Test
    public void updateBookFailsIfBookNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Book with the id 1 does not exist.")).when(bookService)
                .updateBook(1L, "A Book", "A blank description", 2005);

        // Act
        ResponseEntity<String> result = bookController.updateBook(1L, "A Book", "A blank description", 2005);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Book with the id 1 does not exist.", result.getBody());
    }
}