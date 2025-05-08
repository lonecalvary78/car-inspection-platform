package app.carinspection.platform.car.exception;

public enum ErrorType {
    CAR_NOT_FOUND("CAR-ERROR-00001", "Car not found", 404),
    CAR_ALREADY_EXISTS("CAR-ERROR-00002", "Car already exists", 409),
    VALIDATION_ERROR("CAR-ERROR-00003", "Validation error", 400),
    INTERNAL_SERVER_ERROR("CAR-ERROR-00004", "Internal server error", 500),
    MISSING_REQUIRED_OWNER_ID("CAR-ERROR-00005", "Missing required owner ID", 400),
    MISSING_REQUIRED_UNIQUE_ID("CAR-ERROR-00006", "Missing required unique ID", 400),
    MISSING_REQUIRED_PAYLOAD("CAR-ERROR-00007", "Missing required payload", 400),
    FORBIDDEN("CAR-ERROR-00008", "Forbidden", 403),
    UNAUTHORIZED("CAR-ERROR-00009", "Unauthorized", 401);
    
    private final String code;
    private final String message;
    private final int statusCode;

    ErrorType(String code, String message, int statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }   

    public int getStatusCode() {
        return statusCode;
    }
}
