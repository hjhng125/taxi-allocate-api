package me.hjhng125.taxiallocationapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(UserGuideException.class)
    public ResponseEntity<String> userGuideException(UserGuideException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(e.getMessage());
    }
}
