package com.thebigburd.LibraryApplication.Service;


import com.thebigburd.LibraryApplication.Entity.Book;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    BorrowRepository borrowRepository;
    BookRepository bookRepository;
    UserRepository userRepository;

    BorrowMapper borrowMapper;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository, BorrowMapper borrowMapper){
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowMapper = borrowMapper;
    }


    public List<BorrowDTO> getUserBorrowed(long userId){
        if(userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        List<Borrow> userBorrowed = borrowRepository.findByUserId(userId);

        return userBorrowed.stream()
                .map(borrowMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Borrow> getBookBorrowedHistory(long bookId){
        if(bookRepository.findById(bookId).isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
        }
        List<Borrow> bookBorrowHistory = borrowRepository.findByBookId(bookId);
        return bookBorrowHistory;
    }

    public List<Borrow> getAllBorrowed() {
        return borrowRepository.findAll();
    }

    public void borrowBook(long bookId, long userId, LocalDate borrowDate, int duration) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            if(book.isBorrowed()){
                throw new IllegalArgumentException("This book is already being borrowed");
            }
            else{
                Optional<User> userOptional = userRepository.findById(userId);
                if(userOptional.isPresent()){
                    User user = userOptional.get();
                    // If limiting amount of books borrowed add another if condition and add "borrowed" field to User entity.
                    Borrow borrow = new Borrow(book, user, borrowDate, borrowDate.plusDays(duration));
                    book.setBorrowed(true);
                    borrowRepository.save(borrow);
                }
                else{
                    throw new IllegalArgumentException("User with id " +userId + " does not exist.");
                }
            }
        }
        else{
            throw new IllegalArgumentException("Book with id " +bookId + " not found");
        }
    }

    public void deleteBorrow(long borrowId) {
        if(borrowRepository.existsById(borrowId)){
            borrowRepository.deleteById(borrowId);
            System.out.println("Borrow Entry " +borrowId + " has been successfully deleted.");
        }
        else{
            throw new IllegalArgumentException("Invalid borrow ID.");
        }
    }

    @Transactional
    public void returnBook(long borrowId, LocalDate returnDate) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid borrow ID."));
        borrow.setReturnDate(returnDate);
        borrow.getBook().setBorrowed(false);
        borrowRepository.save(borrow);
        System.out.println("The book with id " +borrow.getBook().getId() +" has been successfully returned by user " +borrow.getUser().getId() +".");
    }
}
