package app.carinspection.platform.car.exception;

public class ApplicationException extends Exception {
    private final ErrorType errorType;
    public ApplicationException(ErrorType errorType) {
        super();
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
