package com.technogise.leavemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.repositories.LeaveRepository;

public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public Page<Leave> getLeavesByUserId(Long userId, int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "date");

        Pageable pageable = PageRequest.of(page, size, sort);

        return leaveRepository.findByUserId(userId, pageable);
    }

}
