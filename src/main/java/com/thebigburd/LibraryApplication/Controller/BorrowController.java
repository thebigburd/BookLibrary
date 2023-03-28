package com.thebigburd.LibraryApplication.Controller;


import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Service.BorrowService;
import com.thebigburd.LibraryApplication.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "app/")
public class BorrowController {

    BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @GetMapping(path = "book/status/{bookId}")
    public Borrow getBookStatus(@PathVariable("bookId") long bookId){
        return borrowService.getBookStatus(bookId);
    }


    // Gets a User's list of borrowed books.
    @GetMapping(path = "users/{userId}/borrowlist")
    public List<Borrow> getUserBorrowed(@PathVariable("userId") long userId){
        return borrowService.getUserBorrowed(userId);

    }

    @GetMapping (path="borrow/list")
    public List<Borrow> getAllBorrowed(){
        return borrowService.getAllBorrowed();
    }

    @PostMapping(path = "borrow/{bookId}")
    public void borrowBook(@PathVariable("bookId") @RequestParam long bookId,
                           @RequestParam long userId,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowDate,
                           @RequestParam int duration){
        if(borrowDate == null){
            borrowDate = LocalDate.now();  // Use Current Date if not specified.
        }
        borrowService.borrowBook(bookId, userId, borrowDate, duration);
    }

    @DeleteMapping(path = "borrow/delete/{borrowId}")
    public void deleteBorrowEntry(@PathVariable("borrowId") long borrowId){
        borrowService.deleteBorrowEntry(borrowId);
    }

    @PutMapping(path = "borrow/return/{borrowId}")
    public void returnBook(@PathVariable("borrowId") long borrowId,
                             @RequestParam(required = false) LocalDate returnDate){
        if(returnDate == null){
            returnDate = LocalDate.now();  // Use Current Date if not specified.
        }
        borrowService.returnBook(borrowId, returnDate);
    }
}
