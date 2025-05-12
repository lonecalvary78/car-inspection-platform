package app.carinspection.platform.car.exception;

import java.util.Collections;
import java.util.Map;

public class ApplicationException extends Exception {
    private final ErrorType errorType;
    private final Map<String,String> validationErrors;

    public ApplicationException(ErrorType errorType) {
        super();
        this.errorType = errorType;
        this.validationErrors = Collections.emptyMap();
    }

    public ApplicationException(ErrorType errorType, Map<String, String> validationErrors) {
        super();
        this.errorType = errorType;
        this.validationErrors = validationErrors;
    }


    public ErrorType getErrorType() {
        return errorType;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
