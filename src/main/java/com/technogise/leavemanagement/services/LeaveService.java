package com.technogise.leavemanagement.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    public Leave createOneDayLeave(LeaveDTO leaveDTO) {

        double duration;
        HalfDay halfDay;

        if (leaveDTO.getLeaveType() == "fullday") {
            duration = 1;
            return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), null, user);

        } else {
            duration = 0.5;
        }

        if (leaveDTO.getLeaveType().equals("first half")) {
            halfDay = HalfDay.FIRST_HALF;
        } else {
            halfDay = HalfDay.SECOND_HALF;
        }

        return new Leave(null, leaveDTO.getStartDate(), duration, leaveDTO.getDescription(), halfDay,
                user);
    }

}
