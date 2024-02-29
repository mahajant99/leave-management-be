package com.technogise.leavemanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.HalfDay;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import com.technogise.leavemanagement.repositories.UserRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private static final String FULL_DAY = "fullday";
    private static final double FULL_DURATION = 1;
    private static final double HALF_DURATION = 0.5;

    public User getUserById(Long id) {
        Optional<User> retrievedUser = userRepository.findById(id);
        if (retrievedUser.isPresent()) {
            user = retrievedUser.get();
        }
        return user;
    }

    public Leave createOneDayLeave(LeaveDTO leaveDTO) {

        double duration;
        HalfDay halfDay = null;

        user = getUserById(leaveDTO.getUserId());

        if (leaveDTO.getLeaveType().equals(FULL_DAY)) {
            duration = FULL_DURATION;
            return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), halfDay, user);
        } else {
            duration = HALF_DURATION;
        }

        if (duration == HALF_DURATION) {
            halfDay = HalfDay.valueOf(leaveDTO.getLeaveType());
        }

        return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), halfDay,
                user);
    }

    public List<Leave> addLeaves(LeaveDTO leaveDTO) {

        List<Leave> addedLeaves = new ArrayList<>();

        if (leaveDTO.getStartDate().equals(leaveDTO.getEndDate())) {
            Leave leave = createOneDayLeave(leaveDTO);
            leaveRepository.save(leave);
            addedLeaves.add(leave);
        }

        return addedLeaves;

    }

}
