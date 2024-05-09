package com.thebigburd.LibraryApplication.Service;

import java.time.LocalDate;
import java.util.List;

import com.thebigburd.LibraryApplication.Controller.Request.UserRequest;
import com.thebigburd.LibraryApplication.Model.User;

public interface UserService {
	User getUser(Long id);
	List<User> getUserlist();
	User addUser(User newUser);
	Boolean deleteUser(Long id);
	User updateUser(Long id, UserRequest userRequest);
}
