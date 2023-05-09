package com.thebigburd.LibraryApplication.Unit.Service;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.BorrowService;
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
    private BorrowService borrowService;


    @Test
    public void getUserBorrowedReturnsBorrowDTOs() {
        // Setup
        Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        Book book2 = new Book(2L, "Book 2", "Description of book 2", 2022, true);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        Borrow borrow1 = new Borrow(1L, book1, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        Borrow borrow2 = new Borrow(2L, book2, user, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1));
        BorrowDTO borrow1DTO = new BorrowDTO(1L, book1, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        BorrowDTO borrow2DTO = new BorrowDTO(2L, book2, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowRepository.findByUserId(1L)).thenReturn(List.of(borrow1, borrow2));
        when(borrowMapper.toDTO(borrow1)).thenReturn(borrow1DTO);
        when(borrowMapper.toDTO(borrow2)).thenReturn(borrow2DTO);

        // Act
        List<BorrowDTO> result = borrowService.getUserBorrowed(1L);

        // Assert
        assertEquals(List.of(borrow1DTO,borrow2DTO), result);
    }

    @Test
    public void getUserBorrowedReturnsEmptyListIfNoBorrows(){
        // Setup
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowRepository.findByUserId(1L)).thenReturn(List.of());

        // Act
        List<BorrowDTO> result = borrowService.getUserBorrowed(1L);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserBorrowedThrowsExceptionWhenInvalidId(){
        // Setup
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        borrowService.getUserBorrowed(1L);

        // Assert exception is thrown
    }


    @Test
    public void getBookBorrowedHistoryReturnsBorrows() {
        // Setup
        Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        Borrow borrow1 = new Borrow(1L, book1, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(borrowRepository.findByBookId(1L)).thenReturn(List.of(borrow1));

        // Act
        List<Borrow> result = borrowService.getBookBorrowedHistory(1L);

        // Assert
        assertEquals(List.of(borrow1), result);
    }

    @Test
    public void getBookBorrowedHistoryReturnsEmptyListIfNoBorrows() {
        // Setup
        Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(borrowRepository.findByBookId(1L)).thenReturn(List.of());

        // Act
        List<Borrow> result = borrowService.getBookBorrowedHistory(1L);

        // Assert
        assertEquals(List.of(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBookBorrowedHistoryThrowsExceptionIfInvalidId() {
        // Setup
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        borrowService.getBookBorrowedHistory(1L);

        // Assert exception is thrown
    }


    @Test
    public void testGetAllBorrowed() {
        // Setup
        Borrow borrow1 = new Borrow();
        borrow1.setId(1L);
        Borrow borrow2 = new Borrow();
        borrow2.setId(2L);
        List<Borrow> expectedBorrows = Arrays.asList(borrow1,borrow2);
        when(borrowRepository.findAll()).thenReturn(expectedBorrows);

        // Act
        List<Borrow> actualBorrows = borrowService.getAllBorrowed();

        // Assert
        assertEquals(expectedBorrows, actualBorrows);
    }


    @Test
    public void borrowBookSuccessfullyBorrows(){
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, false);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        borrowService.borrowBook(1L,1L, LocalDate.now(), 7);

        // Assert
        assertTrue(book.isBorrowed());
        verify(borrowRepository, times(1)).save(any(Borrow.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void borrowBookThrowsExceptionIfBookNotFound(){
        // Setup
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        borrowService.borrowBook(1L,1L, LocalDate.now(), 7);

        // Assert exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void borrowBookThrowsExceptionIfUserNotFound(){
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        borrowService.borrowBook(1L,1L, LocalDate.now(), 7);

        // Assert exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void borrowBookThrowsExceptionIfBookUnavailable(){
        // Setup
        Book book = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        borrowService.borrowBook(1L,1L, LocalDate.now(), 7);

        // Assert exception is thrown
    }

    @Test
    public void deleteBorrow() {
        // Setup
        when(borrowRepository.existsById(1L)).thenReturn(true);

        // Act
        borrowService.deleteBorrow(1L);

        // Assert
        verify(borrowRepository).deleteById(1L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteBorrowThrowsExceptionIfBorrowNotExist() {
        // Setup
        when(borrowRepository.existsById(1L)).thenReturn(false);

        // Act
        borrowService.deleteBorrow(1L);

        // Assert exception is thrown
    }
}
