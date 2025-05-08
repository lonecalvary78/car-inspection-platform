package app.carinspection.platform.inspection.api.response;

public record ApiResponse(Object data) {
    public static ApiResponse of(Object data) {
        return new ApiResponse(data);
    }
}
