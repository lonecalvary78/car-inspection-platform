package app.carinspection.platform.car.exception;

public class ApplicationException extends Exception {
    private final ErrorType errorType;

    public ApplicationException(ErrorType errorType, Object... args) {
        super(constructMessage(errorType));
        this.errorType = errorType;
    }
    
    private static String constructMessage(ErrorType errorType) {
        return String.format("%s - %s", errorType.getCode(), errorType.getMessage());
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
}
