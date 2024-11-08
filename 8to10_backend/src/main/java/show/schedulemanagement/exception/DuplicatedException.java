package show.schedulemanagement.exception;

public class DuplicatedException extends BusinessException{
    public DuplicatedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
