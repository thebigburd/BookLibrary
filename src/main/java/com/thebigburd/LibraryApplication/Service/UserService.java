package com.thebigburd.LibraryApplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public User getUser(Long id){
        if(userRepository.existsById(id)){
            return userRepository.findById(id).get();
        }
        else{
            throw new IllegalArgumentException("User does not exist with the id " +id + ".");
        }
    }

    public List<User> getUserlist(){
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

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new IllegalStateException("User with ID " +id + " does not exist.");
        }
        else{
            userRepository.deleteById(id);
        }

    }

    @Transactional
    public void updateUser(Long id,String name, String surname, String email) {
        User person = getUser(id);

        if(name != null &&
                name.length() > 0){
            if(Objects.equals(person.getName(), name)){
                   throw new IllegalArgumentException("This name is already linked with this user.");
            }
            else{
                person.setName(name);
            }
        }

        if(surname != null &&
                surname.length() > 0){
            if(Objects.equals(person.getSurname(), surname)){
                throw new IllegalArgumentException("This name is already linked with this user.");
            }
            else{
                person.setSurname(surname);
            }
        }

        if(email != null &&
                email.length() > 0 &&
                !Objects.equals(person.getEmail(), email)){
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if(userOptional.isPresent()){
                throw new IllegalArgumentException("Email is already in use.");
            }
            else{
                person.setEmail(email);
            }
        }


    }
}