package com.technogise.leavemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.technogise.leavemanagement.entities.Leave;
import org.springframework.data.domain.Page;

import com.technogise.leavemanagement.services.LeaveService;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<Leave>> getLeaves(@PathVariable("userId") Long userId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        Page<Leave> leavesPage = leaveService.getLeavesByUserId(userId, page, size);
        return leavesPage.isEmpty() ? new ResponseEntity<Page<Leave>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<Page<Leave>>(leavesPage, HttpStatus.OK);

    }

}
