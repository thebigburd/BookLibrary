package com.thebigburd.LibraryApplication.Unit.Controller;

import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Controller.UserController;
import com.thebigburd.LibraryApplication.Model.Response;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Service.UserServiceImpl;
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
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getUserReturnsUser() {
        // Setup
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
        when(userService.getUser(1L)).thenReturn(user);

        // Act
        ResponseEntity<Response> result = userController.getUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody().getData().get("user"));
    }

    @Test
    public void getUserFailsIfUserNotExist() {
        // Setup
        when(userService.getUser(2L)).thenThrow(new IllegalArgumentException("User does not exist with the id 2."));

        // Act
        ResponseEntity<Response> result = userController.getUser(2L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }


    @Test
    public void getUserlistReturnsListOfUsers() {
        // Setup
		List<User> userList = Arrays.asList(
			new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
				UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 ),
			new User(2L, "jane.doe@example.com", "Jane", "Doe", "password", "1 Street", "07111 111111",
				UserRole.ROLE_USER, LocalDate.of(1995, 1, 1), 0, 3 )
		);
        when(userService.getUserlist()).thenReturn(userList);

        // Act
        ResponseEntity<Response> result = userController.getUserlist();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userList, result.getBody().getData().get("users"));
    }


    @Test
    public void addUserSavesUserToRepository() {
        // Setup
        User user = new User(null, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
		when(userService.addUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<Response> result = userController.addUser(user);

        // Assert
        verify(userService).addUser(user);
        assertEquals(HttpStatus.CREATED,result.getStatusCode() );
        assertEquals("User successfully registered!", result.getBody().getMessage());
		assertEquals(user, result.getBody().getData().get("user"));
    }

    @Test
    public void addUserFailsIfAlreadyExists() {
        // Setup
        User newUser =	new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
        doThrow(new IllegalArgumentException("Email already in use.")).when(userService).addUser(newUser);

        // Act
        ResponseEntity<Response> result = userController.addUser(newUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email already in use.", result.getBody().getMessage());
    }


    @Test
    public void deleteUser() {
        // Setup
        when(userService.deleteUser(1L)).thenReturn(true);

        // Act
        ResponseEntity<Response> resultFound = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultFound.getStatusCode());
        assertEquals(resultFound.getBody().getMessage(), "User removed successfully.");


    }

    @Test
    public void deleteUserFailsIfUserNotExist() {
        // Setup
        doThrow(new IllegalArgumentException("User with id 2 does not exist.")).when(userService).deleteUser(2L);

        // Act
        ResponseEntity<Response> resultNotFound = userController.deleteUser(2L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultNotFound.getStatusCode());
        assertEquals("User with id 2 does not exist.", resultNotFound.getBody().getMessage());
    }

    @Test
    public void updateUser() {
		// Setup
		User user = new User(1L, "john.doe@example.com", "John", "Souls", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(2000, 1, 1), 0, 3 );
		UserRequest userRequest = new UserRequest();
		userRequest.setSurname("Souls");
		userRequest.setDateOfBirth(LocalDate.of(2000,1,1));
		when(userService.updateUser(anyLong(), any(UserRequest.class))).thenReturn(user);

        // Act
        ResponseEntity<Response> result = userController.updateUser(1L, userRequest);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User updated successfully.", result.getBody().getMessage());
    }

    @Test
    public void updateUserFailsIfUserNotExist() {
        // Setup
		UserRequest userRequest = new UserRequest();
        doThrow(new IllegalArgumentException("User does not exist with the id 2.")).when(userService)
                .updateUser(anyLong(), any(UserRequest.class));

        // Act
        ResponseEntity<Response> result = userController.updateUser(2L, userRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User does not exist with the id 2.", result.getBody().getMessage());
    }

    @Test
    public void updateUserFailsIfEmailInUse() {
        // Setup
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("jane.doe@example.com");
        doThrow(new IllegalArgumentException("Email is already in use.")).when(userService)
                .updateUser(2L, userRequest);

        // Act
        ResponseEntity<Response> result = userController.updateUser(2L, userRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email is already in use.", result.getBody().getMessage());
    }
}
