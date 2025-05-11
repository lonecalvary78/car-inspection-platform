package app.carinspection.platform.car.exception;

public enum ErrorType {
    CAR_NOT_FOUND("CAR-ERR-0001", "Unable to find the car with the given id since it does not exist", 400),
    DUPLICATE_ENTRY("CAR-ERR-0002", "Unable to create the car since it already exists", 400),
    MISSING_UNIQUE_ID("CAR-ERR-0003", "Unable to create the car since the unique id is missing", 400),
    MISSING_REQUIRED_REFERENCE("CAR-ERR-0004", "Unable to create the car since the required reference is missing", 400),
    MISSING_CAR_PAYLOAD("CAR-ERR-0005", "Unable to create the car since the car payload is missing", 400),
    INTERNAL_SERVER_ERROR("CAR-ERR-0006", "An unexpected error occurred while processing the request", 500);

    private final String errorCode;
    private final String errorMessage;
    private final int statusCode;

    ErrorType(String errorCode, String errorMessage, int statusCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
