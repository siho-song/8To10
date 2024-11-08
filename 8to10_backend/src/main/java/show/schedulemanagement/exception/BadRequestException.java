package show.schedulemanagement.exception;

public class BadRequestException extends BusinessException {

    public BadRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
