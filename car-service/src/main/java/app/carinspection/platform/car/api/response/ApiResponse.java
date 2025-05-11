package app.carinspection.platform.car.api.response;

public record ApiResponse(Object data) {
    public static ApiResponse of(Object data) {
        return new ApiResponse(data);
    }
}
