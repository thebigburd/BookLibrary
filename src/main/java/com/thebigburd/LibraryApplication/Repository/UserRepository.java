package com.thebigburd.LibraryApplication.Repository;

import com.thebigburd.LibraryApplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByName(String name);

	Optional<User> findUserById(Long userId);
}
