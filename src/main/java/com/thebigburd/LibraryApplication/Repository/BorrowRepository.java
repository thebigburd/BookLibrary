package com.thebigburd.LibraryApplication.Repository;

import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {


    Optional<Borrow> findByBook(Book book);

    List<Borrow> findByUserId(long userId);

    List<Borrow> findByBookId(long bookId);

    Optional<Borrow> findByBookIdAndUserId(long bookId, long userId);


}
