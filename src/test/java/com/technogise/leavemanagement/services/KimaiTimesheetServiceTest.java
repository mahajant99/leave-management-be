// //         assertEquals(expectedTimesheetResponse.getActivity(), actualResponse.getActivity());
// //         assertEquals(expectedTimesheetResponse.getProject(), actualResponse.getProject());
// //         assertEquals(expectedTimesheetResponse.getUser(), actualResponse.getUser());
// //         assertEquals(expectedTimesheetResponse.getTags(), actualResponse.getTags());
// //         assertEquals(expectedTimesheetResponse.getId(), actualResponse.getId());
// //         assertEquals(expectedTimesheetResponse.getDescription(), actualResponse.getDescription());
// //         assertEquals(expectedTimesheetResponse.getDuration(), actualResponse.getDuration()/3600);
// //         assertEquals(expectedTimesheetResponse.getRate(), actualResponse.getRate());
// //         assertEquals(expectedTimesheetResponse.getInternalRate(), actualResponse.getInternalRate());
// //         assertEquals(expectedTimesheetResponse.getFixedRate(), actualResponse.getFixedRate());
// //         assertEquals(expectedTimesheetResponse.getHourlyRate(), actualResponse.getHourlyRate());
// //         assertEquals(expectedTimesheetResponse.isExported(), actualResponse.isExported());
// //         assertEquals(expectedTimesheetResponse.isBillable(), actualResponse.isBillable());        
// //     }    
// // }

package com.technogise.leavemanagement.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.technogise.leavemanagement.dtos.TimesheetRequest;
import com.technogise.leavemanagement.dtos.TimesheetResponse;
import com.technogise.leavemanagement.dtos.TimesheetResponse.MetaField;
import com.technogise.leavemanagement.entities.Leave;

// @ExtendWith(MockitoExtension.class)
public class KimaiTimesheetServiceTest {

    @Mock
     RestTemplate restTemplate;

    @InjectMocks
     KimaiTimesheetService kimaiTimesheetService;

    @BeforeEach
    public void setUp(){
       MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFullDayLeave() {
        Leave leave = new Leave();
        leave.setDate(LocalDate.of(2024, Month.APRIL, 4));
        leave.setDuration(1.0); 
        leave.setHalfDay(null);
        leave.setDescription("Full Day Leave");

        TimesheetRequest request = new TimesheetRequest();
        request.setBegin(LocalDate.of(2024, Month.APRIL, 4).toString()+"T10:00:00");
        request.setEnd(LocalDate.of(2024, Month.APRIL, 4).toString()+"T18:00:00"); 
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
        expectedTimesheetResponse.setTags( Arrays.asList("string"));
        expectedTimesheetResponse.setId(0);
        expectedTimesheetResponse.setBegin(LocalDate.now().toString()+"T10:00:00.000Z");
        expectedTimesheetResponse.setEnd(LocalDate.now().toString()+"T18:00:00.000Z");
        expectedTimesheetResponse.setDuration(8);
        expectedTimesheetResponse.setDescription("Full Day Leave");
        expectedTimesheetResponse.setRate(0.0);
        expectedTimesheetResponse.setInternalRate(0.0);
        expectedTimesheetResponse.setFixedRate(0.0);
        expectedTimesheetResponse.setHourlyRate(0.0);
        expectedTimesheetResponse.setExported(false);
        expectedTimesheetResponse.setBillable(false);
        expectedTimesheetResponse.setMetaFields(metaFields);

        Mockito
          .when(restTemplate.postForEntity(
            "https://demo.kimai.org/api/timesheets", request, TimesheetResponse.class))
          .thenReturn(new ResponseEntity(expectedTimesheetResponse, HttpStatus.OK));

        TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);
        Assertions.assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());
    }
}





// import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;      
// import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

// import java.net.URI;
// import java.net.URISyntaxException;
// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;

// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.web.client.ExpectedCount;
// import org.springframework.test.web.client.MockRestServiceServer;
// import org.springframework.web.client.RestTemplate;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import com.technogise.leavemanagement.dtos.TimesheetRequest;
// import com.technogise.leavemanagement.dtos.TimesheetResponse;
// import com.technogise.leavemanagement.dtos.TimesheetResponse.MetaField;
// import com.technogise.leavemanagement.entities.Leave;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest(classes = SpringTestConfig.class)
// public class KimaiTimesheetServiceTest {

//     @Autowired
//     private KimaiTimesheetService kimaiTimesheetService;

//     @Autowired
//     private RestTemplate restTemplate;

//     private MockRestServiceServer mockServer;
//     private ObjectMapper mapper = new ObjectMapper();

//     @BeforeEach
//     public void init() {
//         mockServer = MockRestServiceServer.createServer(restTemplate);
//     }
    
//     @Test                                                                                          
//     public void testCreateFullDayLeave() throws JsonProcessingException, URISyntaxException { 
          
//         Leave leave = new Leave();
//         leave.setDate(LocalDate.now());
//         leave.setDuration(1.0); 
//         leave.setHalfDay(null);
//         leave.setDescription("Full Day Leave");

//         TimesheetRequest request = new TimesheetRequest();
//         request.setBegin(LocalDate.now().toString()+"T10:00:00");
//         request.setEnd(LocalDate.now().toString()+"T18:00:00"); 
//         request.setProject("1");
//         request.setActivity("1");
//         request.setDescription(leave.getDescription());
//         request.setUser("1");
//         request.setTags("string");
//         request.setExported("false");
//         request.setBillable("false"); 


//         MetaField metaField = new MetaField();
//         metaField.setName("string");
//         metaField.setValue("string");

//         List<MetaField> metaFields = Arrays.asList(metaField);

//         TimesheetResponse expectedTimesheetResponse = new TimesheetResponse();
//         expectedTimesheetResponse.setActivity(1);
//         expectedTimesheetResponse.setProject(1);
//         expectedTimesheetResponse.setUser(1);
//         expectedTimesheetResponse.setTags( Arrays.asList("string"));
//         expectedTimesheetResponse.setId(0);
//         expectedTimesheetResponse.setBegin(LocalDate.now().toString()+"T10:00:00.000Z");
//         expectedTimesheetResponse.setEnd(LocalDate.now().toString()+"T18:00:00.000Z");
//         expectedTimesheetResponse.setDuration(8);
//         expectedTimesheetResponse.setDescription("Full Day Leave");
//         expectedTimesheetResponse.setRate(0.0);
//         expectedTimesheetResponse.setInternalRate(0.0);
//         expectedTimesheetResponse.setFixedRate(0.0);
//         expectedTimesheetResponse.setHourlyRate(0.0);
//         expectedTimesheetResponse.setExported(false);
//         expectedTimesheetResponse.setBillable(false);
//         expectedTimesheetResponse.setMetaFields(metaFields);
        
//         mockServer.expect(ExpectedCount.once(), 
//           requestTo(new URI("https://demo.kimai.org/api/timesheets")))
//           .andExpect(method(HttpMethod.POST))
//           .andRespond(withStatus(HttpStatus.OK)
//           .contentType(MediaType.APPLICATION_JSON)
//           .body(mapper.writeValueAsString(expectedTimesheetResponse))
//         );                                   
                       
//         TimesheetResponse response = kimaiTimesheetService.createTimesheet(leave);
//         mockServer.verify();
//         Assertions.assertEquals(expectedTimesheetResponse.getDescription(), response.getDescription());                                                        
//     }
// }
