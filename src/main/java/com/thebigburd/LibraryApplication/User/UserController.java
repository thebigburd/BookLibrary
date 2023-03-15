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

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable ("userId") Long id){
        return userService.getUser(id);
    }

    @GetMapping(path = "userlist")
    public List<User> getUserlist() {
        return userService.getUserlist();
    }

    @PostMapping(path = "registerUser")
    public void registerUser(@RequestBody User newUser){
        userService.saveUser(newUser);
    }

    @DeleteMapping(path = "delete/{userId}")
    public void deleteUser(@PathVariable ("userId") Long id){
        userService.deleteUser(id);
    }

    @PutMapping(path = "update/{userId}")
    public void updateUser(@PathVariable ("userId") Long id,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String email){
        userService.updateUser(id, name, email);
    }

}
