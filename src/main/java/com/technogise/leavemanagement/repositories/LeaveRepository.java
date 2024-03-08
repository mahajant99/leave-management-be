package com.technogise.leavemanagement.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.technogise.leavemanagement.entities.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query("SELECT leaveRequest FROM Leave leaveRequest WHERE leaveRequest.user.id = :userId ORDER BY leaveRequest.date DESC")
    Page<Leave> findByUserId(Long userId, Pageable pageable);
}
