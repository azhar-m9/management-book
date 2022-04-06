package com.book.management.dto.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(HttpStatus httpStatus, Object responseObj){
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", responseObj);

        return new ResponseEntity<>(map,httpStatus);
    }
}
