package com.thebigburd.LibraryApplication.Unit.Service;

import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Service.BookServiceImpl;
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
	private BookServiceImpl bookService;

	@Test
	public void getBookReturnsBookIfExists() {
		// Setup
		Long bookId = 1L;
		Book book = new Book(bookId, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));

		// Act
		Book result = bookService.getBook(bookId);

		// Assert
		assertEquals(result, book);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBookThrowsExceptionIfNotFound() {
		// Act
		bookService.getBook(1L);

		// Assert exception thrown
	}

	@Test
	public void returnListOfBooks() {
		// Setup
		List<Book> bookList = Arrays.asList(
			new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE),
			new Book(2L, "A Second Book", "A blank description", 2007, 1, 1, BookStatus.AVAILABLE)
		);

		when(bookRepository.findAll()).thenReturn(bookList);

		// Act
		List<Book> result = bookService.getBooklist();

		// Assert
		assertEquals(result, bookList);
	}

	@Test
	public void addBookToLibrary() {
		// Setup
		Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		when(bookRepository.findBookByName("A Book")).thenReturn(Optional.empty());

		// Act
		bookService.addBook(book);

		// Assert
		verify(bookRepository).save(book);

	}

	@Test
	public void addBookIncrementsStockIfPresent() {
		// Setup
		Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		when(bookRepository.findBookByName("A Book")).thenReturn(Optional.of(book));

		// Act
		bookService.addBook(book);

		// Assert
		assertEquals(2, book.getCurrentStock());
		assertEquals(2, book.getTotalStock());
		verify(bookRepository).save(book);

	}

	@Test
	public void deleteBook() {
		// Setup
		Book book = new Book(1L, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		when(bookRepository.existsById(1L)).thenReturn(true);

		// Act
		bookService.deleteBook(1L);

		// Assert
		verify(bookRepository).deleteById(1L);
	}


	@Test(expected = IllegalArgumentException.class)
	public void deleteBookThrowsExceptionIfNotExists() {
		// Setup
		when(bookRepository.existsById(1L)).thenReturn(false);

		// Act
		bookService.deleteBook(1L);

		// Assert exception is thrown
	}

	@Test
	public void updateBook() {
		// Setup
		Long bookId = 1L;
		Book book = new Book(bookId, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		BookRequest bookRequest = new BookRequest();
		bookRequest.setName("New Book Name");
		bookRequest.setDescription("A New Description");
		bookRequest.setPublishYear(2023);
		bookRequest.setTotalStock(2);
		bookRequest.setStatus(BookStatus.UNAVAILABLE);
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));

		// Act
		bookService.updateBook(bookId, bookRequest);

		// Assert
		verify(bookRepository).save(book);
		assertEquals("New Book Name", book.getName());
		assertEquals("A New Description", book.getDescription());
		assertEquals(2023, book.getPublishYear());
		assertEquals(1, book.getCurrentStock());
		assertEquals(2, book.getTotalStock());
		assertEquals(BookStatus.UNAVAILABLE, book.getStatus());
	}

	@Test
	public void updateBookDoesNotUpdateIfUnchangedParameters() {
		// Setup
		Long bookId = 1L;
		Book book = new Book(bookId, "A Book", "A blank description", 2005, 1, 1, BookStatus.AVAILABLE);
		BookRequest bookRequest = new BookRequest();
		when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));

		// Act
		bookService.updateBook(1L, bookRequest);

		// Assert
		verify(bookRepository).save(book);
		assertEquals("A Book", book.getName());
		assertEquals("A blank description", book.getDescription());
		assertEquals(2005, book.getPublishYear());
		assertEquals(1, book.getCurrentStock());
		assertEquals(1, book.getTotalStock());
		assertEquals(BookStatus.AVAILABLE, book.getStatus());
	}
}
