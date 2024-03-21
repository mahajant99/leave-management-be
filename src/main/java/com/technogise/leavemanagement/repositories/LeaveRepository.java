package com.technogise.leavemanagement.repositories;

import com.technogise.leavemanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.technogise.leavemanagement.entities.Leave;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Page<Leave> findByUserIdAndDeletedFalseOrderByDateDesc(Long userId, Pageable pageable);

    Optional<Leave> findByUserAndDate(User user, LocalDate date);
}
