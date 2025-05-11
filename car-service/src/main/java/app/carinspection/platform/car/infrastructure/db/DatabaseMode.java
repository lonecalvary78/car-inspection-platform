package app.carinspection.platform.car.infrastructure.db;

import java.util.Arrays;

public enum DatabaseMode {
    LOCALSTACK("localstack"),           
    DYNAMODB("dynamodb");

    private final String mode;

    DatabaseMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static DatabaseMode fromString(String mode) {
        return Arrays.stream(DatabaseMode.values())
            .filter(m -> m.mode.equals(mode))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid database mode: " + mode));
    }
}