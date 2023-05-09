package com.thebigburd.LibraryApplication.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BorrowDTO {

    private final long id;

    private final Book book;

    private final LocalDate borrowDate;

    private final LocalDate returnDate;

    public BorrowDTO(@JsonProperty("id") long id, @JsonProperty("book") Book book, @JsonProperty("borrow_date") LocalDate borrowDate, @JsonProperty("return_date") LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }


    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

}
