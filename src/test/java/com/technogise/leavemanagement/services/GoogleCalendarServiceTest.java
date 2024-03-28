package com.technogise.leavemanagement.services;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;

public class GoogleCalendarServiceTest {
    
    private GoogleCalendarService googleCalendarService;
    private Calendar.Events mockEvents;
    private Calendar.Events.Insert mockInsert;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        googleCalendarService = new GoogleCalendarService();
        mockEvents = mock(Calendar.Events.class); 
        mockInsert = mock(Calendar.Events.Insert.class);
    
        Field field = GoogleCalendarService.class.getDeclaredField("service");
        field.setAccessible(true);
        Calendar mockCalendar = mock(Calendar.class);
        when(mockCalendar.events()).thenReturn(mockEvents);
        field.set(googleCalendarService, mockCalendar);
        
    }

    @Test
    public void testAddLeave() throws IOException {
        
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        leave.setDate(LocalDate.now());
        leave.setDuration(1);
        leave.setUser(user);
        when(mockEvents.insert(anyString(), any(Event.class))).thenReturn(mockInsert);

        googleCalendarService.addLeave(leave);
    
        verify(mockEvents, times(1)).insert(anyString(), any(Event.class));
    }

    @Test
    public void testAddHalfLeave() throws IOException {
        
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        leave.setDate(LocalDate.now());
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.SECONDHALF);
        leave.setUser(user);
        when(mockEvents.insert(anyString(), any(Event.class))).thenReturn(mockInsert);

        googleCalendarService.addLeave(leave);
    
        verify(mockEvents, times(1)).insert(anyString(), any(Event.class));
    }
}

