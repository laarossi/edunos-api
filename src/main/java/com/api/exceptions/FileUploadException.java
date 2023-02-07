package com.api.exceptions;

import org.springframework.http.HttpStatus;

public class FileUploadException extends RequestException{

    FileUploadException(HttpStatus status, String message) {
        super(status, message);
    }
}
