package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.repositories.LeaveRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    public void Should_HaveHalfDayNull_When_LeaveTypeIsFullDay() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("full day");

        Leave newLeave = new Leave();
        newLeave.setHalfDay(null);
        newLeave.setDuration(1);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertNull(createdLeave.getHalfDay());
    }

    @Test
    public void Should_HaveHalfDayFirstHalf_When_LeaveTypeIsFirstHalf() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("first half");

        Leave leave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(HalfDay.FIRST_HALF, leave.getHalfDay());
    }

    @Test
    public void Should_HaveHalfDaySecondHalf_When_LeaveTypeIsSecondHalf() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("second half");

        Leave leave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(HalfDay.SECOND_HALF, leave.getHalfDay());
    }

}
