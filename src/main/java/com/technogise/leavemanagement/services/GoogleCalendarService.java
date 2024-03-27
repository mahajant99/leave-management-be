package com.technogise.leavemanagement.services;
import java.util.Arrays;
import java.util.List;

public class GoogleCalendarService {

    private static final String CALENDAR_ID = "abf2ff4f7bcc921456b336e2a7dc80b3daa936d455cdbcebdbc8e24473a8c175@group.calendar.google.com";
    private static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "src\\main\\resources\\service-account-key.json";
    private static final List<String> CALENDAR_SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar");
    
}
