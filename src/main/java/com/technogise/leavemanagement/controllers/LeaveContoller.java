package com.technogise.leavemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.technogise.leavemanagement.services.LeaveService;

@RestController
public class LeaveContoller {

    @Autowired
    private LeaveService leaveService;

    @DeleteMapping("/leaves/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable("id") Long id) {
        leaveService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
