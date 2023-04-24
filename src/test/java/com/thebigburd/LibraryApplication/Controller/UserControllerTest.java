package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUser() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userService.getUser(1L)).thenReturn(user);

        // Act
        User result = userController.getUser(1L);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testGetUserlist() {
        // Setup
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1)));
        userList.add(new User(2L, "jane.doe@example.com", "Jane", "Doe", LocalDate.of(1990, 1, 1)));
        when(userService.getUserlist()).thenReturn(userList);

        // Act
        List<User> result = userController.getUserlist();

        // Assert
        assertEquals(userList, result);
    }

    @Test
    public void testRegisterUser() {
        // Setup
        User newUser = new User("john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));

        // Act
        userController.registerUser(newUser);

        // Assert
        verify(userService).saveUser(newUser);
    }

    @Test
    public void testDeleteUser() {
        // Act
        userController.deleteUser(1L);

        // Assert
        verify(userService).deleteUser(1L);
    }

    @Test
    public void testUpdateUser() {
        // Act
        userController.updateUser(1L, "John", "Doe", "johndoe@gmail.com");

        // Assert
        verify(userService).updateUser(1L, "John", "Doe", "johndoe@gmail.com");
    }
}
