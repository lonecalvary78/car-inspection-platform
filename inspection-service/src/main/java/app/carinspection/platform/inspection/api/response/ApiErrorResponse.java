package app.carinspection.platform.inspection.api.response;

import app.carinspection.platform.inspection.exception.ErrorType;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"errorCode", "errorMessage"})
public record ApiErrorResponse(String errorCode, String errorMessage) {

    public static ApiErrorResponse of(ErrorType errorType) {
        return new ApiErrorResponse(errorType.name(), errorType.getMessage());
    }
}
