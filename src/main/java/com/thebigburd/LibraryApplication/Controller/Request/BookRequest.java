package com.thebigburd.LibraryApplication.Controller.Request;

import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;

public class BookRequest {

	private String name;
	private String description;
	private Integer publishYear;
	private Integer currentStock;
	private Integer totalStock;
	private BookStatus status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(Integer publishYear) {
		this.publishYear = publishYear;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Integer getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(Integer totalStock) {
		this.totalStock = totalStock;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}
}
