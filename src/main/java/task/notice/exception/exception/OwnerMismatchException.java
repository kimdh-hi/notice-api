package task.notice.exception.exception;

public class OwnerMismatchException extends RuntimeException {

    public static final String DELETE_MISMATCH = "자신이 등록한 공지만 삭제 가능합니다.";
    public static final String UPDATE_MISMATCH = "자신이 등록한 공지만 수정 가능합니다.";

    public OwnerMismatchException(String message) {
        super(message);
    }
}
