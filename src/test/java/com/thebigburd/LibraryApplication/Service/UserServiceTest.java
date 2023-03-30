package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.BookRepository;
import com.thebigburd.LibraryApplication.Repository.BorrowRepository;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        user = new User("JohnDoe@example.com", "John", "Doe", LocalDate.of(2000, 01, 01));
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg()); // Return the same User object with ID assigned
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsById(1L)).thenReturn(true); // Simulate a user with ID 1 existing in repository.

        userRepository.save(user);
    }

    @Test
    void testGetUser() {
        User returnedUser = userService.getUser(1L);

        assertNotNull(returnedUser);
        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getName(), returnedUser.getName());
    }

    @Test
    void getUserlist() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}