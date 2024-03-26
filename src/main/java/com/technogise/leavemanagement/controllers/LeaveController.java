package com.technogise.leavemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.services.LeaveService;
import com.technogise.leavemanagement.dtos.LeaveDTO;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    private static final String defaultPageSize = "6";
    private static final String defaultPageCount = "0";

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<Leave>> getLeaves(@PathVariable("userId") Long userId, @RequestParam(defaultPageCount) int page,
            @RequestParam(defaultPageSize) int size) {

        Page<Leave> leavesPage = leaveService.getLeavesByUserId(userId, page, size);
        return leavesPage.isEmpty() ? new ResponseEntity<Page<Leave>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<Page<Leave>>(leavesPage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Leave>> getAllLeaves(@RequestParam(defaultPageCount) int page,
            @RequestParam(defaultPageSize) int size) {

        Page<Leave> leavesPage = leaveService.getAllLeaves(page, size);
        return new ResponseEntity<Page<Leave>>(leavesPage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeave(@PathVariable("id") Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<List<Leave>> addLeaves(@Valid @RequestBody LeaveDTO leaveDTO) throws Exception {
        List<Leave> leaves = leaveService.addLeaves(leaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaves);
    }
}
