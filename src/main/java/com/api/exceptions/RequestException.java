package com.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class RequestException extends Exception{

    private HttpStatus httpStatus;
    RequestException(HttpStatus status, String message){
        super(message);
        this.httpStatus = status;
    }

    private RequestException(HttpStatus status){
        super();
        this.httpStatus = status;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
