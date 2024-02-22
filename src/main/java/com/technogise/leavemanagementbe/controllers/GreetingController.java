package com.technogise.leavemanagementbe.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class GreetingController {

    @GetMapping("/")
    public String greetUser() {
        return "Hello World !";
    }

}
