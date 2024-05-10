package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.BookController;
import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.Response;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Service.BookServiceImpl;
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
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void getBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE );
        when(bookService.getBook(1L)).thenReturn(book);

        // Act
        ResponseEntity<Response> result = bookController.getBook(1L);

        // Assert
        assertEquals(book, result.getBody().getData().get("book"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getBookFailsIfBookNotExist() {
        // Setup
        when(bookService.getBook(1L)).thenThrow(new IllegalArgumentException("Book does not exist with the id 1."));

        // Act
        ResponseEntity<Response> result = bookController.getBook(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void getBookList() {
        // Setup
        List<Book> bookList = Arrays.asList(
          new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE ),
          new Book(2L, "A Second Book", "A blank description", 2007, 1, 1, BookStatus.AVAILABLE )
        );
        when(bookService.getBooklist()).thenReturn(bookList);

        // Act
        ResponseEntity<Response> result = bookController.getBooklist();

        // Assert
        assertEquals(bookList, result.getBody().getData().get("books"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE );
		when(bookService.addBook(any(Book.class))).thenReturn(book);

        // Act
        ResponseEntity<Response> result = bookController.addBook(book);

        // Assert
        verify(bookService).addBook(book);
        assertEquals(HttpStatus.CREATED,result.getStatusCode() );
        assertEquals("'" + book.getName() + "' has been added succesfully.", result.getBody().getMessage());
    }

    @Test
    public void addBookFailsIfExists() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE );
        doThrow(new IllegalArgumentException("This book already exists in the library.")).when(bookService).addBook(book);

        // Act
        ResponseEntity<Response> result = bookController.addBook(book);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("This book already exists in the library.", result.getBody().getMessage());
    }

    @Test
    public void deleteBook() {
        // Act
        ResponseEntity<Response> result = bookController.deleteBook(1L);

        // Assert
        verify(bookService).deleteBook(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book removed successfully.", result.getBody().getMessage());
    }

    @Test
    public void deleteBookFailsIfNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Book with id 1 does not exist.")).when(bookService).deleteBook(1L);

        // Act
        ResponseEntity<Response> result = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Book with id 1 does not exist.", result.getBody().getMessage());
    }

    @Test
    public void updateBook() {
        // Setup
		Book book = new Book("A New Book", "A new description", 2000, 1, 2, BookStatus.UNAVAILABLE);
		BookRequest bookRequest = new BookRequest();
		bookRequest.setName("A New Book");
		bookRequest.setDescription("A new description");
		bookRequest.setPublishYear(2000);
		bookRequest.setCurrentStock(1);
		bookRequest.setTotalStock(2);
		bookRequest.setStatus(BookStatus.UNAVAILABLE);
		when(bookService.updateBook(anyLong(), any(BookRequest.class))).thenReturn(book);

		// Act
        ResponseEntity<Response> result = bookController.updateBook(1L, bookRequest);

        // Assert
        verify(bookService).updateBook(1L, bookRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book updated successfully.", result.getBody().getMessage());
    }

    @Test
    public void updateBookFailsIfBookNotExist() {
		BookRequest bookRequest = new BookRequest();
        // Setup
        doThrow(new IllegalArgumentException("Book with the id 1 does not exist.")).when(bookService)
                .updateBook(anyLong(), any(BookRequest.class));

        // Act
        ResponseEntity<Response> result = bookController.updateBook(1L,bookRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Book with the id 1 does not exist.", result.getBody().getMessage());
    }
}
