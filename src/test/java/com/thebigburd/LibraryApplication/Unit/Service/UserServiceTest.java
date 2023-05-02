package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
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
    private UserService userService;

    @Test
    public void testGetUserReturnsUserWhenExists() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUser(1L);

        // Assert
        assertEquals(user, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserThrowsExceptionInvalidId() {
        // Setup
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act
        userService.getUser(1L);

        // Assert exception thrown
    }

    @Test
    public void testGetUserList() {
        // Setup

        List<User> userList = Arrays.asList(
                new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1)),
                new User(2L, "jane.doe@example.com", "Jane", "Doe", LocalDate.of(1995, 1, 1))
        );
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getUserlist();

        // Assert
        assertEquals(userList, result);
    }

    @Test
    public void testSaveUser() {
        // Setup
        User user = new User(null, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        userService.saveUser(user);

        // Assert
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalStateException.class)
    public void testSaveUserWithExistingEmail() {
        // Setup
        User user = new User(null, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        userService.saveUser(user);

        // Assert exception thrown
    }

    @Test
    public void testDeleteUser() {

        // Setup
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteNonExistingUser() {
        // Setup
        when(userRepository.existsById(1L)).thenReturn(false);


        // Act
        userService.deleteUser(1L);

        // Assert exception thrown
    }

    @Test
    public void testUpdateUser() {

        // SetUp
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail("jane.doe@example.com")).thenReturn(Optional.empty());

        // Act
        userService.updateUser(1L, "Jane", "Doe", "jane.doe@example.com");

        // Assert
        verify(userRepository).save(user);
        assertEquals("Jane", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("jane.doe@example.com", user.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserWithUsedEmail() {
        // Setup
        User user = new User(1L, "john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1));
        User user2 = new User(2L, "jane.doe@example.com", "Jane", "Doe", LocalDate.of(1990, 1, 1));
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        // Act
        userService.updateUser(2l,"Jane", "Doe", "john.doe@example.com");

        // Assert
    }
}