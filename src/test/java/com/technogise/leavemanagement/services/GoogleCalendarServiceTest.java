package com.technogise.leavemanagement.services;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.enums.HalfDay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleCalendarServiceTest {

   @Mock
    private Calendar mockCalendarService;

    private GoogleCalendarService googleCalendarService;
    static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "src\\main\\resources\\service-account-key.json";
    private static final List<String> CALENDAR_SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar");
    private static final String APPLICATION_NAME = "LeaveManagement";

    @BeforeEach
    public void setUp() throws GeneralSecurityException, IOException {
        MockitoAnnotations.openMocks(this);
        googleCalendarService = new GoogleCalendarService();
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
    
     @Test
    public void testInitializeCalendarServiceSuccess() throws IOException, GeneralSecurityException {
        Calendar service = initializeCalendarService();
        assertNotNull(service, "Calendar service should not be null");
    }

    public Calendar initializeCalendarService() {
        GoogleCredential credential = null;
        Calendar service = null;
        try {
            credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE_PATH))
                    .createScoped(CALENDAR_SCOPES);
            service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return service;
    }
    
}

