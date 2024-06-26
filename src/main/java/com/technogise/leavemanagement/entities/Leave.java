package com.technogise.leavemanagement.entities;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technogise.leavemanagement.enums.HalfDay;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private double duration;

    private String description;

    @Nullable
    @Enumerated(EnumType.STRING)
    private HalfDay halfDay;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean deleted = Boolean.FALSE;
}
