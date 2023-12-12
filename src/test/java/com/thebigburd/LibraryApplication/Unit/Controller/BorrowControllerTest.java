package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.BorrowController;
import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Service.BorrowService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BorrowControllerTest {

    @Mock
    private BorrowMapper borrowMapper;

    @Mock
    private BorrowService borrowService;


    @InjectMocks
    private BorrowController borrowController;



    @Test
    public void getUserBorrowedReturnsListOfDTO() {
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        LocalDate borrowDate = LocalDate.of(2023,4,1);
        LocalDate returnDate = borrowDate.plusDays(14);
        BorrowDTO borrowDTO = new BorrowDTO(1L, book, borrowDate, returnDate);

        List<BorrowDTO> borrowList = Arrays.asList(borrowDTO);
        when(borrowService.getUserBorrowed(anyLong())).thenReturn(borrowList);


        // Act
        ResponseEntity<List<BorrowDTO>> result = borrowController.getUserBorrowed(1L);

        // Assert
        assertEquals(result.getBody().size(),1);
        BorrowDTO userBorrowed = result.getBody().get(0);
        assertEquals(1L, userBorrowed.getId());
        assertEquals(userBorrowed.getBook().getId(), book.getId());
        assertEquals(userBorrowed.getBook().getName(), book.getName());
        assertEquals(userBorrowed.getBook().getDescription(), book.getDescription());
        assertEquals(userBorrowed.getBorrowDate(), borrowDate);
        assertEquals(userBorrowed.getReturnDate(), returnDate);
    }

    @Test
    public void getUserBorrowedFailsIfUserNotExist() {
        // Setup
        when(borrowService.getUserBorrowed(1L)).thenThrow(new IllegalArgumentException("User with id 1 does not exist."));

        // Act
        ResponseEntity<List<BorrowDTO>> result = borrowController.getUserBorrowed(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(result.getBody(), null);
    }

    @Test
    public void getBookBorrowedHistory() {
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        Borrow borrow = new Borrow(1L, book, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        when(borrowService.getBookBorrowedHistory(1L)).thenReturn(List.of(borrow));

        // Act
        ResponseEntity<List<Borrow>> result = borrowController.getBookBorrowedHistory(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(borrow), result.getBody());
    }

    @Test
    public void getBookBorrowedFailsIfBookNotExist() {
        // Setup
        when(borrowService.getBookBorrowedHistory(1L)).thenThrow(new IllegalArgumentException("Book with id 1 does not exist."));

        // Act
        ResponseEntity<List<Borrow>> result = borrowController.getBookBorrowedHistory(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(result.getBody(), null);
    }

    @Test
    public void getAllBorrowed() {
        // Setup
        Borrow borrow1 = new Borrow();
        Borrow borrow2 = new Borrow();
        List<Borrow> borrowList = List.of(borrow1, borrow2);
        when(borrowService.getAllBorrowed()).thenReturn(borrowList);

        // Act
        ResponseEntity<List<Borrow>> result = borrowController.getAllBorrowed();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(borrowList, result.getBody());
    }

    @Test
    public void borrowBook() {
        // Act
        ResponseEntity<String> result = borrowController.borrowBook(1L, 1L, LocalDate.now(), 7);

        // Assert
        verify(borrowService).borrowBook(1L, 1L, LocalDate.now(), 7);
        assertEquals(HttpStatus.CREATED, result.getStatusCode() );
        assertEquals("User 1 has borrowed book 1.", result.getBody());
    }

    @Test
    public void borrowBookFailsIfBookAlreadyBorrowed() {
        // Setup
        doThrow(new IllegalArgumentException("This book is already being borrowed.")).when(borrowService).borrowBook(1L, 1L, LocalDate.now(), 7);

        // Act
        ResponseEntity<String> result = borrowController.borrowBook(1L, 1L, LocalDate.now(), 7);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("This book is already being borrowed.", result.getBody());
    }

    @Test
    public void borrowBookFailsIfUserNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("User with id 1 does not exist.")).when(borrowService).borrowBook(1L, 1L, LocalDate.now(), 7);

        // Act
        ResponseEntity<String> result = borrowController.borrowBook(1L, 1L, LocalDate.now(), 7);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("User with id 1 does not exist.", result.getBody());
    }

    @Test
    public void borrowBookFailsIfBookNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Book with id 1 not found.")).when(borrowService).borrowBook(1L, 1L, LocalDate.now(), 7);

        // Act
        ResponseEntity<String> result = borrowController.borrowBook(1L, 1L, LocalDate.now(), 7);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode() );
        assertEquals("Book with id 1 not found.", result.getBody());
    }

    @Test
    public void deleteBorrow() {
        // Act
        ResponseEntity<String> result = borrowController.deleteBorrow(1L);

        // Assert
        verify(borrowService).deleteBorrow(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Borrow Entry successfully deleted.", result.getBody());
    }

    @Test
    public void deleteBorrowFailsIfNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Invalid borrow ID.")).when(borrowService).deleteBorrow(1L);

        // Act
        ResponseEntity<String> result = borrowController.deleteBorrow(1L);

        // Assert
        verify(borrowService).deleteBorrow(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid borrow ID.", result.getBody());
    }

    @Test
    public void returnBook() {
        // Act
        ResponseEntity<String> result = borrowController.returnBook(1L, LocalDate.now());

        // Assert
        verify(borrowService).returnBook(1L, LocalDate.now());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Book has been successfully returned.", result.getBody());
    }

    @Test
    public void returnBookFailsIfBookNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("Invalid borrow ID.")).when(borrowService).returnBook(1L, LocalDate.now());

        // Act
        ResponseEntity<String> result = borrowController.returnBook(1L, LocalDate.now());

        // Assert
        verify(borrowService).returnBook(1L, LocalDate.now());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid borrow ID.", result.getBody());
    }

}