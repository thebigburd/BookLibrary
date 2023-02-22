package com.thebigburd.LibraryApplication.User;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class UserService {


    public List<User> getUsers(){
        return List.of(
                new User("bigburd@gmail.com", "Burd",
                        LocalDate.of(1999, Month.JANUARY, 17),
                        24)
        );
    }
}