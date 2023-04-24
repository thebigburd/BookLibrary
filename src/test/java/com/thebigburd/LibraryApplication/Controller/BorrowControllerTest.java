package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Service.BorrowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BorrowControllerTest {

    @Mock
    private BorrowService borrowService;

    private BorrowMapper borrowMapper = Mappers.getMapper(BorrowMapper.class);

    @InjectMocks
    private BorrowController borrowController;


    @Test
    public void getUserBorrowedReturnsBorrowDTO() {
        // Setup
        Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        Book book2 = new Book(2L, "Book 2", "Description of book 2", 2022, true);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        Borrow borrow1 = new Borrow(1L, book1, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        Borrow borrow2 = new Borrow(2L, book2, user, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1));
        when(borrowService.getUserBorrowed(1L)).thenReturn(List.of(borrow1,borrow2));
        BorrowDTO borrow1DTO = new BorrowDTO(1L, book1, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        BorrowDTO borrow2DTO = new BorrowDTO(2L, book2, LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1));
        List<BorrowDTO> expected = List.of(borrow1DTO,borrow2DTO);

        // Act
        List<BorrowDTO> result = borrowController.getUserBorrowed(1L);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void getBookBorrowedHistory() {
        // Setup
        Book book1 = new Book(1L, "Book 1", "Description of book 1", 2021, true);
        User user = new User(1L, "JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        Borrow borrow1 = new Borrow(1L, book1, user, LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 15));
        when(borrowService.getBookBorrowedHistory(1L)).thenReturn(List.of(borrow1));

        // Act
        List<Borrow> result = borrowController.getBookBorrowedHistory(1L);

        // Assert
        assertEquals(List.of(borrow1), result);
    }

    @Test
    public void getAllBorrowed() {
        // Setup
        Borrow borrow1 = new Borrow();
        Borrow borrow2 = new Borrow();
        List<Borrow> expected = List.of(borrow1, borrow2);
        when(borrowService.getAllBorrowed()).thenReturn(expected);

        // Act
        List<Borrow> result = borrowController.getAllBorrowed();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void borrowBook() {
        // Act
        borrowController.borrowBook(1L, 1L, LocalDate.now(), 7);

        // Assert
        verify(borrowService).borrowBook(1L, 1L, LocalDate.now(), 7);
    }

    @Test
    public void deleteBorrow() {
        // Act
        borrowController.deleteBorrow(1L);

        // Assert
        verify(borrowService).deleteBorrow(1L);
    }

    @Test
    public void returnBook() {
        // Act
        borrowController.returnBook(1L, LocalDate.now());

        // Assert
        verify(borrowService).returnBook(1L, LocalDate.now());
    }

}