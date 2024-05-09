package com.thebigburd.LibraryApplication.Service;

import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.User;
import com.thebigburd.LibraryApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
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

    public User addUser(User newUser) {
        Optional<User> userOptional = userRepository.findUserByEmail(newUser.getEmail());
        if(userOptional.isPresent()){
            throw new IllegalStateException("Email already in use.");
        }
        else{
			newUser.setAge();
            return userRepository.save(newUser);
        }
    }

    public Boolean deleteUser(Long id) {
        // Possibly make it so that to delete a User, their books must be returned first?
        if(!userRepository.existsById(id)){
            throw new IllegalStateException("User with ID " +id + " does not exist.");
        }
        else{
            userRepository.deleteById(id);
            return true;
        }

    }

    @Transactional
    public User updateUser(Long id, UserRequest userRequest) {
        User person = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User does not exist with the id " +id + "."));

		// Handle Optional Variables and Business Logic Validation

        if(userRequest.getName() != null &&
                !userRequest.getName().isEmpty()){
                person.setName(userRequest.getName());
        }

        if(userRequest.getSurname() != null &&
                !userRequest.getSurname().isEmpty()){
                person.setSurname(userRequest.getSurname());
        }

        if(userRequest.getEmail() != null &&
                !userRequest.getEmail().isEmpty() &&
                !Objects.equals(person.getEmail(), userRequest.getEmail())){	// Prevent exception throw if same email is used as param.
            Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
            if(userOptional.isPresent()){
                throw new IllegalArgumentException("Email is already in use.");
            }
            else{
                person.setEmail(userRequest.getEmail());
            }
        }

		if(userRequest.getPassword() != null
			&& !userRequest.getPassword().isEmpty()) {
			person.setPassword(userRequest.getPassword());
		}

		if(userRequest.getAddress() != null
			&& !userRequest.getAddress().isEmpty()) {
			person.setAddress(userRequest.getAddress());
		}

		if(userRequest.getPhone() != null
			&& !userRequest.getPhone().isEmpty()) {
			person.setPhone(userRequest.getPhone());
		}

        if(userRequest.getDateOfBirth() != null) {
            person.setDateOfBirth(userRequest.getDateOfBirth());
            person.setAge();
        }

        return userRepository.save(person);
    }
}
