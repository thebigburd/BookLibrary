package com.thebigburd.LibraryApplication.Controller.Request;

import java.time.LocalDate;

public class ReturnRequest {

	// To verify ownership of borrowed book.
	private long userId;

	private LocalDate returnDate;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}
