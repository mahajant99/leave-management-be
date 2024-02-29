package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    public void Should_ReturnUser_When_UserExists() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = leaveService.getUserById(userId);

        assertEquals(true, retrievedUser.isPresent());
    }

    @Test
    public void Should_ReturnEmpty_When_UserDoesNotExists() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = leaveService.getUserById(userId);

        assertEquals(false, retrievedUser.isPresent());

    }

}
