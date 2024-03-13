package com.technogise.leavemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technogise.leavemanagement.entities.Leave;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

}
