package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Controller.Request.ReturnRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.BorrowDTO;
import com.thebigburd.LibraryApplication.Mapper.BorrowMapper;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final BorrowMapper borrowMapper;

    @Autowired
    public BorrowServiceImpl(BorrowRepository borrowRepository, BookRepository bookRepository,
            UserRepository userRepository, BorrowMapper borrowMapper) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowMapper = borrowMapper;
    }

    public List<BorrowDTO> getUserHistory(Long userId) {
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        List<Borrow> userBorrowed = borrowRepository.findByUserId(userId);

        return userBorrowed.stream()
                .map(borrowMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Borrow> getBookHistory(Long bookId) {
        if (bookRepository.findBookById(bookId).isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
        }
        return borrowRepository.findByBookId(bookId);
    }

    public List<Borrow> getAllBorrowed() {
        return borrowRepository.findAll();
    }

    public Borrow borrowBook(Long bookId, BorrowRequest borrowRequest) {
        Book book = bookRepository.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found"));

        // Book is always Unavailable when Stock is 0. Book may not always be available when Stock is more than 0.
        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            throw new IllegalArgumentException("This book is currently unavailable.");
        }
        else {
            User user = userRepository.findById(borrowRequest.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User with id " + borrowRequest.getUserId() + " does not exist."));

            if (user.getBorrowCount() >= user.getBorrowLimit()) {
                throw new IllegalStateException("The User has reached their borrowing limit.");
            }
            else {
                Borrow borrow = new Borrow(book, user, borrowRequest.getBorrowDate(), borrowRequest.getBorrowDate().plusDays(borrowRequest.getDuration()), false, BorrowStatus.BORROWED);
                book.setCurrentStock(book.getCurrentStock() - 1);

                if (book.getCurrentStock() == 0) {
                  book.setStatus(BookStatus.UNAVAILABLE);
                }

                user.setBorrowCount(user.getBorrowCount() + 1);

                return borrowRepository.save(borrow);
            }
        }
    }

    public boolean deleteBorrow(Long borrowId) {
		Borrow borrow = borrowRepository.findById(borrowId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid Borrow ID"));
 			if(!borrow.isReturned()) {
				 throw new IllegalArgumentException("Book must be returned before the borrow can be deleted.");
			}
			 else {
				borrowRepository.deleteById(borrowId);
				return true;
			}
    }

    @Transactional
    public Borrow returnBook(Long borrowId, ReturnRequest returnRequest) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Borrow Id."));
		if (borrow.getUser().getId() == returnRequest.getUserId()) {
			if (!borrow.isReturned()) {
				borrow.setReturnDate(returnRequest.getReturnDate());
				borrow.getBook().setCurrentStock(borrow.getBook().getCurrentStock() + 1);
				borrow.getBook().setStatus(BookStatus.AVAILABLE);
				borrow.setReturned(true);
				borrow.setBorrowStatus(BorrowStatus.RETURNED);

				User user = borrow.getUser();
				user.setBorrowCount(user.getBorrowCount() - 1);

				return borrowRepository.save(borrow);
			}
			else {
				throw new IllegalArgumentException("This book has already been returned.");
			}
		}
		else {
			throw new IllegalArgumentException("This book is not borrowed by this user.");
		}

    }
}
