package app.carinspection.platform.inspection.exception;

public enum ErrorType {
    INSPECTION_NOT_FOUND("INSPECTION-ERR-00001", "Inspection not found",400),
    INSPECTION_ALREADY_EXISTS("INSPECTION-ERR-00002", "Inspection already exists",400),
    CAR_NOT_FOUND("INSPECTION-ERR-00003", "Car not found",400),
    MISSING_REQUIRED_UNIQUE_ID("INSPECTION-ERR-00004", "Missing required unique ID",400),
    MISSING_REQUIRED_INSPECTOR_ID("INSPECTION-ERR-00005", "Missing required inspector ID",400),
    MISSING_REQUIRED_PAYLOAD("INSPECTION-ERR-00006", "Missing required payload for the inspection details",400),
    VALIDATION_ERROR("INSPECTION-ERR-00007", "Validation error",400),
    INTERNAL_SERVER_ERROR("INSPECTION-ERR-00008", "Internal server error",500);

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
