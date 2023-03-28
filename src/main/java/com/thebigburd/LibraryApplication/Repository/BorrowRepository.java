package com.thebigburd.LibraryApplication.Repository;

import com.thebigburd.LibraryApplication.Book.Book;
import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {


    Optional<Borrow> findBorrowByBook(Book book);

    List<Borrow> findByUserId(long userId);

    Optional<Borrow> findByBookIdAndUserId(long bookId, long userId);


}
