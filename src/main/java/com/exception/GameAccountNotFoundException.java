package com.exception;

public class GameAccountNotFoundException extends RuntimeException {
    public GameAccountNotFoundException(String message) {
        super(message);
    }
}