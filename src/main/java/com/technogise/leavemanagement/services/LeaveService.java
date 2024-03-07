package com.technogise.leavemanagement.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    private static final String FULL_DAY = "FULLDAY";
    private static final String FIRST_HALF = "FIRSTHALF";
    private static final String SECOND_HALF = "SECONDHALF";
    private static final double FULL_DURATION = 1;
    private static final double HALF_DURATION = 0.5;

    public double getDuration(String leaveType) {
        leaveType = leaveType.trim().toUpperCase().replaceAll("\\s+", "");
        return leaveType.equals(FULL_DAY) ? FULL_DURATION : HALF_DURATION;
    }

    public HalfDay mapLeaveType(String leaveType) {
        leaveType = leaveType.trim().toUpperCase().replaceAll("\\s+", "");
        return switch (leaveType) {
            case FULL_DAY -> null;
            case FIRST_HALF -> HalfDay.FIRST_HALF;
            case SECOND_HALF -> HalfDay.SECOND_HALF;
            default -> throw new IllegalArgumentException("Unrecognized leave type: " + leaveType);
        };
    }

    public Leave createOneDayLeave(LeaveDTO leaveDTO, User user) {
        Leave leave = new Leave();
        leave.setDate(leaveDTO.getStartDate());
        leave.setDuration(getDuration(leaveDTO.getLeaveType()));
        leave.setDescription(leaveDTO.getDescription());
        leave.setHalfDay(mapLeaveType(leaveDTO.getLeaveType()));
        leave.setUser(user);

        return leaveRepository.save(leave);
    }

    public List<Leave> addLeaves(LeaveDTO leaveDTO) {
        List<Leave> addedLeaves = new ArrayList<>();
        Optional<User> currentUserOptional = userRepository.findById(leaveDTO.getUserId());

        if (!currentUserOptional.isPresent()) return Collections.emptyList();

        User currentUser = currentUserOptional.get();

        if (leaveDTO.getStartDate().equals(leaveDTO.getEndDate())) {
            Leave leave = createOneDayLeave(leaveDTO, currentUser);
            addedLeaves.add(leave);
        }
        return addedLeaves;
    }
}