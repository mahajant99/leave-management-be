package com.technogise.leavemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.technogise.leavemanagement.dtos.TimesheetRequest;
import com.technogise.leavemanagement.dtos.TimesheetResponse;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.enums.HalfDay;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class KimaiTimesheetService {

    private final RestTemplate restTemplate;

    @Autowired
    public KimaiTimesheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final double fullDay = 1.0;

    public TimesheetResponse createTimesheet(Leave createdLeaves) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AUTH-USER", "susan_super");
        headers.set("X-AUTH-TOKEN", "api_kitten");

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().addAll(headers);
            return execution.execute(request, body);
        }));

        TimesheetRequest request = new TimesheetRequest();
        LocalDate date = createdLeaves.getDate();
        if (createdLeaves.getDuration() == fullDay) {
            request.setBegin(date.toString() + "T10:00:00");
            request.setEnd(date.toString() + "T18:00:00");
        } else {
            if (createdLeaves.getHalfDay() == HalfDay.FIRSTHALF) {
                request.setBegin(date.toString() + "T10:00:00");
                request.setEnd(date.toString() + "T14:00:00");
            } else {
                request.setBegin(date.toString() + "T14:00:00");
                request.setEnd(date.toString() + "T18:00:00");
            }
        }
        request.setProject("1");
        request.setActivity("1");
        request.setDescription(createdLeaves.getDescription());
        request.setUser("1");
        request.setTags("string");
        request.setExported("false");
        request.setBillable("false");

        try {
            ResponseEntity<TimesheetResponse> response = restTemplate.postForEntity("https://demo.kimai.org/api/timesheets", request, TimesheetResponse.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return null;
        }
    }
}