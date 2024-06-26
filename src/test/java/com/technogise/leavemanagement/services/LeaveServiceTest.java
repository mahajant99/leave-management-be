package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.technogise.leavemanagement.enums.LeaveType;
import com.technogise.leavemanagement.exceptions.CalendarConfigException;
import com.technogise.leavemanagement.exceptions.LeaveAlreadyExistsException;
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
import com.technogise.leavemanagement.dtos.TimesheetResponse;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.exceptions.LeaveNotFoundException;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import com.technogise.leavemanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    KimaiTimesheetService kimaiTimesheetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    GoogleCalendarService googleCalendarService;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    @DisplayName("Given a user ID and pagination/sorting parameters, when fetching leaves, then the expected page of leaves is returned")
    void shouldFetchLeavesByUserId() {

        Long userId = 1L;
        int page = 0;
        int size = 10;
        String[] roles = {"User"};
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        PageRequest pageable = PageRequest.of(page, size, sort);

        User user = new User(userId, "Summer", "summer@gmail", roles, "https://www.test.com/", null);

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
    @DisplayName("Given pagination/sorting parameters and leaves exist, when fetching leaves, then the expected page of leaves is returned")
    void shouldFetchAllLeaves() {

        Long userId = 1L;
        int page = 0;
        int size = 10;
        String[] roles = {"User","Admin"};
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        PageRequest pageable = PageRequest.of(page, size, sort);

        User user = new User(userId, "Summer", "summer@gmail", roles, "https://www.test.com/", null);

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

        when(leaveRepository.findByDeletedFalseOrderByDateDesc(pageable)).thenReturn(expectedPage);

        Page<Leave> resultPage = leaveService.getAllLeaves(page, size);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    @DisplayName("Given a user and a leave exists, when you softdelete a leave, then deleted should be set to true.")
    public void shouldDeleteLeaveById() {

        Long leaveId = 1L;
        String[] userRole = {"user"};
        User user = new User(001l, "Test User", "testuser@gmail.com", userRole, "https://www.test.com/", null);

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
    public void Should_HaveHalfDayNull_When_LeaveTypeIsFullDay() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setHalfDay(null);
        newLeave.setDuration(1);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);

        assertNull(createdLeave.getHalfDay());
        assertEquals(newLeave.getDuration(), createdLeave.getDuration());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_HaveHalfDayAsFirstHalf_When_LeaveTypeIsFirstHalf() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FIRSTHALF))
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setHalfDay(HalfDay.FIRSTHALF);
        newLeave.setDuration(0.5);
        newLeave.setUser(user);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
        assertEquals(newLeave.getHalfDay(), createdLeave.getHalfDay());
        assertEquals(newLeave.getUser().getId(), createdLeave.getUser().getId());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_HaveHalfDaySecondHalf_When_LeaveTypeIsSecondHalf() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.SECONDHALF))
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setHalfDay(HalfDay.SECONDHALF);
        newLeave.setDuration(0.5);

        lenient().when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());
        assertEquals(newLeave.getHalfDay(), createdLeave.getHalfDay());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_HaveDateSetCorrectly_When_CreatingLeave() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setDate(LocalDate.of(2022, 3, 5));

        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);

        assertEquals(leaveDTO.getStartDate(), createdLeave.getDate());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_HaveDurationSetToOne_When_LeaveTypeIsFullDay() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setDuration(1);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_HaveDurationSetToZeroPointFive_When_LeaveTypeIsHalfDay() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FIRSTHALF))
                .startDate(LocalDate.of(2022, 3, 5))
                .endDate(LocalDate.of(2022, 3, 5))
                .build();

        Leave newLeave = new Leave();
        newLeave.setId(1L);
        newLeave.setDuration(0.5);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(leaveRepository.save(any(Leave.class))).thenReturn(newLeave);
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);
        Leave createdLeave = createdLeaves.get(0);  

        assertEquals(newLeave.getDuration(), createdLeave.getDuration());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_MapHalfDayAsNull_When_LeaveTypeIsFullDay() {
        String leaveType = String.valueOf(LeaveType.FULLDAY);

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertNull(halfDay);
    }

    @Test
    public void Should_MapHalfDayAsFirstHalf_When_LeaveTypeIsFirstHalf() {
        String leaveType = String.valueOf(LeaveType.FIRSTHALF);

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertEquals(HalfDay.FIRSTHALF, halfDay);
    }

    @Test
    public void Should_MapHalfDayAsSecondHalf_When_LeaveTypeIsSecondHalf() {
        String leaveType = String.valueOf(LeaveType.SECONDHALF);

        HalfDay halfDay = leaveService.mapLeaveType(leaveType);

        assertEquals(HalfDay.SECONDHALF, halfDay);
    }

    @Test
    public void Should_ReturnOne_WhenLeaveTypeIsFullDay() {
        String leaveType = String.valueOf(LeaveType.FULLDAY);

        double duration = leaveService.getDuration(leaveType);

        assertEquals(1.0, duration);
    }

    @Test
    public void Should_AddASingleLeave_When_StartAndEndDateIsSame() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 04, 01))
                .endDate(LocalDate.of(2024, 04, 01))
                .description("Vacation")
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Leave savedLeave = new Leave();
        savedLeave.setId(1L);
        savedLeave.setUser(user);
        savedLeave.setDate(leaveDTO.getStartDate());
        savedLeave.setDescription(leaveDTO.getDescription());
        savedLeave.setHalfDay(null);
        savedLeave.setDuration(1.0);

        when(leaveRepository.save(any(Leave.class))).thenReturn(savedLeave);
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);

        Leave createdLeave = createdLeaves.get(0);

        assertEquals(LocalDate.of(2024, 04, 01), createdLeave.getDate());
        assertEquals(1, createdLeave.getDuration());
        assertNull(createdLeave.getHalfDay());
        assertEquals(leaveDTO.getDescription(), createdLeave.getDescription());
        assertEquals(1, createdLeave.getDuration());
        assertEquals(user, createdLeave.getUser());

        verify(kimaiTimesheetService).createTimesheet(any(Leave.class));
    }

    @Test
    public void Should_AddMultipleLeaves_When_StartAndEndDateIsDifferent() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 16))
                .endDate(LocalDate.of(2024, 3, 17))
                .description("Vacation")
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        Leave savedLeave1 = new Leave();
        savedLeave1.setId(1L);
        savedLeave1.setUser(user);
        savedLeave1.setDate(leaveDTO.getStartDate());
        savedLeave1.setDescription(leaveDTO.getDescription());
        savedLeave1.setHalfDay(null);
        savedLeave1.setDuration(1.0);

        Leave savedLeave2 = new Leave();
        savedLeave2.setId(2L);
        savedLeave2.setUser(user);
        savedLeave2.setDate(leaveDTO.getEndDate());
        savedLeave2.setDescription(leaveDTO.getDescription());
        savedLeave2.setHalfDay(null);
        savedLeave2.setDuration(1.0);

        when(leaveRepository.save(any(Leave.class))).thenReturn(savedLeave2).thenReturn(savedLeave1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));
        TimesheetResponse mockResponse = new TimesheetResponse(); 
        when(kimaiTimesheetService.createTimesheet(any(Leave.class))).thenReturn(mockResponse);
        
        List<Leave> createdLeaves = leaveService.addLeaves(leaveDTO);

        Leave createdLeave1 = createdLeaves.get(0);
        Leave createdLeave2 = createdLeaves.get(1);

        assertEquals(LocalDate.of(2024, 03, 16), createdLeave1.getDate());
        assertEquals(1, createdLeave1.getDuration());
        assertEquals(leaveDTO.getDescription(), createdLeave1.getDescription());
        assertNull(createdLeave1.getHalfDay());
        assertEquals(user, createdLeave1.getUser());

        assertEquals(LocalDate.of(2024, 03, 17), createdLeave2.getDate());
        assertEquals(1, createdLeave2.getDuration());
        assertEquals(leaveDTO.getDescription(), createdLeave2.getDescription());
        assertNull(createdLeave2.getHalfDay());
        assertEquals(user, createdLeave2.getUser());
    }    

    @Test
    public void Should_ReturnEmptyList_When_StartDateIsAfterEndDate() throws Exception {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 17))
                .endDate(LocalDate.of(2024, 3, 16))
                .description("Vacation")
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        List<Leave> createdLeaves = leaveService.createLeaves(leaveDTO, user);

        assertEquals(0, createdLeaves.size());
    }
   
    @Test
    public void Should_ThrowLeaveAlreadyExists_When_ADuplicateLeaveIsAdded() throws CalendarConfigException {
        User user = new User();
        user.setId(1L);

        LeaveDTO leaveDTO = LeaveDTO.builder()
                .startDate(LocalDate.of(2024, 3, 17))
                .endDate(LocalDate.of(2024, 3, 17))
                .description("Vacation")
                .userId(1L)
                .leaveType(String.valueOf(LeaveType.FULLDAY))
                .build();

        when(userRepository.findById(leaveDTO.getUserId())).thenReturn(Optional.of(user));
        when(leaveRepository.existsByUserIdAndDateAndDeletedFalse(user.getId(), leaveDTO.getStartDate())).thenReturn(true);
        lenient().doNothing().when(googleCalendarService).addLeave(any(Leave.class));

        assertThrows(LeaveAlreadyExistsException.class, () -> leaveService.addLeaves(leaveDTO));
    }
}