package com.technogise.leavemanagement.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.technogise.leavemanagement.entities.HalfDay;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;

@SpringBootTest
public class LeaveRepositoryTest {

    @Autowired
    private LeaveRepository leaveRepository;

    @Test
    @DisplayName("Given a user exists and a its leave exists, when I softdelete by leave id, the deleted should be set to true")
    void softDeleteById_setsDeletedTrue() {

        String[] userRole = {"user"};

        User user = new User(11l, "Alok6","alo11k@gmail",userRole, null);         
        
        Leave leave = new Leave();
        leave.setDate(LocalDate.now());
        leave.setDuration(0.5);
        leave.setDescription("Sick Leave");
        leave.setHalfDay(HalfDay.FIRST_HALF);
        leave.setUser(user);
        leaveRepository.save(leave);
        leaveRepository.flush();
        
        leaveRepository.softDeleteById(leave.getId());
        
        Optional<Leave> result = leaveRepository.findById(leave.getId());
        assertTrue(result.get().isDeleted());              
    }
}
