package show.schedulemanagement.exception;

public class NotFoundEntityException extends BusinessException {

    public NotFoundEntityException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
