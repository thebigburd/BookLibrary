package com.thebigburd.LibraryApplication.Mapper;

import com.thebigburd.LibraryApplication.Entity.Borrow;
import com.thebigburd.LibraryApplication.Entity.BorrowDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper {
    BorrowDTO toDTO(Borrow borrow);
}
