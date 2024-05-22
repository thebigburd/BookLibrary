package com.thebigburd.LibraryApplication.Model;



import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private int publishYear;
	private int currentStock;
	private int totalStock;
	private BookStatus status;

	public Book() {
	}

	public Book(String name, String description, int publishYear, int currentStock, int totalStock, BookStatus status) {
		this.name = name;
		this.description = description;
		this.publishYear = publishYear;
		this.currentStock = currentStock;
		this.totalStock = totalStock;
		this.status = status;
	}

	public Book(Long id, String name, String description, int publishYear, int currentStock, int totalStock,
				BookStatus status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.publishYear = publishYear;
		this.currentStock = currentStock;
		this.totalStock = totalStock;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public int getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}


	public int getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Book {id=" + id +
			", name=" + name +
			", description=" + description +
			", publishYear=" + publishYear +
			", currentStock=" + currentStock +
			", totalStock=" + totalStock +
			", status=" + status +
			"}";
	}
}
