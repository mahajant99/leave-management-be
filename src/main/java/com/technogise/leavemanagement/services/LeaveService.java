package com.technogise.leavemanagement.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.technogise.leavemanagement.entities.Leave;
import com.technogise.leavemanagement.repositories.LeaveRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public Page<Leave> getLeavesByUserId(Long userId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(page, size, sort);
        return leaveRepository.findByUserId(userId, pageable);
    }

    @SuppressWarnings("null")
    public void remove(Long id) {
        Leave leave = leaveRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Leave with id " + id + " not found"));
        leave.setDeleted(true);
        leaveRepository.save(leave);
    }
}
