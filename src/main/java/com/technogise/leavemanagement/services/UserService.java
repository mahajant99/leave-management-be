package com.technogise.leavemanagement.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.technogise.leavemanagement.configs.JWTUtils;
import com.technogise.leavemanagement.dtos.IdTokenRequestDto;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    
    @Autowired
    private final JWTUtils jwtUtils;

    private final GoogleIdTokenVerifier verifier;
    private static final String ALLOWEDDOMAIN = "technogise.com";

    public Page<User> getAllUsers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    public UserService(@Value("${app.googleClientId}") String clientId, UserRepository userRepository,
                          JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String loginOAuthGoogle(IdTokenRequestDto requestBody) {
        User user = verifyIDToken(requestBody.getIdToken());
        if (user == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }
        user = createOrUpdateUser(user);
        boolean isRememberMe = false;
        return jwtUtils.createToken(user, isRememberMe);
    }

    @Transactional
    public User createOrUpdateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser == null) {
            userRepository.save(user);
            return user;
        }
        existingUser.setName(user.getName());
        existingUser.setPictureUrl(user.getPictureUrl());
        userRepository.save(existingUser);
        return existingUser;
    }

    private User verifyIDToken(String idToken) {
        User newUser = new User();
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) {
                throw new IllegalArgumentException("ID token verification failed");
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            if (!email.matches("^.+@" + ALLOWEDDOMAIN + "$")) {
               throw new IllegalArgumentException("Email domain not allowed");
            }
    
            String fullName = firstName.concat(" ").concat(lastName);

            newUser.setName(fullName);
            newUser.setEmail(email);
            newUser.setPictureUrl(pictureUrl);

            return newUser;
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalArgumentException("Failed to verify ID token", e);
        }
    }
}
