package com.thebigburd.LibraryApplication.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;

import java.time.LocalDate;

public record BorrowDTO(Long id, Book book, LocalDate borrowDate, LocalDate returnDate, boolean returned, BorrowStatus borrowStatus) {

	public BorrowDTO(@JsonProperty("id") Long id, @JsonProperty("book") Book book, @JsonProperty("borrow_date") LocalDate borrowDate, @JsonProperty("return_date") LocalDate returnDate, @JsonProperty("returned") boolean returned, @JsonProperty("borrow_status") BorrowStatus borrowStatus) {
		this.id = id;
		this.book = book;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.returned = returned;
		this.borrowStatus = borrowStatus;
	}

}
