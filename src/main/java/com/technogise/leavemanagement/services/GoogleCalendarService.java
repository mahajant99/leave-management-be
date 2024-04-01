package com.technogise.leavemanagement.services;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.enums.HalfDay;
import com.technogise.leavemanagement.exceptions.CalendarConfigException;
import com.google.api.services.calendar.Calendar;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.ZoneId;

@Service
public class GoogleCalendarService {

    private static final String CALENDAR_ID = "abf2ff4f7bcc921456b336e2a7dc80b3daa936d455cdbcebdbc8e24473a8c175@group.calendar.google.com";
    static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "src\\main\\resources\\service-account-key.json";
    private static final List<String> CALENDAR_SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar");
    private static final String APPLICATION_NAME = "LeaveManagement";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String KOLKATA_TIME_ZONE = "Asia/Kolkata";
    private static final String ON_LEAVE = " on leave";
    private static final Double FULL_DAY = 1.0;
    private static final String FIRST_HALF = "First Half";
    private static final String SECOND_HALF = "Second Half";
    private Calendar service = null;

     GoogleCalendarService() throws CalendarConfigException {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE_PATH))
                        .createScoped(CALENDAR_SCOPES);
            this.service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            
            throw new CalendarConfigException("Failed to initialize Google Calendar service", e);
        }
        
    }

    public String generateLeaveSummary(Leave leave) {
        String userName = leave.getUser().getName();
        Double duration = leave.getDuration();

        if (FULL_DAY.equals(duration)) {
            return userName + ON_LEAVE;
        } else if(leave.getHalfDay().equals(HalfDay.FIRSTHALF)) {
            return userName + ON_LEAVE + "(" + FIRST_HALF + ")";
        }
        return userName + ON_LEAVE + "(" + SECOND_HALF + ")";
    }

    private Event createLeaveEvent(Leave leave) {
        String SUMMARY = generateLeaveSummary(leave);
        
        ZoneId kolkataZoneId = ZoneId.of(KOLKATA_TIME_ZONE);
        Date startDate = Date.from(leave.getDate().atStartOfDay(kolkataZoneId).toInstant());
        Date endDate = Date.from(leave.getDate().plusDays(1).atStartOfDay(kolkataZoneId).toInstant());
    
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
    
        DateTime startDateTime = new DateTime(startDateStr);
        DateTime endDateTime = new DateTime(endDateStr);
    
        EventDateTime start = new EventDateTime().setDate(startDateTime);
        EventDateTime end = new EventDateTime().setDate(endDateTime);
        
        Event event = new Event()
                .setSummary(SUMMARY)
                .setStart(start)
                .setEnd(end);
    
        return event;
    }
    
    public void addLeave(Leave leave) throws CalendarConfigException{

        Event event = createLeaveEvent(leave);
    
        try {
            this.service.events().insert(CALENDAR_ID, event).execute();
        } catch (IOException e) {
            
            throw new CalendarConfigException("Failed to add leave to calendar", e);
        }    
    }
}
