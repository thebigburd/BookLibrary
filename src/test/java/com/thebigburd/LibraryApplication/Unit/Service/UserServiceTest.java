package com.thebigburd.LibraryApplication.Unit.Service;

import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import com.thebigburd.LibraryApplication.Service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getUserReturnsUserIfExists() {
        // Setup
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUser(1L);

        // Assert
        assertEquals(user, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserThrowsExceptionInvalidId() {
        // Setup
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act
        userService.getUser(1L);

        // Assert exception thrown
    }

    @Test
    public void getUserlistReturnsAllUsers() {
        // Setup

        List<User> userList = Arrays.asList(
				new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
				UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 ),
				new User(2L, "jane.doe@example.com", "Jane", "Doe", "password", "1 Street", "07111 111111",
				UserRole.ROLE_USER, LocalDate.of(1995, 1, 1), 0, 3 )
        );
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getUserlist();

        // Assert
        assertEquals(userList, result);
    }

    @Test
    public void addUserToRepository() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        userService.addUser(user);

        // Assert
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalStateException.class)
    public void addUserWithExistingEmailThrowsException() {
        // Setup
        User user = new User(null, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        userService.addUser(user);

        // Assert exception thrown
    }

    @Test
    public void deleteUser() {

        // Setup
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void deleteNonExistingUserThrowsException() {
        // Setup
        when(userRepository.existsById(1L)).thenReturn(false);


        // Act
        userService.deleteUser(1L);

        // Assert exception thrown
    }

    @Test
    public void updateUserSavesToRepository() {

        // Setup
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("jane.doe@example.com");
		userRequest.setName("Jane");
		userRequest.setDateOfBirth(LocalDate.of(2000,1,1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail("jane.doe@example.com")).thenReturn(Optional.empty());

        // Act
        userService.updateUser(1L, userRequest);

        // Assert
        verify(userRepository).save(user);
        assertEquals("Jane", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), user.getDateOfBirth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserWithUsedEmail() {
        // Setup
		User user = new User(1L, "john.doe@example.com", "John", "Doe", "password", "1 Street", "07123 456789",
			UserRole.ROLE_ADMIN, LocalDate.of(1990, 1, 1), 0, 3 );
		User user2 = new User(2L, "jane.doe@example.com", "Jane", "Doe", "password", "1 Street", "07111 111111",
			UserRole.ROLE_USER, LocalDate.of(1995, 1, 1), 0, 3 );
        UserRequest userRequest = new UserRequest();
		userRequest.setEmail("john.doe@example.com");
		when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        // Act
        userService.updateUser(2L, userRequest);

        // Assert
    }
}
