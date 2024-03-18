package com.technogise.leavemanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@Builder
public class LeaveDTO {

    @NotNull(message = "ID is required")
    private Long userId;

    @NotNull(message = "Start Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "Leave Type is required")
    private String leaveType;

    @NotBlank(message = "Description is required")
    private String description;

}
