package com.technogise.leavemanagement.exceptions;

public class LeaveNotFoundException extends RuntimeException {
    public LeaveNotFoundException(Long id){
        super("Leave not found with ID: " + id);
    }
}
