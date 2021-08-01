package me.hjhng125.taxiallocationapi.exception;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerHandler {

    @ExceptionHandler(UserGuideException.class)
    public ResponseEntity<ErrorResponse> userGuideException(UserGuideException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(ErrorResponse.builder()
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));

        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .message(errorMessage)
                .build());
    }
}
