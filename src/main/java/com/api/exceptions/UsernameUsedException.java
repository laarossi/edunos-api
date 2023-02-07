package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameUsedException extends RequestException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public UsernameUsedException(){
        super(HTTP_STATUS, "Username already used.");
    }

    public UsernameUsedException(String message) {
        super(HTTP_STATUS, message);
    }


}
