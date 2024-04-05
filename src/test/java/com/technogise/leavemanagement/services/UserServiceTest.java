package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

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

import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Given pagination/sorting parameters and users exist, when fetching all users, then the expected page of users is returned")
    void shouldFetchAllUsers() {

        int page = 0;
        int size = 10;
        String[] roles = {"User","Admin"};
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        PageRequest pageable = PageRequest.of(page, size, sort);

        User testUser1 = new User(1L, "testuser1", "testuser1@gmail", roles, null);
        User testUser2 = new User(2L, "testuser2", "testuser2@gmail", roles, null);

        List<User> users = Arrays.asList(testUser2,testUser1);
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> resultPage = userService.getAllUsers(page, size);

        assertEquals(expectedPage, resultPage);
    }    
}