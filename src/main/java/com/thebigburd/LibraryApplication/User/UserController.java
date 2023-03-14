package com.thebigburd.LibraryApplication.User;

import org.springframework.beans.factory.annotation.Autowired;

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

    @GetMapping(path = "userlist")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(path = "registerUser")
    public void registerUser(@RequestBody User newUser){
        userService.saveUser(newUser);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable ("userId") Long id){
        userService.deleteUser(id);
    }
}
