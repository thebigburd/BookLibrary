package com.thebigburd.LibraryApplication.Controller.Request;

import java.time.LocalDate;

public class BorrowRequest {

	private Long userId;
	private LocalDate borrowDate;
	private int duration;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
