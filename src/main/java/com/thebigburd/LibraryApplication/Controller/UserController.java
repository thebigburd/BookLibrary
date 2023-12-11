package com.thebigburd.LibraryApplication.Controller;

import com.thebigburd.LibraryApplication.Entity.User;
import com.thebigburd.LibraryApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "app/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<User> getUser(@PathVariable ("userId") Long id){
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(user);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path = "userlist")
    public ResponseEntity<List<User>> getUserlist() {
        List<User> userList = userService.getUserlist();
        return ResponseEntity.ok(userList);
    }

    @PostMapping(path = "registerUser")
    public ResponseEntity<String> addUser(@RequestBody User newUser){
        try {
            userService.addUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable ("userId") Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(path = "update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable ("userId") Long id,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String surname,
                           @RequestParam(required = false) String email){
        try {
            userService.updateUser(id, name, surname, email);
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
