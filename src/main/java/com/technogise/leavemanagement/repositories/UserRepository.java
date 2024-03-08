package com.technogise.leavemanagement.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.technogise.leavemanagement.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
    
}
