package com.thebigburd.LibraryApplication.Controller;


import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Service.BorrowService;
import com.thebigburd.LibraryApplication.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "app/borrow")
public class BorrowController {

    BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @GetMapping(path = "status/{bookId}")
    public Borrow getBookStatus(@PathVariable("bookId") long bookId){
        return borrowService.getBookStatus(bookId);
    }


    // Gets a User's list of borrowed books.
    @GetMapping(path = "user/{userId}")
    public List<Borrow> getUserBorrowed(@PathVariable("userId") long userId){
        return borrowService.getUserBorrowed(userId);
    }

    @GetMapping (path="list")
    public List<Borrow> getAllBorrowed(){
        return borrowService.getAllBorrowed();
    }

    @PostMapping(path = "{bookId}")
    public void borrowBook(@PathVariable("bookId") @RequestParam long bookId,
                           @RequestParam long userId,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowDate,
                           @RequestParam int duration){
        if(borrowDate == null){
            borrowDate = LocalDate.now();  // Use Current Date if not specified.
        }
        borrowService.borrowBook(bookId, userId, borrowDate, duration);
    }

    @DeleteMapping(path = "return/{bookId}")
    public void returnBook(@PathVariable("bookId") long bookId, long userId){
        borrowService.returnBook(bookId, userId);
    }

    @PutMapping(path = "update/{borrowId}")
    public void updateBorrow(@PathVariable("borrowId") long borrowId,
                             @RequestParam(required = false) LocalDate returnDate){
        borrowService.updateBorrow(borrowId, returnDate);
    }
}
