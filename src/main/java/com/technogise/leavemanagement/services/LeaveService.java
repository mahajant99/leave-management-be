package com.technogise.leavemanagement.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.technogise.leavemanagement.exceptions.LeaveAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;
import com.technogise.leavemanagement.exceptions.UserNotFoundException;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.repositories.UserRepository;

@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String FULLDAY = "FULLDAY";
    private static final String FIRSTHALF = "FIRSTHALF";
    private static final String SECONDHALF = "SECONDHALF";
    private static final double FULLDURATION = 1;
    private static final double HALFDURATION = 0.5;

    public Page<Leave> getLeavesByUserId(Long userId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(page, size, sort);
        return leaveRepository.findByUserIdAndDeletedFalseOrderByDateDesc(userId, pageable);
    }

    public void deleteLeave(Long id) throws LeaveNotFoundException {
        Optional<Leave> leave = leaveRepository.findById(id);
        if (!leave.isPresent()) {
            throw new LeaveNotFoundException(id);
        }
        leave.get().setDeleted(true);
        leaveRepository.save(leave.get());
    }
   
    public double getDuration(String leaveType) {
        leaveType = leaveType.trim().toUpperCase().replaceAll("\\s+", "");
        return leaveType.equals(FULLDAY) ? FULLDURATION : HALFDURATION;
    }

    public HalfDay mapLeaveType(String leaveType) {
        leaveType = leaveType.trim().toUpperCase().replaceAll("\\s+", "");
        return switch (leaveType) {
            case FULLDAY -> null;
            case FIRSTHALF -> HalfDay.FIRSTHALF;
            case SECONDHALF -> HalfDay.SECONDHALF;
            default -> throw new IllegalArgumentException("Unrecognized leave type: " + leaveType);
        };
    }


    public List<Leave> createLeaves(LeaveDTO leaveDTO, User currentUser) {
        LocalDate currentDate = leaveDTO.getStartDate();
        List<Leave> multipleLeaves = new ArrayList<>();

        while (!currentDate.isAfter(leaveDTO.getEndDate())) {
            Optional<Leave> existingLeave = leaveRepository.findByUserIdAndDate(currentUser.getId(), currentDate);

            if (existingLeave.isEmpty()) {
                Leave leave = new Leave();

                leave.setDate(currentDate);
                leave.setDuration(getDuration(leaveDTO.getLeaveType()));
                leave.setDescription(leaveDTO.getDescription());
                leave.setHalfDay(mapLeaveType(leaveDTO.getLeaveType()));
                leave.setUser(currentUser);

                multipleLeaves.add(leaveRepository.save(leave));
            }
            currentDate = currentDate.plusDays(1);
        }
        return multipleLeaves;
    }

    public List<Leave> addLeaves(LeaveDTO leaveDTO) throws Exception {
        Optional<User> currentUserOptional = userRepository.findById(leaveDTO.getUserId());

        if (currentUserOptional.isEmpty())
            throw new UserNotFoundException(leaveDTO.getUserId());

        User currentUser = currentUserOptional.get();

        List<Leave> createdLeaves = createLeaves(leaveDTO, currentUser);

        if(createdLeaves.isEmpty()) {
            throw new LeaveAlreadyExistsException();
        }
        return createdLeaves;
    }
}
