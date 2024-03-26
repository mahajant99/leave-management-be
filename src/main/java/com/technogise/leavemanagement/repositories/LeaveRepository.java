package com.technogise.leavemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.technogise.leavemanagement.entities.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Page<Leave> findByUserIdAndDeletedFalseOrderByDateDesc(Long userId, Pageable pageable);

    Page<Leave> findByDeletedFalseOrderByDateDesc(Pageable pageable);
}
