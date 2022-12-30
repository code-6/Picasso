package org.novinomad.picasso.aop.aspects.wrappers;

import org.novinomad.picasso.commons.exceptions.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<String> handleNotFoundException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class, BindException.class})
    public final ResponseEntity<String> handleIllegalArgumentOrStateExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<String> handleAnyException(Throwable t) {
        return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
