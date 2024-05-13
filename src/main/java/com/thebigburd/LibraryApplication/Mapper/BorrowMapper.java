package com.thebigburd.LibraryApplication.Mapper;

import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.BorrowDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper {
    BorrowDTO toDTO(Borrow borrow);
}
