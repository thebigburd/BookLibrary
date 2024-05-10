package com.thebigburd.LibraryApplication.Service;

import java.util.List;

import com.thebigburd.LibraryApplication.Controller.Request.BookRequest;
import com.thebigburd.LibraryApplication.Model.Book;

public interface BookService {
	Book getBook(Long id);
	List<Book> getBooklist();
	Book addBook(Book book);
	Boolean deleteBook(Long id);
	Book updateBook(Long id, BookRequest bookRequest);


}
