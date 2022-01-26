package task.notice.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public InvalidTokenException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
