package com.thebigburd.LibraryApplication.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="borrow")
public class Borrow {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate borrowDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

	private boolean returned;

	private BorrowStatus borrowStatus;

    public Borrow(){
    }

	public Borrow(Book book, User user, LocalDate borrowDate, LocalDate returnDate, boolean returned, BorrowStatus borrowStatus) {
		this.book = book;
		this.user = user;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.returned = returned;
		this.borrowStatus = borrowStatus;
	}

	public Borrow(Long id, Book book, User user, LocalDate borrowDate, LocalDate returnDate, boolean returned, BorrowStatus borrowStatus) {
		this.id = id;
		this.book = book;
		this.user = user;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.returned = returned;
		this.borrowStatus = borrowStatus;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public BorrowStatus getBorrowStatus() {
		return borrowStatus;
	}

	public void setBorrowStatus(BorrowStatus borrowStatus) {
		this.borrowStatus = borrowStatus;
	}

	@Override
	public String toString() {
		return "Borrow{" +
			"id=" + id +
			", book=" + book +
			", user=" + user +
			", borrowDate=" + borrowDate +
			", returnDate=" + returnDate +
			", returned=" + returned +
			", borrowStatus=" + borrowStatus +
			'}';
	}
}
