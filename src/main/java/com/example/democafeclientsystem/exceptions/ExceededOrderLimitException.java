package com.example.democafeclientsystem.exceptions;

public class ExceededOrderLimitException extends RuntimeException {
    public ExceededOrderLimitException() {
        super("User has reached the maximum order limit.");
    }
}
