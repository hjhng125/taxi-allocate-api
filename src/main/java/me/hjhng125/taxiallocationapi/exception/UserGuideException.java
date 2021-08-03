package me.hjhng125.taxiallocationapi.exception;

import org.springframework.http.HttpStatus;

public class UserGuideException extends RuntimeException{

    private HttpStatus httpStatus = HttpStatus.OK;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public UserGuideException() {
    }

    public UserGuideException(HttpStatus httpStatus, UserGuideMessage message) {
        super(message.getUserGuideMessage());
        this.httpStatus = httpStatus;
    }

    public UserGuideException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UserGuideException(UserGuideMessage message, Throwable cause) {
        super(message.getUserGuideMessage(), cause);
    }
}
