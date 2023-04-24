package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController;

    @Test
    public void getBook(){
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookService.getBook(1L)).thenReturn(book);

        // Act
        Book result = bookController.getBook(1L);

        // Assert
        assertEquals(result, book);
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
        List<Book> result = bookController.getBookList();

        // Assert
        assertEquals(result, bookList);
    }

    @Test
    public void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );

        // Act
        bookController.addBook(book);

        // Assert
        verify(bookService).addBook(book);
    }

    @Test
    public void deleteBook() {
        // Act
        bookController.deleteBook(1L);

        // Assert
        verify(bookService).deleteBook(1L);
    }

    @Test
    public void updateBook() {
        // Act
        bookController.updateBook(1L, "A Book", "A blank description",2005);

        // Assert
        verify(bookService).updateBook(1L, "A Book", "A blank description",2005);
    }
}