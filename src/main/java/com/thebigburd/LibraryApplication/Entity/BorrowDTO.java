package com.thebigburd.LibraryApplication.Entity;

import com.thebigburd.LibraryApplication.Book.Book;

import java.time.LocalDate;

public class BorrowDTO {

    private long id;

    private Book book;

    private LocalDate borrowDate;

    private LocalDate returnDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
