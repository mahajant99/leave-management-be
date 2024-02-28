package com.technogise.leavemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technogise.leavemanagement.entities.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

}
