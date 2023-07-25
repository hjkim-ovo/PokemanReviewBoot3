package com.pokemonreview.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(com.pokemonreview.api.exceptions.MyResourceException.class)
    public ResponseEntity<com.pokemonreview.api.exceptions.ErrorObject> handleResourceNotFoundException(
            com.pokemonreview.api.exceptions.MyResourceException ex) {
        com.pokemonreview.api.exceptions.ErrorObject errorObject = new com.pokemonreview.api.exceptions.ErrorObject();
        errorObject.setStatusCode(ex.getStatusCode());
        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());

        return new ResponseEntity<com.pokemonreview.api.exceptions.ErrorObject>(errorObject,
                HttpStatus.resolve(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> result = new HashMap<String, Object>();
        ResponseEntity<Object> ret = null;

//        String msg = "예상치 못한 문제가 발생했습니다.\n관리자에게 연락 하시기 바랍니다.";
        result.put("message", e.getMessage());
        result.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        ret = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        e.printStackTrace();

        log.error(e.getMessage(), e);

        return ret;
    }
}