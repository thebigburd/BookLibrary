package com.thebigburd.LibraryApplication.Unit.Service;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void getBookReturnsBookIfExists() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Book result = bookService.getBook(1L);

        // Assert
        assertEquals(result, book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBookThrowsExceptionIfNotFound(){
        // Setup
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        bookService.getBook(1L);

        // Assert exception thrown
    }

    @Test
    public void getBookList() {
        // Setup
        List<Book> bookList = Arrays.asList(
                new Book(1L, "A Book", "A blank description", 2005, false ),
                new Book(2L, "A Second Book", "A blank description", 2007, false )
                );

        when(bookRepository.findAll()).thenReturn(bookList);

        // Act
        List<Book> result = bookRepository.findAll();

        // Assert
        assertEquals(result, bookList);
    }

    @Test
    public void addBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.existsByName("A Book")).thenReturn(false);

        // Act
        bookService.addBook(book);

        // Assert
        verify(bookRepository).save(book);

    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookThrowsExceptionIfExists(){
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.existsByName("A Book")).thenReturn(true);

        // Act
        bookService.addBook(book);

        // Assert exception thrown
    }

    @Test
    public void deleteBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.existsById(1L)).thenReturn(true);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository).deleteById(1L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteBookThrowsExceptionIfNotExists(){
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.existsById(1L)).thenReturn(false);

        // Act
        bookService.deleteBook(1L);

        // Assert exception is thrown
    }

    @Test
    public void updateBook() {
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        bookService.updateBook(1L, "New Book Name", "A New Description", 2023);

        // Assert
        verify(bookRepository).save(book);
        assertEquals("New Book Name", book.getName());
        assertEquals("A New Description", book.getDescription());
        assertEquals(2023, book.getPublishYear());

    }

    @Test
    public void updateBookDoesNotUpdateIfEmptyParameters(){
        // Setup
        Book book = new Book(1L, "A Book", "A blank description", 2005, false );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        String name = "";
        String description = "";
        int publishYear = 0;

        // Act
        bookService.updateBook(1L, name, description, publishYear);

        // Assert
        verify(bookRepository).save(book);
        assertEquals("A Book", book.getName());
        assertEquals("A blank description", book.getDescription());
        assertEquals(2005, book.getPublishYear());
    }
}