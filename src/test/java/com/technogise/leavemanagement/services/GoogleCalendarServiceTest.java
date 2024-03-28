package com.technogise.leavemanagement.services;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GoogleCalendarServiceTest {

    private GoogleCalendarService googleCalendarService;
    private Calendar mockCalendarService;

    @BeforeEach
    public void setUp() throws GeneralSecurityException, IOException {
        googleCalendarService = new GoogleCalendarService();
        mockCalendarService = mock(Calendar.class);
        googleCalendarService.initializeCalendarService(); 
    }

    @Test
    public void testCreateLeaveEventFullDay() {
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        leave.setDate(LocalDate.now());
        leave.setDuration(1.0);
        leave.setUser(user);

        Event event = googleCalendarService.createLeaveEvent(leave);
        assertNotNull(event, "Event should not be null");
        assertTrue(event.getSummary().contains("on leave"), "Event summary should indicate on leave");
    }

    @Test
    public void testCreateLeaveEventHalfDay() {
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        leave.setDate(LocalDate.now());
        leave.setDuration(0.5);
        leave.setUser(user);
        leave.setHalfDay(HalfDay.FIRSTHALF);

        Event event = googleCalendarService.createLeaveEvent(leave);
        assertNotNull(event, "Event should not be null");
        assertTrue(event.getSummary().contains(" on leave(FIRSTHALF)"), "Rick on leave(FIRSTHALF)");
    }
    
}

