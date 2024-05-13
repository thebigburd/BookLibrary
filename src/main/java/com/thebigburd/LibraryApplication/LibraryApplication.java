package com.thebigburd.LibraryApplication;

import com.thebigburd.LibraryApplication.Model.Book;
import com.thebigburd.LibraryApplication.Model.Borrow;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.BookStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.BorrowStatus;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;


@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	CommandLineRunner run(BookRepository bookRepository, UserRepository userRepository, BorrowRepository borrowRepository) {
			return args -> {
				bookRepository.save(new Book(null, "First Book", "A blank description", 2000, 1, 1, BookStatus.AVAILABLE ));
				bookRepository.save(new Book(null, "Second Book", "A blank description", 2001, 1, 2, BookStatus.AVAILABLE ));
				Book unavailable = new Book(null, "Unavailable Book", "A blank description", 2005, 0, 1, BookStatus.UNAVAILABLE);
				bookRepository.save(unavailable);
				bookRepository.save(new Book(null, "Unavailable Stocked Book", "A blank description", 2003, 3, 3, BookStatus.UNAVAILABLE ));

				User normal = new User(null, "NormalUser@address.co.uk", "Normal", "User", "password", "1 Road", "+44 1111 111111", UserRole.ROLE_USER, LocalDate.of(2000, 1, 1), 1, 3);
				userRepository.save(new User(null, "AdminUser@address.co.uk", "Admin", "User", "password", "1 Street", "07123 456789", UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3));
				userRepository.save(normal);

				borrowRepository.save(new Borrow(null, unavailable, normal, LocalDate.of(2024,1,1), LocalDate.of(2024,1,8), false, BorrowStatus.OVERDUE));
			};
	}


}
