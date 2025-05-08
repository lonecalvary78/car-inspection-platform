package app.carinspection.platform.inspection.model.entity;

public enum InspectionType {
    PRE_PURCHASE("PRE_PURCHASE"),
    POST_PURCHASE("POST_PURCHASE"),
    ANNUAL("ANNUAL"),
    MONTHLY("MONTHLY"),
    QUARTERLY("QUARTERLY"),
    YEARLY("YEARLY");

    private final String type;

    InspectionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
