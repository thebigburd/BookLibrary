package com.thebigburd.LibraryApplication.Service;

import java.time.LocalDate;
import java.util.List;

import com.thebigburd.LibraryApplication.Controller.Request.BorrowRequest;
import com.thebigburd.LibraryApplication.Controller.Request.ReturnRequest;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.BorrowDTO;

public interface BorrowService {
	List<BorrowDTO> getUserHistory(Long id);
	List<Borrow> getBookHistory(Long bookId);
	List<Borrow> getAllBorrowed();
	Borrow borrowBook(Long bookId, BorrowRequest borrowRequest);
	boolean deleteBorrow(Long bookId);
	Borrow returnBook(Long borrowId, ReturnRequest returnRequest);

}
