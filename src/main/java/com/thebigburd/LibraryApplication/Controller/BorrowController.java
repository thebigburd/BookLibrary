package com.thebigburd.LibraryApplication.Controller;


import com.thebigburd.LibraryApplication.Service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "app/borrow")
public class BorrowController {

    BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
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
}
