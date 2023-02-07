package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class EmailUsedException extends RequestException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public EmailUsedException(){
        super(HTTP_STATUS, "Email already associated with another account");
    }

}
