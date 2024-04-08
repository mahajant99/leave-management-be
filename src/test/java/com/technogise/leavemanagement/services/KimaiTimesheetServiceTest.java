package com.technogise.leavemanagement.services;

import com.technogise.leavemanagement.dtos.TimesheetRequest;
import com.technogise.leavemanagement.dtos.TimesheetResponse;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.enums.HalfDay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;


public class KimaiTimesheetServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KimaiTimesheetService kimaiTimesheetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given a full day leave, when creating a timesheet, then it should return a valid response")
    public void testCreateTimesheetForFullDayLeave() {
        TimesheetResponse mockResponse = new TimesheetResponse();
        mockResponse.setId(0);
        mockResponse.setDescription("Full day leave");
        mockResponse.setDuration(8);
        mockResponse.setExported(false);
        mockResponse.setBillable(false);
        mockResponse.setTags(Collections.singletonList("leave"));

        when(restTemplate.postForEntity(any(String.class), any(TimesheetRequest.class), eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Leave leave = new Leave();
        leave.setDate(LocalDate.parse("2024-08-08"));
        leave.setDuration(1.0);
        leave.setHalfDay(null);
        leave.setDescription("Full day leave");

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertNotNull(response);
        assertEquals(mockResponse.getId(), response.getId());
        assertEquals(mockResponse.getDescription(), response.getDescription());
        assertEquals(mockResponse.getDuration(), response.getDuration());
        assertEquals(mockResponse.isExported(), response.isExported());
        assertEquals(mockResponse.isBillable(), response.isBillable());
        assertEquals(mockResponse.getTags(), response.getTags());
    }

    @Test
    @DisplayName("Given a first half leave, when creating a timesheet, then it should return a valid response")
    public void testCreateTimesheetForFirstHalfLeave() {
        TimesheetResponse mockResponse = new TimesheetResponse();
        mockResponse.setId(0);
        mockResponse.setDescription("First half leave");
        mockResponse.setDuration(4);
        mockResponse.setExported(false);
        mockResponse.setBillable(false);
        mockResponse.setTags(Collections.singletonList("leave"));

        when(restTemplate.postForEntity(any(String.class), any(TimesheetRequest.class), eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Leave leave = new Leave();
        leave.setDate(LocalDate.parse("2024-08-08"));
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.FIRSTHALF);
        leave.setDescription("First half leave");

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertNotNull(response);
        assertEquals(mockResponse.getId(), response.getId());
        assertEquals(mockResponse.getDescription(), response.getDescription());
        assertEquals(mockResponse.getDuration(), response.getDuration());
        assertEquals(mockResponse.isExported(), response.isExported());
        assertEquals(mockResponse.isBillable(), response.isBillable());
        assertEquals(mockResponse.getTags(), response.getTags());
    }

    @Test
    @DisplayName("Given a second half leave, when creating a timesheet, then it should return a valid response")
    public void testCreateTimesheetForSecondHalfLeave() {
        TimesheetResponse mockResponse = new TimesheetResponse();
        mockResponse.setId(0);
        mockResponse.setDescription("Second half leave");
        mockResponse.setDuration(4);
        mockResponse.setExported(false);
        mockResponse.setBillable(false);
        mockResponse.setTags(Collections.singletonList("leave"));

        when(restTemplate.postForEntity(any(String.class), any(TimesheetRequest.class), eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        Leave leave = new Leave();
        leave.setDate(LocalDate.parse("2024-08-08"));
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.SECONDHALF);
        leave.setDescription("Second half leave");

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertNotNull(response);
        assertEquals(mockResponse.getId(), response.getId());
        assertEquals(mockResponse.getDescription(), response.getDescription());
        assertEquals(mockResponse.getDuration(), response.getDuration());
        assertEquals(mockResponse.isExported(), response.isExported());
        assertEquals(mockResponse.isBillable(), response.isBillable());
        assertEquals(mockResponse.getTags(), response.getTags());
    }
}