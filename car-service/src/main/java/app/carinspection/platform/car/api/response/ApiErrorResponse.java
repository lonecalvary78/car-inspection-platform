package app.carinspection.platform.car.api.response;

import app.carinspection.platform.car.exception.ErrorType;

public record ApiErrorResponse(String errorCode, String errorMessage, String validationErrors) {
    public static ApiErrorResponse of(ErrorType errorType) {
        return new ApiErrorResponse(errorType.getErrorCode(), errorType.getErrorMessage(), null);
    }
    public static ApiErrorResponse of(ErrorType errorType, String validationErrors) {
        return new ApiErrorResponse(errorType.getErrorCode(), errorType.getErrorMessage(), validationErrors);
    }
}
