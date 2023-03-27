package com.thebigburd.LibraryApplication.Service;


import com.thebigburd.LibraryApplication.Book.Book;
import com.thebigburd.LibraryApplication.Book.BookRepository;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.User.User;
import com.thebigburd.LibraryApplication.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {

    BorrowRepository borrowRepository;
    BookRepository bookRepository;
    UserRepository userRepository;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository){
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Borrow getBookStatus(long bookId) {
        if(!bookRepository.existsById(bookId)){
            throw new IllegalArgumentException("Book with id " +bookId + " does not exist.");
        }
        else{
            Book book = bookRepository.findById(bookId).get();
            Optional<Borrow> borrowOptional = borrowRepository.findBorrowByBook(book);
            if(borrowOptional.isPresent()){
                return borrowOptional.get();
            }
            else{
                throw new RuntimeException("This book is currently available.");
            }
        }
    }

    public List<Borrow> getUserBorrowed(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("This user does not exist."));
        List<Borrow> userBorrowed = borrowRepository.findByUser(user);
        return userBorrowed;
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

    public void returnBook(long bookId, long userId) {
        Optional<Borrow> borrowOptional = borrowRepository.findByBookIdAndUserId(bookId, userId);
        if(borrowOptional.isPresent()){
            Borrow borrowed = borrowOptional.get();
            Book returnedBook = borrowed.getBook();
            borrowRepository.delete(borrowed);
            returnedBook.setBorrowed(false);
            System.out.println("The book with id " +bookId +" has been successfully returned by user " +userId +".");
        }
        else{
            throw new IllegalStateException("Could not find a result with the given IDs.");
        }
    }

    @Transactional
    public void updateBorrow(long borrowId, LocalDate returnDate) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalStateException("Invalid borrow ID."));
        borrow.setReturnDate(returnDate);
        borrowRepository.save(borrow);
    }
}