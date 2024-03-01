package com.technogise.leavemanagement.entities;

import java.time.LocalDate;

import com.technogise.leavemanagement.enums.HalfDay;

import jakarta.persistence.Entity;
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

    @Enumerated
    private HalfDay halfDay;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
