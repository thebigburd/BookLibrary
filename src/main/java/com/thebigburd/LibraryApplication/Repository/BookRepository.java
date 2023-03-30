package com.thebigburd.LibraryApplication.Repository;

import com.thebigburd.LibraryApplication.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByName(String name);
}
