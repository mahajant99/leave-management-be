package com.technogise.leavemanagement.services;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.technogise.leavemanagement.entities.Leave;
import com.google.api.services.calendar.Calendar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.ZoneId;


public class GoogleCalendarService {

    private static final String CALENDAR_ID = "abf2ff4f7bcc921456b336e2a7dc80b3daa936d455cdbcebdbc8e24473a8c175@group.calendar.google.com";
    private static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "src\\main\\resources\\service-account-key.json";
    private static final List<String> CALENDAR_SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar");
    private static final String APPLICATION_NAME = "LeaveManagement";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String KOLKATA_TIME_ZONE = "Asia/Kolkata";
    private static final String ON_LEAVE = "on leave";
    private static final Double FULL_DAY = 1.0;


    public void addLeave(Leave leave) throws FileNotFoundException, IOException, GeneralSecurityException{

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE_PATH))
                    .createScoped(CALENDAR_SCOPES);
    
        Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    
        String SUMMARY = leave.getDuration() == FULL_DAY ? leave.getUser().getName() + ON_LEAVE : leave.getUser().getName() + ON_LEAVE + leave.getHalfDay().toString();
        
        Event event = new Event()
                .setSummary(SUMMARY);
    
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
                
        event.setStart(start);
        event.setEnd(end);
    
        service.events().insert(CALENDAR_ID, event).execute();

    }
    
}
