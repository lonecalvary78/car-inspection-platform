package app.carinspection.platform.inspection.model.entity;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;

@Data
@DynamoDbBean
public class Inspection {
    public static final String TABLE_NAME = "inspection";
    private static final String INDEX_INSPECTOR_ID = "ix-inspectiion-inspector";
    private static final String INDEX_INSPECTION_STATUS = "ix-inspection-status";

    @Getter(onMethod=@__(@DynamoDbPartitionKey))
    private UUID id;

    @Getter(onMethod=@__(@DynamoDbSortKey))
    private UUID carId;
    
    @Getter(onMethod=@__(@DynamoDbSecondaryPartitionKey(indexNames = {INDEX_INSPECTOR_ID, INDEX_INSPECTION_STATUS})))
    private UUID inspectorId;

    private InspectionType inspectionType;
    private Instant inspectionDate;
    

    @Getter(onMethod=@__(@DynamoDbSecondarySortKey(indexNames = {INDEX_INSPECTION_STATUS})))
    private InspectionStatus inspectionStatus;

    private Map<String, String> inspectionResult;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter(onMethod=@__(@DynamoDbVersionAttribute))
    private Long version;

    public Inspection mergeWith(Inspection amendedInspection) {
        if(amendedInspection != null) {
            var numberOfChanges = 0;
            if(amendedInspection.getInspectionResult() != null && amendedInspection.getInspectionResult() != getInspectionResult()) {
                setInspectionResult(amendedInspection.getInspectionResult());
                numberOfChanges++;
            }
            if(amendedInspection.getInspectionStatus() != null && amendedInspection.getInspectionStatus() != getInspectionStatus()) {
                setInspectionStatus(amendedInspection.getInspectionStatus());
                numberOfChanges++;
            }
            if(numberOfChanges > 0) {
                setUpdatedAt(Instant.now());
            }
        }
        return this;
        
    }
}
