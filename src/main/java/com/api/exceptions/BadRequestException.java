package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RequestException {

    public BadRequestException(){
        super(HttpStatus.BAD_REQUEST, "Email already associated with another account");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
