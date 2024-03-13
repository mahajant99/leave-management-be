package com.technogise.leavemanagement.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveDTO {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String description;
}
