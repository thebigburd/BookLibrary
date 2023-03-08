package com.thebigburd.LibraryApplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void saveUser(User newUser) {
        Optional<User> userOptional = userRepository.findUserByEmail(newUser.getEmail());
        if(userOptional.isPresent()){
            throw new IllegalStateException("Email already in use.");
        }
        else{
            userRepository.save(newUser);
        }
    }

}