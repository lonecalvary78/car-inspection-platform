package app.carinspection.platform.inspection.model.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import app.carinspection.platform.inspection.model.entity.InspectionStatus;
import app.carinspection.platform.inspection.model.entity.InspectionType;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotNull;

@JsonbPropertyOrder({"id", "carId", "inspectionType", "inspectionDate", "inspectorId", "inspectionStatus", "inspectionResult", "createdAt", "updatedAt"})
public record InspectionDTO(UUID id, @NotNull UUID carId, @NotNull InspectionType inspectionType, @NotNull Instant inspectionDate, @NotNull UUID inspectorId, @NotNull InspectionStatus inspectionStatus, @NotNull Map<String, String> inspectionResult, @NotNull Instant createdAt, @NotNull Instant updatedAt, @NotNull Long version) {
    @Override
    public String toString() {
        return "InspectionDTO{" +
                "id=" + id +
                ", carId='" + carId + '\'' +
                ", inspectionType=" + inspectionType +
                ", inspectionDate=" + inspectionDate +
                ", inspectorId=" + inspectorId +
                ", inspectionStatus=" + inspectionStatus +
                ", inspectionResult=" + inspectionResult +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
}
