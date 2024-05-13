package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.BorrowController;
import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Controller.Request.ReturnRequest;
import com.thebigburd.LibraryApplication.Model.*;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Service.BorrowServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BorrowControllerTest {

    @Mock
    private BorrowMapper borrowMapper;

    @Mock
    private BorrowServiceImpl borrowService;


    @InjectMocks
    private BorrowController borrowController;



    @Test
    public void getUserBorrowedReturnsListOfDTO() {
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
        LocalDate borrowDate = LocalDate.of(2023,4,1);
        LocalDate returnDate = borrowDate.plusDays(14);
		BorrowDTO borrowDTO = new BorrowDTO(1L, book, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);


        List<BorrowDTO> borrowList = Arrays.asList(borrowDTO);
        when(borrowService.getUserHistory(anyLong())).thenReturn(borrowList);


        // Act
        ResponseEntity<Response> result = borrowController.getUserHistory(1L);

        // Assert
        assertEquals(result.getBody().getData().size(),1);
        List<BorrowDTO> userBorrowed = (List<BorrowDTO>) result.getBody().getData().get("borrows");
		BorrowDTO borrowed = userBorrowed.get(0);
        assertEquals(1L, (long) borrowed.id());
        assertEquals(borrowed.book().getId(), book.getId());
        assertEquals(borrowed.book().getName(), book.getName());
        assertEquals(borrowed.book().getDescription(), book.getDescription());
        assertEquals(borrowed.borrowDate(), borrowDate);
        assertEquals(borrowed.returnDate(), returnDate);
    }

    @Test
    public void getUserBorrowedFailsIfUserNotExist() {
        // Setup
        when(borrowService.getUserHistory(1L)).thenThrow(new IllegalArgumentException("User with id 1 does not exist."));

        // Act
        ResponseEntity<Response> result = borrowController.getUserHistory(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody().getData());
    }

    @Test
    public void getBookBorrowedHistoryReturnsBorrowHistory() {
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, 1, 1, BookStatus.AVAILABLE);
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3);
		Borrow borrow = new Borrow(1L, book, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
        when(borrowService.getBookHistory(1L)).thenReturn(List.of(borrow));

        // Act
        ResponseEntity<Response> result = borrowController.getBookHistory(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(borrow), result.getBody().getData().get("borrows"));
    }

    @Test
    public void getBookBorrowedFailsIfBookDoesNotExist() {
        // Setup
        when(borrowService.getBookHistory(1L)).thenThrow(new IllegalArgumentException("Book with id 1 does not exist."));

        // Act
        ResponseEntity<Response> result = borrowController.getBookHistory(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertEquals("Book with id 1 does not exist.", result.getBody().getMessage());
    }

    @Test
    public void getAllBorrowedReturnsBorrowList() {
        // Setup
        Borrow borrow1 = new Borrow();
        Borrow borrow2 = new Borrow();
        List<Borrow> borrowList = List.of(borrow1, borrow2);
        when(borrowService.getAllBorrowed()).thenReturn(borrowList);

        // Act
        ResponseEntity<Response> result = borrowController.getAllBorrowed();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(borrowList, result.getBody().getData().get("borrows"));
    }

    @Test
    public void borrowBookCreatesNewBorrow() {
		// Setup
		User user = new User();
		user.setId(1L);
		Book book = new Book();
		book.setId(1L);
		Borrow borrow = new Borrow(1L, book, user, LocalDate.of(2024,1,1), LocalDate.of(2024,1,8), false, BorrowStatus.BORROWED);
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(1L);
		borrowRequest.setBorrowDate(LocalDate.of(2024, 1, 1));
		borrowRequest.setDuration(7);
		when(borrowService.borrowBook(anyLong(), any(BorrowRequest.class))).thenReturn(borrow);

        // Act
        ResponseEntity<Response> result = borrowController.borrowBook(1L, borrowRequest);

        // Assert
        verify(borrowService).borrowBook(1L, borrowRequest);
        assertEquals(HttpStatus.CREATED, result.getStatusCode() );
        assertEquals("Book has been successfully borrowed!", result.getBody().getMessage());
		assertEquals(borrow ,result.getBody().getData().get("borrow"));
    }

    @Test
    public void borrowBookFailsIfBookUnavailable() {
        // Setup
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setDuration(7);
        doThrow(new IllegalArgumentException("This book is currently unavailable.")).when(borrowService).borrowBook(1L, borrowRequest);

        // Act
        ResponseEntity<Response> result = borrowController.borrowBook(1L, borrowRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("This book is currently unavailable.", result.getBody().getMessage());
    }

    @Test
    public void borrowBookFailsIfUserNotExist() {
        // Setup
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setUserId(1L);
		borrowRequest.setBorrowDate(LocalDate.of(2024, 1, 1));
		borrowRequest.setDuration(7);
        doThrow(new IllegalArgumentException("User with id 1 does not exist.")).when(borrowService).borrowBook(1L, borrowRequest);

        // Act
        ResponseEntity<Response> result = borrowController.borrowBook(1L, borrowRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("User with id 1 does not exist.", result.getBody().getMessage());
    }

    @Test
    public void borrowBookFailsIfBookNotExist() {
        // Setup
		BorrowRequest borrowRequest = new BorrowRequest();
		borrowRequest.setDuration(7);
        doThrow(new IllegalArgumentException("Book with id 1 not found.")).when(borrowService).borrowBook(1L, borrowRequest);

        // Act
        ResponseEntity<Response> result = borrowController.borrowBook(1L, borrowRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("Book with id 1 not found.", result.getBody().getMessage());
    }

	@Test
	public void borrowBookFailsIfDurationNotSpecified() {
		// Setup
		BorrowRequest borrowRequest = new BorrowRequest();

		// Act
		ResponseEntity<Response> result = borrowController.borrowBook(1L, borrowRequest);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
		assertEquals("Borrow duration cannot be 0 days.", result.getBody().getMessage());
	}

    @Test
    public void deleteBorrow() {
        // Act
        ResponseEntity<Response> result = borrowController.deleteBorrow(1L);

        // Assert
        verify(borrowService).deleteBorrow(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Borrow Entry deleted successfully.", result.getBody().getMessage());
    }

    @Test
    public void deleteBorrowFailsIfNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Invalid Borrow ID.")).when(borrowService).deleteBorrow(1L);

        // Act
        ResponseEntity<Response> result = borrowController.deleteBorrow(1L);

        // Assert
        verify(borrowService).deleteBorrow(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid Borrow ID.", result.getBody().getMessage());
    }

    @Test
    public void returnBook() {
		// Setup
		User user = new User();
		user.setId(1L);
		Book book = new Book();
		book.setId(1L);
		Borrow borrow = new Borrow(1L, book, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15), false, BorrowStatus.BORROWED);
		ReturnRequest returnRequest = new ReturnRequest();
		returnRequest.setUserId(1L);
		returnRequest.setReturnDate(LocalDate.now());
		when(borrowService.returnBook(anyLong(), any(ReturnRequest.class))).thenReturn(borrow);


        // Act
        ResponseEntity<Response> result = borrowController.returnBook(1L, returnRequest);

        // Assert
        verify(borrowService).returnBook(1L, returnRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book has been returned successfully.", result.getBody().getMessage());
    }

    @Test
    public void returnBookFailsIfBorrowNotExist() {
        // Setup
		ReturnRequest returnRequest = new ReturnRequest();
		returnRequest.setUserId(1L);

        doThrow(new IllegalArgumentException("Invalid borrow Id.")).when(borrowService).returnBook(1L, returnRequest);

        // Act
        ResponseEntity<Response> result = borrowController.returnBook(1L, returnRequest);

        // Assert
        verify(borrowService).returnBook(1L, returnRequest);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid borrow Id.", result.getBody().getMessage());
    }

}
