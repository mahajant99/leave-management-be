package com.technogise.leavemanagement.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.technogise.leavemanagement.configs.JWTUtils;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;


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
    
    @Test
    @DisplayName("Given a new user, when creating or updating the user, then the user is successfully created")
    void testCreateOrUpdateUser_NewUser() {
        User newUser = new User();
        newUser.setEmail("Rick@technogise.com");
        newUser.setName("Rick");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.createOrUpdateUser(newUser);

        assertNotNull(result);
        assertEquals(newUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Given an existing user, when updating the user with new details, then the updated user is returned with the existing ID and new name")
    void testCreateOrUpdateUser_ExistingUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");
        existingUser.setName("Existing User");

        User updateUser = new User();
        updateUser.setEmail(existingUser.getEmail());
        updateUser.setName("Updated User");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.createOrUpdateUser(updateUser);

       assertNotNull(result);
       assertEquals(existingUser.getId(), result.getId());
       assertEquals(updateUser.getName(), result.getName());
       verify(userRepository, times(1)).findByEmail(existingUser.getEmail());
       verify(userRepository, times(1)).save(any(User.class));
    }
}