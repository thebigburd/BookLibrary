package com.thebigburd.LibraryApplication.Model.enumeration;

public enum BorrowStatus {
	BORROWED("BORROWED"),
	RETURNED("RETURNED"),
	OVERDUE("OVERDUE");

	private final String status;

	BorrowStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

}
