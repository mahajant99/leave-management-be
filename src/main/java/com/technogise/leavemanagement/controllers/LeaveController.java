package com.technogise.leavemanagement.controllers;

import com.technogise.leavemanagement.exceptions.LeaveAlreadyExistsException;
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

import java.security.Principal;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/oauth/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "6";


    @GetMapping("/users")
    public ResponseEntity<Page<Leave>> getLeaves(Principal principal,
    @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Page<Leave> leavesPage = leaveService.getLeavesByUserId(Long.valueOf(principal.getName()), page, size);
        return leavesPage.isEmpty() ? new ResponseEntity<Page<Leave>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<Page<Leave>>(leavesPage, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<Leave>> getAllLeaves(@PathVariable("userId") Long userId,@RequestParam(defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(defaultValue = DEFAULT_SIZE) int size) {

        Page<Leave> leavesPage = leaveService.getLeavesByUserId(userId, page, size);
        return leavesPage.isEmpty() ? new ResponseEntity<Page<Leave>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<Page<Leave>>(leavesPage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLeave(@PathVariable("id") Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<List<Leave>> addLeaves(Principal principal, @Valid @RequestBody LeaveDTO leaveDTO) throws LeaveAlreadyExistsException {
        leaveDTO.setUserId(Long.valueOf(principal.getName()));
        List<Leave> leaves = leaveService.addLeaves(leaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaves);
    }
}
