package com.technogise.leavemanagement.dtos;

public class ErrorResponse {
    @SuppressWarnings("unused")
    private int statusCode;
    @SuppressWarnings("unused")
    private String message;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }    
}
