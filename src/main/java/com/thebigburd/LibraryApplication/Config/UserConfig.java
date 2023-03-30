package com.thebigburd.LibraryApplication.Config;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


//@Configuration
//public class UserConfig {
//
//    // Two Database entries for request testing purposes.
//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepository) {
//        return args -> {
//            User burd = new User("bigburd@gmail.com", "Big", "Burd",
//                    LocalDate.of(1999, Month.JANUARY, 17)
//            );
//
//            User doe = new User("johndoe@example.com", "John", "Doe",
//                    LocalDate.of(1994, Month.APRIL, 04)
//            );
//
//            userRepository.saveAll(
//                    List.of(burd, doe)
//            );
//        };
//
//    }
//}
