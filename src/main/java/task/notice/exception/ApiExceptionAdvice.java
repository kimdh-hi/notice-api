package task.notice.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import task.notice.exception.dto.ExceptionResponse;
import task.notice.exception.exception.ExceptedException;
import task.notice.exception.exception.InvalidAuthenticationException;
import task.notice.exception.exception.OwnerMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionResponse>  unexpectedExceptionHandler(RuntimeException e) {
        printExceptionLog(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({ExceptedException.class})
    public ResponseEntity<ExceptionResponse> expectedExceptionHandler(ExceptedException e) {
        printExceptionLog(e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({InvalidAuthenticationException.class})
    public ResponseEntity<ExceptionResponse> invalidAuthenticationExceptionHandler(InvalidAuthenticationException e) {
        printExceptionLog(e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({OwnerMismatchException.class})
    public ResponseEntity<ExceptionResponse> invalidAuthenticationExceptionHandler(OwnerMismatchException e) {
        printExceptionLog(e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getMessage()));
    }

    private void printExceptionLog(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
    }
}
