package com.technogise.leavemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.technogise.leavemanagement.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
