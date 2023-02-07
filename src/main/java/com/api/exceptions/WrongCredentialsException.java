package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class WrongCredentialsException extends RequestException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public WrongCredentialsException(){
        super(HTTP_STATUS, "Incorrect username or password.");
    }


}
