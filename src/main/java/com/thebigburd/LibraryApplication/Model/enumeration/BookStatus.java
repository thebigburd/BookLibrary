package com.thebigburd.LibraryApplication.Model.enumeration;

public enum BookStatus {
	AVAILABLE("AVAILABLE"),
	UNAVAILABLE("UNAVAILABLE");
	private final String status;

	BookStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}


}
