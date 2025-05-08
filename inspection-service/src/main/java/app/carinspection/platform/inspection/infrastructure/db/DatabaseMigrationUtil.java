package app.carinspection.platform.inspection.infrastructure.db;

import app.carinspection.platform.inspection.model.entity.Inspection;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@UtilityClass
public class DatabaseMigrationUtil {

    public void migrate() {
        try(var dynamoDbClient = DatabaseUtil.newConnection()) {
            if (!isTableExists(dynamoDbClient)) {
                createTable(dynamoDbClient);
            }
        }
    }

    public DynamoDbClient newConnection() {
        return DatabaseUtil.newConnection();
    }

    private boolean isTableExists(DynamoDbClient dynamoDbClient) {
        return dynamoDbClient.listTables().tableNames().stream().anyMatch(tableName->tableName.equals(Inspection.TABLE_NAME));
    }

    private void createTable(DynamoDbClient dynamoDbClient) {
        var inspectionTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build()
        .table(Inspection.TABLE_NAME, TableSchema.fromBean(Inspection.class));
        inspectionTable.createTable();
    }
}
