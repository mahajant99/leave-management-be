package com.technogise.leavemanagement.controllers;

import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.technogise.leavemanagement.dtos.UserDTO.convertToDto;

@RestController
@RequestMapping("/v1/oauth")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/info")
    public ResponseEntity getUserInfo(Principal principal) {
        User user = userService.getUser(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertToDto(user));
    }
}
