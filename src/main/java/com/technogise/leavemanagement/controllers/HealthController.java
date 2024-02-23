package com.technogise.leavemanagement.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
