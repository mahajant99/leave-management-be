package com.technogise.leavemanagement.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }
}
