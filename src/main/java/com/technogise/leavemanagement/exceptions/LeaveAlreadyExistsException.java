package com.technogise.leavemanagement.exceptions;

import java.time.LocalDate;

public class LeaveAlreadyExistsException extends Exception{
    public LeaveAlreadyExistsException(LocalDate date) {
        super("Leave exists for: " + date);
    }
}