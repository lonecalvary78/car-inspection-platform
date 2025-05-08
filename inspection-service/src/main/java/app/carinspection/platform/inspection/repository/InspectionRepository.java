package app.carinspection.platform.inspection.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import app.carinspection.platform.inspection.model.entity.Inspection;
import app.carinspection.platform.inspection.model.entity.InspectionStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class InspectionRepository {
    private DynamoDbTable<Inspection> inspectionTable;

    public InspectionRepository(DynamoDbClient dynamoDbClient) {
        this.inspectionTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build()
        .table(Inspection.TABLE_NAME, TableSchema.fromBean(Inspection.class));
    }
   

    public List<Inspection> findAllByInspetorId(UUID inspectorId) {
        return inspectionTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByInspectorId(inspectorId)).build()).items().stream().toList();
    }

    public Optional<Inspection> findById(UUID inspectionId) {
        return Optional.ofNullable(inspectionTable.getItem(Key.builder().partitionValue(inspectionId.toString()).build()));
    }

    public Inspection save(Inspection inspection) {
        inspectionTable.putItem(inspection);
        return inspection;
    }

    public Inspection update(Inspection inspection) {
        return inspectionTable.updateItem(inspection);
    }

    public void delete(Inspection inspection) {
        inspectionTable.deleteItem(inspection);
    }

    public Long countByInspectorId(UUID inspectorId) {
        return inspectionTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByInspectorId(inspectorId)).build()).stream().count();
    }

    public Long countByCarId(UUID carId) {
        return inspectionTable.scan(ScanEnhancedRequest.builder().filterExpression(filterByCarId(carId)).build()).stream().count();
    }

    private Expression filterByInspectorId(UUID inspectorId) {
        return Expression.builder().expression("inspectorId = :inspectorId")
        .expressionValues(
            Map.of(":inspectorId", AttributeValue.fromS(inspectorId.toString()))
        ).build();
    }

    private Expression filterByCarId(UUID carId) {
        return Expression.builder().expression("carId = :carId AND inspectionStatus != :inspectionStatus")
        .expressionValues(
            Map.of(":carId", AttributeValue.fromS(carId.toString()),
            ":inspectionStatus", AttributeValue.fromS(InspectionStatus.COMPLETED.toString()))
        ).build();
    }

    public List<Inspection> findByExpression(Expression filterExpression) {
        return inspectionTable.scan(
            ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .build()
        ).items().stream().toList();
    }
}
