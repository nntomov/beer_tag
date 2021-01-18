package com.beertag.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(final String msg) {
        super(msg);
    }
}