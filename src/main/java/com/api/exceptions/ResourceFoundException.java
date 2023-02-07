package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceFoundException extends RequestException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public ResourceFoundException(){
        super(HTTP_STATUS, "Resource not found.");
    }

    public ResourceFoundException(String message) {
        super(HTTP_STATUS, message);
    }


}
