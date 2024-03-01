package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    public void Should_HaveHalfDayNull_When_LeaveTypeIsFullDay() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("fullday");

        Leave leave = leaveService.createOneDayLeave(leaveDTO, user);

        assertNull(leave.getHalfDay());        
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

}
