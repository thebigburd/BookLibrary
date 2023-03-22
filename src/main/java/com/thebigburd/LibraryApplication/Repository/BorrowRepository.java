package com.thebigburd.LibraryApplication.Repository;

import com.thebigburd.LibraryApplication.Entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
}
