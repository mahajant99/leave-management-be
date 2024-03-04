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
    private static final String FIRST_HALF = "firsthalf";
    private static final String SECOND_HALF = "secondhalf";
    private static final double FULL_DURATION = 1;
    private static final double HALF_DURATION = 0.5;

    public double getDuration(String leaveType) {

        if (leaveType.equalsIgnoreCase(FULL_DAY)) {
            return FULL_DURATION;
        }
        return HALF_DURATION;
    }

    public HalfDay mapLeaveType(String leaveType) {
        leaveType = leaveType.trim().toLowerCase().replaceAll("\\s+", "");

        switch (leaveType) {
            case FULL_DAY:
                return null;
            case FIRST_HALF:
                return HalfDay.FIRST_HALF;
            case SECOND_HALF:
                return HalfDay.SECOND_HALF;
            default:
                throw new IllegalArgumentException("Unrecognized leave type: " + leaveType);
        }
    }

    public Leave createOneDayLeave(LeaveDTO leaveDTO, User user) {
        return leaveRepository.save(new Leave(null, leaveDTO.getStartDate(), getDuration(leaveDTO.getLeaveType()),
                leaveDTO.getDescription(),
                mapLeaveType(leaveDTO.getLeaveType()),
                user));
    }

    public List<Leave> addLeaves(LeaveDTO leaveDTO) {
        List<Leave> addedLeaves = new ArrayList<>();
        Optional<User> currentUser = userRepository.findById(leaveDTO.getUserId());

        if (leaveDTO.getStartDate().equals(leaveDTO.getEndDate())) {
            Leave leave = createOneDayLeave(leaveDTO, currentUser.get());
            addedLeaves.add(leave);
        }
        return addedLeaves;
    }
}
