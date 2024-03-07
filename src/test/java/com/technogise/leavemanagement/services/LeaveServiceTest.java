package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.technogise.leavemanagement.repositories.UserRepository;
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
import org.springframework.boot.test.context.SpringBootTest;

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
        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
    }

    @Test
    public void Should_HaveHalfDayAsFirstHalf_When_LeaveTypeIsFirstHalf() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("first half");

        Leave newLeave = new Leave();
        newLeave.setHalfDay(HalfDay.FIRST_HALF);
        newLeave.setDuration(0.5);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
        assertEquals(newLeave.getHalfDay(), createdLeave.getHalfDay());
    }

    @Test
    public void Should_HaveHalfDaySecondHalf_When_LeaveTypeIsSecondHalf() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("second half");

        Leave newLeave = new Leave();
        newLeave.setHalfDay(HalfDay.SECOND_HALF);
        newLeave.setDuration(0.5);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
        assertEquals(newLeave.getHalfDay(), createdLeave.getHalfDay());
    }

    @Test
    public void Should_HaveDateSetCorrectly_When_CreatingLeave() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("full day");
        leaveDTO.setStartDate(LocalDate.of(2022, 3, 5));

        Leave newLeave = new Leave();
        newLeave.setDate(LocalDate.of(2022, 3, 5));

        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(leaveDTO.getStartDate(), createdLeave.getDate());
    }

    @Test
    public void Should_HaveDurationSetToOne_When_LeaveTypeIsFullDay() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("full day");

        Leave newLeave = new Leave();
        newLeave.setDuration(1);

        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());

    }

    @Test
    public void Should_HaveDurationSetToZeroPointFive_When_LeaveTypeIsHalfDay() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("full day");

        Leave newLeave = new Leave();
        newLeave.setDuration(0.5);

        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());

    }

    @Test
    public void Should_MapHalfDayAsNull_When_LeaveTypeIsFullDay() {
        String leaveType = "full day";

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertNull(halfDay);
    }

    @Test
    public void Should_MapHalfDayAsFirstHalf_When_LeaveTypeIsFirstHalf() {
        String leaveType = "first half";

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertEquals(HalfDay.FIRST_HALF, halfDay);
    }

    @Test
    public void Should_MapHalfDayAsSecondHalf_When_LeaveTypeIsSecondHalf() {
        String leaveType = "second half";

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertEquals(HalfDay.SECOND_HALF, halfDay);
    }

    @Test
    public void Should_ReturnOne_WhenLeaveTypeIsFullDay() {
        String leaveType = "full day";

        double duration = leaveService.getDuration(leaveType);

        assertEquals(1.0, duration);
    }

}
