package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.technogise.leavemanagement.dtos.TimesheetRequest;
import com.technogise.leavemanagement.dtos.TimesheetResponse;
import com.technogise.leavemanagement.dtos.TimesheetResponse.MetaField;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.enums.HalfDay;

@SpringBootTest
public class KimaiTimesheetServiceTest {

    @Autowired
    private KimaiTimesheetService kimaiTimesheetService;

    @MockBean
    private RestTemplate restTemplate;

    @DisplayName("Testing the creation of a full day leave timesheet")
    @Test
    public void testCreateFullDayLeave() {
        Leave leave = new Leave();
        leave.setDate(LocalDate.of(2024, Month.APRIL, 15));
        leave.setDuration(1.0);
        leave.setHalfDay(null);
        leave.setDescription("Full Day Leave");

        TimesheetRequest request = new TimesheetRequest();
        LocalDate date = leave.getDate();
        request.setBegin(date.toString() + "T10:00:00");
        request.setEnd(date.toString() + "T18:00:00");
        request.setProject("1");
        request.setActivity("1");
        request.setDescription(leave.getDescription());
        request.setUser("1");
        request.setTags("string");
        request.setExported("false");
        request.setBillable("false");

        MetaField metaField = new MetaField();
        metaField.setName("string");
        metaField.setValue("string");

        List<MetaField> metaFields = Arrays.asList(metaField);

        TimesheetResponse expectedTimesheetResponse = new TimesheetResponse();
        expectedTimesheetResponse.setActivity(1);
        expectedTimesheetResponse.setProject(1);
        expectedTimesheetResponse.setUser(1);
        expectedTimesheetResponse.setTags(Arrays.asList("string"));
        expectedTimesheetResponse.setId(0);
        expectedTimesheetResponse.setBegin(date.toString() + "T10:00:00.000Z");
        expectedTimesheetResponse.setEnd(date.toString() + "T18:00:00.000Z");
        expectedTimesheetResponse.setDuration(8);
        expectedTimesheetResponse.setDescription("Full Day Leave");
        expectedTimesheetResponse.setRate(0.0);
        expectedTimesheetResponse.setInternalRate(0.0);
        expectedTimesheetResponse.setFixedRate(0.0);
        expectedTimesheetResponse.setHourlyRate(0.0);
        expectedTimesheetResponse.setExported(false);
        expectedTimesheetResponse.setBillable(false);
        expectedTimesheetResponse.setMetaFields(metaFields);

        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedTimesheetResponse, HttpStatus.OK));

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertEquals(expectedTimesheetResponse, response);
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getActivity(), response.getActivity());
        assertEquals(expectedTimesheetResponse.getProject(), response.getProject());
        assertEquals(expectedTimesheetResponse.getUser(), response.getUser());
        assertEquals(expectedTimesheetResponse.getTags(), response.getTags());
        assertEquals(expectedTimesheetResponse.getId(), response.getId());
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getDuration(), response.getDuration());
        assertEquals(expectedTimesheetResponse.getRate(), response.getRate());
        assertEquals(expectedTimesheetResponse.getInternalRate(), response.getInternalRate());
        assertEquals(expectedTimesheetResponse.getFixedRate(), response.getFixedRate());
        assertEquals(expectedTimesheetResponse.getHourlyRate(), response.getHourlyRate());
        assertEquals(expectedTimesheetResponse.isExported(), response.isExported());
        assertEquals(expectedTimesheetResponse.isBillable(), response.isBillable());

        Mockito.verify(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class));
    }

    @DisplayName("Testing the creation of a first half leave timesheet")
    @Test
    public void testCreateFirstHalfLeave() {
        Leave leave = new Leave();
        leave.setDate(LocalDate.of(2024, Month.APRIL, 15));
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.FIRSTHALF);
        leave.setDescription("First Half Leave");

        TimesheetRequest request = new TimesheetRequest();
        LocalDate date = leave.getDate();
        request.setBegin(date.toString() + "T10:00:00");
        request.setEnd(date.toString() + "T14:00:00");
        request.setProject("1");
        request.setActivity("1");
        request.setDescription(leave.getDescription());
        request.setUser("1");
        request.setTags("string");
        request.setExported("false");
        request.setBillable("false");

        MetaField metaField = new MetaField();
        metaField.setName("string");
        metaField.setValue("string");

        List<MetaField> metaFields = Arrays.asList(metaField);

        TimesheetResponse expectedTimesheetResponse = new TimesheetResponse();
        expectedTimesheetResponse.setActivity(1);
        expectedTimesheetResponse.setProject(1);
        expectedTimesheetResponse.setUser(1);
        expectedTimesheetResponse.setTags(Arrays.asList("string"));
        expectedTimesheetResponse.setId(0);
        expectedTimesheetResponse.setBegin(date.toString() + "T10:00:00.000Z");
        expectedTimesheetResponse.setEnd(date.toString() + "T14:00:00.000Z");
        expectedTimesheetResponse.setDuration(4);
        expectedTimesheetResponse.setDescription("First Half Leave");
        expectedTimesheetResponse.setRate(0.0);
        expectedTimesheetResponse.setInternalRate(0.0);
        expectedTimesheetResponse.setFixedRate(0.0);
        expectedTimesheetResponse.setHourlyRate(0.0);
        expectedTimesheetResponse.setExported(false);
        expectedTimesheetResponse.setBillable(false);
        expectedTimesheetResponse.setMetaFields(metaFields);

        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedTimesheetResponse, HttpStatus.OK));

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertEquals(expectedTimesheetResponse, response);
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getActivity(), response.getActivity());
        assertEquals(expectedTimesheetResponse.getProject(), response.getProject());
        assertEquals(expectedTimesheetResponse.getUser(), response.getUser());
        assertEquals(expectedTimesheetResponse.getTags(), response.getTags());
        assertEquals(expectedTimesheetResponse.getId(), response.getId());
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getDuration(), response.getDuration());
        assertEquals(expectedTimesheetResponse.getRate(), response.getRate());
        assertEquals(expectedTimesheetResponse.getInternalRate(), response.getInternalRate());
        assertEquals(expectedTimesheetResponse.getFixedRate(), response.getFixedRate());
        assertEquals(expectedTimesheetResponse.getHourlyRate(), response.getHourlyRate());
        assertEquals(expectedTimesheetResponse.isExported(), response.isExported());
        assertEquals(expectedTimesheetResponse.isBillable(), response.isBillable());

        Mockito.verify(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class));
    }

    @DisplayName("Testing the creation of a second half leave timesheet")
    @Test
    public void testCreateSecondHalfLeave() {
        Leave leave = new Leave();
        leave.setDate(LocalDate.of(2024, Month.APRIL, 15));
        leave.setDuration(0.5);
        leave.setHalfDay(HalfDay.SECONDHALF);
        leave.setDescription("Second Half Leave");

        TimesheetRequest request = new TimesheetRequest();
        LocalDate date = leave.getDate();
        request.setBegin(date.toString() + "T14:00:00");
        request.setEnd(date.toString() + "T18:00:00");
        request.setProject("1");
        request.setActivity("1");
        request.setDescription(leave.getDescription());
        request.setUser("1");
        request.setTags("string");
        request.setExported("false");
        request.setBillable("false");

        MetaField metaField = new MetaField();
        metaField.setName("string");
        metaField.setValue("string");

        List<MetaField> metaFields = Arrays.asList(metaField);

        TimesheetResponse expectedTimesheetResponse = new TimesheetResponse();
        expectedTimesheetResponse.setActivity(1);
        expectedTimesheetResponse.setProject(1);
        expectedTimesheetResponse.setUser(1);
        expectedTimesheetResponse.setTags(Arrays.asList("string"));
        expectedTimesheetResponse.setId(0);
        expectedTimesheetResponse.setBegin(date.toString() + "T14:00:00.000Z");
        expectedTimesheetResponse.setEnd(date.toString() + "T18:00:00.000Z");
        expectedTimesheetResponse.setDuration(4);
        expectedTimesheetResponse.setDescription("Second Half Leave");
        expectedTimesheetResponse.setRate(0.0);
        expectedTimesheetResponse.setInternalRate(0.0);
        expectedTimesheetResponse.setFixedRate(0.0);
        expectedTimesheetResponse.setHourlyRate(0.0);
        expectedTimesheetResponse.setExported(false);
        expectedTimesheetResponse.setBillable(false);
        expectedTimesheetResponse.setMetaFields(metaFields);

        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class)))
                .thenReturn(new ResponseEntity<>(expectedTimesheetResponse, HttpStatus.OK));

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);

        assertEquals(expectedTimesheetResponse, response);
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getActivity(), response.getActivity());
        assertEquals(expectedTimesheetResponse.getProject(), response.getProject());
        assertEquals(expectedTimesheetResponse.getUser(), response.getUser());
        assertEquals(expectedTimesheetResponse.getTags(), response.getTags());
        assertEquals(expectedTimesheetResponse.getId(), response.getId());
        assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
        assertEquals(expectedTimesheetResponse.getDuration(), response.getDuration());
        assertEquals(expectedTimesheetResponse.getRate(), response.getRate());
        assertEquals(expectedTimesheetResponse.getInternalRate(), response.getInternalRate());
        assertEquals(expectedTimesheetResponse.getFixedRate(), response.getFixedRate());
        assertEquals(expectedTimesheetResponse.getHourlyRate(), response.getHourlyRate());
        assertEquals(expectedTimesheetResponse.isExported(), response.isExported());
        assertEquals(expectedTimesheetResponse.isBillable(), response.isBillable());

        Mockito.verify(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(TimesheetRequest.class), Mockito.eq(TimesheetResponse.class));
    }
}