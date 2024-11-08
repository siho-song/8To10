package show.schedulemanagement.exception;

public class InvalidTokenException extends AuthException {

    public InvalidTokenException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
