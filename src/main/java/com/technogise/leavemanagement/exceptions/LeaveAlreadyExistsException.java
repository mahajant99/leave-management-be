package com.technogise.leavemanagement.exceptions;

import java.time.LocalDate;

public class LeaveAlreadyExistsException extends Exception{
    public LeaveAlreadyExistsException() {
        super("Leave already exists");
    }
}