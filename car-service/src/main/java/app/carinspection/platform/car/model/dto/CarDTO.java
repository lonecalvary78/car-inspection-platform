package app.carinspection.platform.car.model.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CarDTO(UUID id, UUID ownerId, @NotBlank String brand, @NotBlank String model, @NotBlank String year, @NotBlank String color, @NotBlank String licensePlate, @NotBlank String vin, Instant createdAt, Instant updatedAt, Long version) {

}
