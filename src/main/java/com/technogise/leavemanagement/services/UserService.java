package com.technogise.leavemanagement.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.technogise.leavemanagement.configs.JWTUtils;
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    
    @Autowired
    private final JWTUtils jwtUtils;

    @Autowired
    private final GoogleIdTokenVerifier verifier;

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
    
}
