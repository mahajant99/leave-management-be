package com.technogise.leavemanagement.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technogise.leavemanagement.dtos.LeaveDTO;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.services.LeaveService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leaves")
@Slf4j
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping
    public ResponseEntity<List<Leave>> addLeaves(@RequestBody LeaveDTO leaveDTO) {
        try {
            List<Leave> leave = leaveService.addLeaves(leaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(leave);
        } catch (Exception e) {
            log.error("Error occurred while adding leave: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
