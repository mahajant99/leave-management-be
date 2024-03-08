package com.technogise.leavemanagement.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.time.LocalDate;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.LeaveRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    @DisplayName("Given a user ID and pagination/sorting parameters, when fetching leaves, then the expected page of leaves is returned")
    void shouldFetchLeavesByUserId() {
        
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String[] roles = {"User"};
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        PageRequest pageable = PageRequest.of(page, size, sort);

        User user = new User(userId,"Summer","summer@gmail",roles, null);
        
        Leave leave1 = new Leave();
        leave1.setId(1L);
        leave1.setUser(user);
        leave1.setDate(LocalDate.now());

        Leave leave2 = new Leave();
        leave2.setId(2L);
        leave2.setUser(user);
        leave2.setDate(LocalDate.now().minusDays(1));

        List<Leave> leaves = Arrays.asList(leave1, leave2);
        Page<Leave> expectedPage = new PageImpl<>(leaves, pageable, leaves.size());

        when(leaveRepository.findByUserId(userId, pageable)).thenReturn(expectedPage);

        Page<Leave> resultPage = leaveService.getLeavesByUserId(userId, page, size);
        
        assertEquals(expectedPage, resultPage);
    }

    @Test
    @DisplayName("Given a user and a leave exists, when you softdelete a leave, then deleted should be set to true.")
    public void shouldRemoveLeaveById() {
        
        Long leaveId = 1L;
        String[] userRole = {"user"};
        User user = new User(001l, "Test User" , "testuser@gmail.com", userRole, null);

        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setDeleted(false);
        leave.setDate(LocalDate.now());
        leave.setDuration(1);
        leave.setDescription("Sick Leave");
        leave.setHalfDay(null);
        leave.setUser(user);

        when(leaveRepository.findById(leaveId)).thenReturn(java.util.Optional.of(leave));

        String result = leaveService.remove(leaveId);
        Optional<Leave> response = leaveRepository.findById(leaveId);

        assertTrue(response.get().isDeleted());
        assertEquals(result, "deleted");
    }
    
    @Test
    @DisplayName("Given a leave does not exists, when you softdelete that leave, then null should be returned.")
    public void removeShouldReturnNull() {
        
        Long leaveId = 1L;
        Optional<Leave> leave = Optional.empty();

        when(leaveRepository.findById(leaveId)).thenReturn(leave);

        String result = leaveService.remove(leaveId);
        
        assertEquals(result,null);
    }

    @Test
    @DisplayName("Given a leave exists, when you softdelete that leave and any run time exception exist, then error message should be returned.")
    public void removeShouldReturnError() {
        
        Long leaveId = 1L;
        String[] userRole = {"user"};
        User user = new User(001l, "Test User" , "testuser@gmail.com", userRole, null);

        Leave leave = new Leave();
        leave.setId(leaveId);
        leave.setDeleted(false);
        leave.setDate(LocalDate.now());
        leave.setDuration(1);
        leave.setDescription("Sick Leave");
        leave.setHalfDay(null);
        leave.setUser(user);
        Exception exception = new RuntimeException("Internal Server Error"); 
        
        when(leaveRepository.findById(leaveId)).thenThrow(exception);

        String result = leaveService.remove(leaveId);
        String errorMessage = exception.toString();
        
        assertEquals(result,errorMessage);

    }
}
