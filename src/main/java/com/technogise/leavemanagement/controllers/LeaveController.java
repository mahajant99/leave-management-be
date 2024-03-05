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

@RestController
@RequestMapping("/leaves")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping
    public ResponseEntity<List<Leave>> addLeaves(@RequestBody LeaveDTO leaveDTO) {
        if(!validateLeaveDTO(leaveDTO)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        List<Leave> leaves = leaveService.addLeaves(leaveDTO);

        if (leaves.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(leaves);
    }

    private boolean validateLeaveDTO(LeaveDTO leaveDTO) {
        return (leaveDTO.getUserId() == null ||
                leaveDTO.getStartDate() == null ||
                leaveDTO.getEndDate() == null ||
                leaveDTO.getLeaveType() == null ||
                leaveDTO.getLeaveType().isEmpty());
    }

}
