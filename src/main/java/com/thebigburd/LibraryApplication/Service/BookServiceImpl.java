package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Book getBook(Long id) {
		Book book = bookRepository.findBookById(id)
			.orElseThrow(() -> new IllegalArgumentException("Book with the id " + id + " does not exist."));
		return book;
	}

	public List<Book> getBooklist() {
		return bookRepository.findAll();
	}

	public Book addBook(Book book) {
		Optional<Book> bookOptional = bookRepository.findBookByName(book.getName());
		if (bookOptional.isPresent()) {
			Book selectedBook = bookOptional.get();
			selectedBook.setCurrentStock(selectedBook.getCurrentStock() + 1);
			selectedBook.setTotalStock(selectedBook.getTotalStock() + 1);
			return bookRepository.save(selectedBook);
		}
		else {
			return bookRepository.save(book);
		}
	}

	public Boolean deleteBook(Long id) {
		if (!bookRepository.existsById(id)) {
			throw new IllegalArgumentException("Book with the id " + id + " does not exist.");
		}
		else {
			bookRepository.deleteById(id);
			return true;
		}
	}

	@Transactional
	public Book updateBook(Long id, BookRequest bookRequest) {
		Book book = bookRepository.findBookById(id)
			.orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " does not exist."));

		// Handle Optional Variables and Business Logic Validation

		if (bookRequest.getName() != null &&
			!bookRequest.getName().isEmpty()) {
			book.setName(bookRequest.getName());
		}

		if (bookRequest.getDescription() != null &&
			!bookRequest.getDescription().isEmpty()) {
			book.setDescription(bookRequest.getDescription());
		}

		if (bookRequest.getPublishYear() != null &&
			bookRequest.getPublishYear() > 0) {
			book.setPublishYear(bookRequest.getPublishYear());
		}

		// Ensure if both currentStock and totalStock are provided, currentStock <= totalStock
		if (bookRequest.getCurrentStock() != null && bookRequest.getTotalStock() != null) {
			if (bookRequest.getCurrentStock() > bookRequest.getTotalStock()) {
				throw new IllegalArgumentException("Current Stock must be less than or equal to Total Stock");
			}
			book.setCurrentStock(bookRequest.getCurrentStock());
			book.setTotalStock(bookRequest.getTotalStock());
		}
		// If only updated currentStock in request, it is less than or equal to the set totalStock
		else if (bookRequest.getCurrentStock() != null) {
			if (bookRequest.getCurrentStock() > book.getTotalStock()) {
				throw new IllegalArgumentException("Current Stock must be less than or equal to Total Stock");
			}
			book.setCurrentStock(bookRequest.getCurrentStock());
		}
		// If only updated totalStock in request, it is more than or equal to the set currentStock
		else if (bookRequest.getTotalStock() != null) {
			if (bookRequest.getTotalStock() < book.getCurrentStock()) {
				throw new IllegalArgumentException("Total Stock must be more than or equal to Current Stock");
			}
			book.setTotalStock(bookRequest.getTotalStock());
		}

		if (bookRequest.getStatus() != null &&
			bookRequest.getStatus() != book.getStatus()) {
			book.setStatus(bookRequest.getStatus());
		}

		return bookRepository.save(book);
	}
}
