package show.schedulemanagement.exception;

public class MismatchException extends BusinessException {

    public MismatchException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
