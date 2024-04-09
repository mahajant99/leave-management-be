package com.technogise.leavemanagement.services;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.exceptions.CalendarConfigException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GoogleCalendarServiceTest {
    
    private GoogleCalendarService googleCalendarService;
    private Calendar.Events mockEvents;
    private Calendar.Events.Insert mockInsert;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, CalendarConfigException {
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
    @DisplayName("Given a full-day leave, when added, then it should be created as an event in Google Calendar")
    public void testAddLeave() throws IOException, CalendarConfigException {

        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        ZoneId kolkataZoneId = ZoneId.of("Asia/Kolkata");
        LocalDate currentDateInKolkata = ZonedDateTime.now(kolkataZoneId).toLocalDate();
        leave.setDate(currentDateInKolkata);
        leave.setDuration(1);
        leave.setUser(user);
        when(mockEvents.insert(anyString(), any(Event.class))).thenReturn(mockInsert);

        googleCalendarService.addLeave(leave);
    
        verify(mockEvents, times(1)).insert(anyString(), any(Event.class));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(mockEvents).insert(anyString(), eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getSummary()).isEqualTo("Rick on leave");
    }

    @Test
    @DisplayName("Given a half-day first-half leave, when added, then it should be created as an event in Google Calendar")
    public void testAddFirstHalfLeave() throws IOException, CalendarConfigException {
        
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        ZoneId kolkataZoneId = ZoneId.of("Asia/Kolkata");
        LocalDate currentDateInKolkata = ZonedDateTime.now(kolkataZoneId).toLocalDate();
        leave.setDate(currentDateInKolkata);
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.FIRSTHALF);
        leave.setUser(user);
        when(mockEvents.insert(anyString(), any(Event.class))).thenReturn(mockInsert);

        googleCalendarService.addLeave(leave);
    
        verify(mockEvents, times(1)).insert(anyString(), any(Event.class));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(mockEvents).insert(anyString(), eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getSummary()).isEqualTo("Rick on leave(First Half)");
    }

    @Test
    @DisplayName("Given a half-day second-half leave, when added, then it should be created as an event in Google Calendar")
    public void testAddSecondHalfLeave() throws IOException, CalendarConfigException {
        
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        ZoneId kolkataZoneId = ZoneId.of("Asia/Kolkata");
        LocalDate currentDateInKolkata = ZonedDateTime.now(kolkataZoneId).toLocalDate();
        leave.setDate(currentDateInKolkata);
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.SECONDHALF);
        leave.setUser(user);
        when(mockEvents.insert(anyString(), any(Event.class))).thenReturn(mockInsert);

        googleCalendarService.addLeave(leave);
    
        verify(mockEvents, times(1)).insert(anyString(), any(Event.class));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(mockEvents).insert(anyString(), eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getSummary()).isEqualTo("Rick on leave(Second Half)");
    }

    @Test
    @DisplayName("Given an IOException when adding a leave, then it should throw CalendarConfigException")
    public void testIOException() throws IOException, CalendarConfigException {
    
        User user = new User();
        user.setId(1L);
        user.setName("Rick");
        Leave leave = new Leave();
        ZoneId kolkataZoneId = ZoneId.of("Asia/Kolkata");
        LocalDate currentDateInKolkata = ZonedDateTime.now(kolkataZoneId).toLocalDate();
        leave.setDate(currentDateInKolkata);
        leave.setDuration(1);
        leave.setUser(user);

        when(mockEvents.insert(anyString(), any(Event.class))).thenThrow(IOException.class);

        assertThrows(CalendarConfigException.class, () -> googleCalendarService.addLeave(leave));
    }
}

