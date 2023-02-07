package com.api.utils;

import com.api.entities.ErrorResponse;
import com.api.exceptions.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<?> render(boolean condition){
        if(condition) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public static ResponseEntity<?> render(Object responseData){
        if(responseData != null) return new ResponseEntity<>(responseData, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    public static ResponseEntity<?> renderException(RequestException exception){
        return new ResponseEntity<>(ErrorResponse.wrap(exception), exception.getHttpStatus());
    }

    public static ResponseEntity<?> renderOrNotFound(boolean condition) {
        if(condition) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
