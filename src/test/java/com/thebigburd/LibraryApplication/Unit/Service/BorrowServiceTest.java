package com.thebigburd.LibraryApplication.Unit.Service;

import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.BorrowDTO;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.BorrowServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BorrowServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BorrowRepository borrowRepository;

	@Mock
	private BorrowMapper borrowMapper;

	@InjectMocks
	private BorrowServiceImpl borrowService;


	@Test
	public void getUserBorrowedReturnsBorrowDTOs() {
		// Setup
		Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		Book book2 = new Book(2L, "Book 2", "Description of book 2", 2022, 2, 2, BookStatus.AVAILABLE);
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		Borrow borrow1 = new Borrow(1L, book1, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
		Borrow borrow2 = new Borrow(2L, book2, user, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1), false, BorrowStatus.OVERDUE);
		BorrowDTO borrow1DTO = new BorrowDTO(1L, book1, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
		BorrowDTO borrow2DTO = new BorrowDTO(2L, book2, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1), false, BorrowStatus.OVERDUE);
		when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
		when(borrowRepository.findByUserId(1L)).thenReturn(List.of(borrow1, borrow2));
		when(borrowMapper.toDTO(borrow1)).thenReturn(borrow1DTO);
		when(borrowMapper.toDTO(borrow2)).thenReturn(borrow2DTO);

		// Act
		List<BorrowDTO> result = borrowService.getUserHistory(1L);

		// Assert
		assertEquals(List.of(borrow1DTO, borrow2DTO), result);
	}

	@Test
	public void getUserBorrowedReturnsEmptyListIfNoBorrows() {
		// Setup
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
		when(borrowRepository.findByUserId(1L)).thenReturn(List.of());

		// Act
		List<BorrowDTO> result = borrowService.getUserHistory(1L);

		// Assert
		assertEquals(List.of(), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUserBorrowedThrowsExceptionWhenInvalidId() {
		// Act
		borrowService.getUserHistory(1L);

		// Assert exception is thrown
	}


	@Test
	public void getBookBorrowedHistoryReturnsBorrows() {
		// Setup
		Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		Borrow borrow1 = new Borrow(1L, book, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
		when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
		when(borrowRepository.findByBookId(1L)).thenReturn(List.of(borrow1));

		// Act
		List<Borrow> result = borrowService.getBookHistory(1L);

		// Assert
		assertEquals(List.of(borrow1), result);
	}

	@Test
	public void getBookBorrowedHistoryReturnsEmptyListIfNoBorrows() {
		// Setup
		Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book1));
		when(borrowRepository.findByBookId(1L)).thenReturn(List.of());

		// Act
		List<Borrow> result = borrowService.getBookHistory(1L);

		// Assert
		assertEquals(List.of(), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBookBorrowedHistoryThrowsExceptionIfInvalidId() {
		// Act
		borrowService.getBookHistory(1L);

		// Assert exception is thrown
	}


	@Test
	public void getAllBorrowedReturnsBorrowList() {
		// Setup
		Borrow borrow1 = new Borrow();
		borrow1.setId(1L);
		Borrow borrow2 = new Borrow();
		borrow2.setId(2L);
		List<Borrow> expectedBorrows = Arrays.asList(borrow1, borrow2);
		when(borrowRepository.findAll()).thenReturn(expectedBorrows);

		// Act
		List<Borrow> actualBorrows = borrowService.getAllBorrowed();

		// Assert
		assertEquals(expectedBorrows, actualBorrows);
	}


	@Test
	public void borrowBookReducesCurrentStockAndChangesStatusIfNoStock() {
		// Setup
		Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(1L);
		borrowRequest.setBorrowDate(LocalDate.of(2024, 1, 1));
		borrowRequest.setDuration(7);
		when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// Act
		borrowService.borrowBook(1L, borrowRequest);

		// Assert
		assertEquals(0, book.getCurrentStock());
		assertEquals(BookStatus.UNAVAILABLE, book.getStatus());
		assertEquals(1, user.getBorrowCount());
		verify(borrowRepository, times(1)).save(any(Borrow.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void borrowBookThrowsExceptionIfBookNotFound() {
		// Setup
		BorrowRequest borrowRequest = new BorrowRequest();

		// Act
		borrowService.borrowBook(1L, borrowRequest);

		// Assert exception is thrown
	}

	@Test(expected = IllegalArgumentException.class)
	public void borrowBookThrowsExceptionIfUserNotFound() {
		// Setup
		Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(1L);
		borrowRequest.setBorrowDate(LocalDate.of(2024, 1, 1));
		borrowRequest.setDuration(7);

		// Act
		borrowService.borrowBook(1L, borrowRequest);

		// Assert exception is thrown
	}

	@Test(expected = IllegalArgumentException.class)
	public void borrowBookThrowsExceptionIfBookUnavailable() {
		// Setup
		Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.UNAVAILABLE);
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(1L);
		borrowRequest.setBorrowDate(LocalDate.of(2024, 1, 1));
		borrowRequest.setDuration(7);

		// Act
		borrowService.borrowBook(1L, borrowRequest);

		// Assert exception is thrown
	}

	@Test
	public void deleteBorrow() {
		// Setup
		Borrow borrow = new Borrow();
		borrow.setId(1L);
		borrow.setReturned(true);
		when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrow));

		// Act
		borrowService.deleteBorrow(1L);

		// Assert
		verify(borrowRepository).deleteById(1L);
	}


	@Test(expected = IllegalArgumentException.class)
	public void deleteBorrowThrowsExceptionIfBorrowNotExist() {
		// Setup
		when(borrowRepository.findById(1L)).thenReturn(Optional.empty());

		// Act
		borrowService.deleteBorrow(1L);

		// Assert exception is thrown
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteBorrowThrowsExceptionIfBookNotReturnedFirst() {
		// Setup
		Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.UNAVAILABLE);
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		Borrow borrow = new Borrow(1L, book, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
		when(borrowRepository.findById(1L)).thenReturn(Optional.of(borrow));

		// Act
		borrowService.deleteBorrow(1L);

		// Assert exception is thrown
	}
}
