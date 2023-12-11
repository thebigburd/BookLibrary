package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.UserController;
import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUser() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userService.getUser(1L)).thenReturn(user);

        // Act
        ResponseEntity<User> result = userController.getUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    public void getUserFailsIfUserNotExist() {
        // Setup
        when(userService.getUser(2L)).thenThrow(new IllegalArgumentException("User does not exist with the id 2."));

        // Act
        ResponseEntity<User> result = userController.getUser(2L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(null, result.getBody());
    }


    @Test
    public void getUserList() {
        // Setup
        List<User> userList = Arrays.asList(
                new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1)),
                new User(2L, "jane.doe@example.com", "Jane", "Doe", LocalDate.of(1990, 1, 1))
        );
        when(userService.getUserlist()).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> result = userController.getUserlist();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userList, result.getBody());
    }


    @Test
    public void addUser() {
        // Setup
        User newUser = new User("john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));

        // Act
        ResponseEntity<String> result = userController.addUser(newUser);

        // Assert
        verify(userService).addUser(newUser);
        assertEquals(HttpStatus.CREATED,result.getStatusCode() );
        assertEquals("User added successfully.", result.getBody());
    }

    @Test
    public void addUserFailsIfExists() {
        // Setup
        User newUser = new User("john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        doThrow(new IllegalArgumentException("Email already in use.")).when(userService).addUser(newUser);

        // Act
        ResponseEntity<String> result = userController.addUser(newUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email already in use.", result.getBody());
    }


    @Test
    public void deleteUser() {
        // Setup
        doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<String> resultFound = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultFound.getStatusCode());
        assertEquals(resultFound.getBody(), "User deleted successfully.");


    }

    @Test
    public void deleteUserFailsIfUserNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("User with id 2 does not exist.")).when(userService).deleteUser(2L);

        // Act
        ResponseEntity<String> resultNotFound = userController.deleteUser(2L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultNotFound.getStatusCode());
        assertEquals("User with id 2 does not exist.", resultNotFound.getBody());
    }

    @Test
    public void updateUser() {
        // Setup
        doNothing().when(userService).updateUser(1L, "John", "Souls", null);

        // Act
        ResponseEntity<String> resultFound = userController.updateUser(1L, "John", "Souls", null);

        // Assert
        assertEquals(HttpStatus.OK, resultFound.getStatusCode());
        assertEquals("User updated successfully", resultFound.getBody());
    }

    @Test
    public void updateUserFailsIfUserNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("User does not exist with the id 2.")).when(userService)
                .updateUser(2L, "Jane", "Doe", "jane.doe@example.com");

        // Act
        ResponseEntity<String> result = userController.updateUser(2L, "Jane", "Doe", "jane.doe@example.com");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User does not exist with the id 2.", result.getBody());
    }

    @Test
    public void updateUserFailsIfSameEmail() {
        // Setup
        doThrow(new IllegalArgumentException("Email is already in use.")).when(userService)
                .updateUser(2L, "Jane", "Doe", "jane.doe@example.com");

        // Act
        ResponseEntity<String> result = userController.updateUser(2L, "Jane", "Doe", "jane.doe@example.com");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email is already in use.", result.getBody());
    }
}
