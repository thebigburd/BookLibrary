package com.thebigburd.LibraryApplication.Controller;


import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import com.thebigburd.LibraryApplication.Service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    // Gets a User's list of borrowed books.
    @GetMapping(path = "users/{userId}/borrowlist")
    public ResponseEntity<List<BorrowDTO>> getUserBorrowed(@PathVariable("userId") long userId){
        try {
            List<BorrowDTO> borrowList = borrowService.getUserBorrowed(userId);
            return ResponseEntity.ok(borrowList);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping(path = "book/history/{bookId}")
    public ResponseEntity<List<Borrow>> getBookBorrowedHistory(@PathVariable("bookId") long bookId){
        try {
            List<Borrow> bookHistory = borrowService.getBookBorrowedHistory(bookId);
            return ResponseEntity.ok(bookHistory);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping (path="borrow/list")
    public ResponseEntity<List<Borrow>> getAllBorrowed(){
        List<Borrow> borrowList = borrowService.getAllBorrowed();
        return ResponseEntity.ok(borrowList);
    }

    @PostMapping(path = "borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable("bookId") @RequestParam long bookId,
                           @RequestParam long userId,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate borrowDate,
                           @RequestParam int duration){
        if(borrowDate == null){
            borrowDate = LocalDate.now();  // Use Current Date if not specified.
        }
        try {
            borrowService.borrowBook(bookId, userId, borrowDate, duration);
            return ResponseEntity.status(HttpStatus.CREATED).body("User " +userId +" has borrowed book " +bookId + ".");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((e.getMessage()));
        }
    }

    @DeleteMapping(path = "borrow/delete/{borrowId}")
    public ResponseEntity<String> deleteBorrow(@PathVariable("borrowId") long borrowId){
        try {
            borrowService.deleteBorrow(borrowId);
            return ResponseEntity.ok("Borrow Entry successfully deleted.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(path = "borrow/return/{borrowId}")
    public ResponseEntity<String> returnBook(@PathVariable("borrowId") long borrowId,
                             @RequestParam(required = false) LocalDate returnDate){
        if(returnDate == null){
            returnDate = LocalDate.now();  // Use Current Date if not specified.
        }
        try {
            borrowService.returnBook(borrowId, returnDate);
            return ResponseEntity.ok("Book has been successfully returned.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
