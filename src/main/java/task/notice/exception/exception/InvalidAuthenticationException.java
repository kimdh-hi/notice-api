package task.notice.exception.exception;

public class InvalidAuthenticationException extends RuntimeException {

    private static final String MESSAGE = "인증을 위한 정보가 올바르지 않습니다.";

    public InvalidAuthenticationException() {
        super(MESSAGE);
    }
}
