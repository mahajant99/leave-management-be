package com.technogise.leavemanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import com.technogise.leavemanagement.repositories.UserRepository;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String FULL_DAY = "fullday";
    private static final double FULL_DURATION = 1;
    private static final double HALF_DURATION = 0.5;


    public double getDuration(String leaveType) {
        double duration;

        if (leaveType.equals(FULL_DAY)) {
            duration = FULL_DURATION;
        } else {
            duration = HALF_DURATION;
        }
        return duration;
    }

    public HalfDay mapLeaveType(String leaveType) {
        leaveType = leaveType.toLowerCase();

        switch (leaveType) {
            case "first half":
            case "firsthalf":
                return HalfDay.FIRST_HALF;
            case "second half":
            case "secondhalf":
                return HalfDay.SECOND_HALF;
            default:
                throw new IllegalArgumentException("Unrecognized leave type: " + leaveType);
        }
    }

    public Leave createOneDayLeave(LeaveDTO leaveDTO, User user) {
        HalfDay halfDay = null;
        double duration;

        duration = getDuration(leaveDTO.getLeaveType().toString());

        if (duration == HALF_DURATION) {
            halfDay = mapLeaveType(leaveDTO.getLeaveType());
            return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), halfDay,
                    user);
        }

        return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), halfDay,
                user);
    }

    public List<Leave> addLeaves(LeaveDTO leaveDTO) {
        List<Leave> addedLeaves = new ArrayList<>();
        Optional<User> currentUser = userRepository.findById(leaveDTO.getUserId());

        if (leaveDTO.getStartDate().equals(leaveDTO.getEndDate())) {
            Leave leave = createOneDayLeave(leaveDTO, currentUser.get());
            leaveRepository.save(leave);
            addedLeaves.add(leave);
        }
        return addedLeaves;
    }
}
