package com.api.entities;

import com.api.exceptions.RequestException;

public class ErrorResponse {

    private String status;
    private String message;

    public static ErrorResponse wrap(RequestException requestException){
            return new ErrorResponse(requestException.getHttpStatus().toString(), requestException.getMessage());
    }

    private ErrorResponse(String status, String message){
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
