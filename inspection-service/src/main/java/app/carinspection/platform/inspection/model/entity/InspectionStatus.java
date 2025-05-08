package app.carinspection.platform.inspection.model.entity;

public enum InspectionStatus {
    SCHEDULED("SCHEDULED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    MISSED("MISSED");

    private final String status;

    InspectionStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
