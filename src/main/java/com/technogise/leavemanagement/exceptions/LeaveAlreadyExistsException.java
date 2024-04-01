package com.technogise.leavemanagement.exceptions;

public class LeaveAlreadyExistsException extends Exception{
    public LeaveAlreadyExistsException() {
        super("Leave already exists");
    }
}