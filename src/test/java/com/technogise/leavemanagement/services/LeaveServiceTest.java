package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.time.LocalDate;

import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;
import com.technogise.leavemanagement.repositories.LeaveRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    @DisplayName("Given a user ID and pagination/sorting parameters, when fetching leaves, then the expected page of leaves is returned")
    void shouldFetchLeavesByUserId() {

        Long userId = 1L;
        int page = 0;
        int size = 10;
        String[] roles = { "User" };
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        PageRequest pageable = PageRequest.of(page, size, sort);

        User user = new User(userId, "Summer", "summer@gmail", roles, null);

        Leave leave1 = new Leave();
        leave1.setId(1L);
        leave1.setUser(user);
        leave1.setDate(LocalDate.now());

        Leave leave2 = new Leave();
        leave2.setId(2L);
        leave2.setUser(user);
        leave2.setDate(LocalDate.now().minusDays(1));

        List<Leave> leaves = Arrays.asList(leave1, leave2);
        Page<Leave> expectedPage = new PageImpl<>(leaves, pageable, leaves.size());

        when(leaveRepository.findByUserIdAndDeletedFalseOrderByDateDesc(userId, pageable)).thenReturn(expectedPage);

        Page<Leave> resultPage = leaveService.getLeavesByUserId(userId, page, size);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    @DisplayName("Given a user and a leave exists, when you softdelete a leave, then deleted should be set to true.")
    public void shouldDeleteLeaveById() {

        Long leaveId = 1L;
        String[] userRole = { "user" };
        User user = new User(001l, "Test User", "testuser@gmail.com", userRole, null);

        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setDeleted(false);
        leave.setDate(LocalDate.now());
        leave.setDuration(1);
        leave.setDescription("Sick Leave");
        leave.setHalfDay(null);
        leave.setUser(user);

        when(leaveRepository.findById(leaveId)).thenReturn(java.util.Optional.of(leave));

        leaveService.deleteLeave(leaveId);

        Optional<Leave> response = leaveRepository.findById(leaveId);
        assertTrue(response.get().isDeleted());
    }

    @Test
    @DisplayName("Given a leave does not exists, when you softdelete that leave, then throw leave not found exception.")
    public void deleteShouldThrowLeaveNotFoundException() {

        Long nonExistentId = 1L;
        when(leaveRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(LeaveNotFoundException.class, () -> leaveService.deleteLeave(nonExistentId));
    }
    
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
        newLeave.setHalfDay(HalfDay.FIRSTHALF);
        newLeave.setDuration(0.5);
        newLeave.setUser(user);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);

        Leave createdLeave = leaveService.createOneDayLeave(leaveDTO, user);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
        assertEquals(newLeave.getHalfDay(), createdLeave.getHalfDay());
        assertEquals(newLeave.getUser().getId(), createdLeave.getUser().getId());
    }

    @Test
    public void Should_HaveHalfDaySecondHalf_When_LeaveTypeIsSecondHalf() {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = new LeaveDTO();
        leaveDTO.setUserId(1L);
        leaveDTO.setLeaveType("second half");

        Leave newLeave = new Leave();
        newLeave.setHalfDay(HalfDay.SECONDHALF);
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

        assertEquals(HalfDay.FIRSTHALF, halfDay);
    }

    @Test
    public void Should_MapHalfDayAsSecondHalf_When_LeaveTypeIsSecondHalf() {
        String leaveType = "second half";

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertEquals(HalfDay.SECONDHALF, halfDay);
    }

    @Test
    public void Should_ReturnOne_WhenLeaveTypeIsFullDay() {
        String leaveType = "full day";

        double duration = leaveService.getDuration(leaveType);

        assertEquals(1.0, duration);
    }
}
