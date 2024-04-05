package com.technogise.leavemanagement.controllers;

<<<<<<< HEAD
import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/oauth")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/info")
    public ResponseEntity getUserInfo(Principal principal) {
        User user = userService.getUser(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(user);
    }
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technogise.leavemanagement.entities.User;
import com.technogise.leavemanagement.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "6";

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Page<User> usersPage = userService.getAllUsers(page, size);
        return new ResponseEntity<Page<User>>(usersPage, HttpStatus.OK);
    }    
>>>>>>> main
}
