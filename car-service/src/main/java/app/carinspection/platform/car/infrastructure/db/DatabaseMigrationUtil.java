package app.carinspection.platform.car.infrastructure.db;

import app.carinspection.platform.car.model.entity.Car;
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

    private boolean isTableExists(DynamoDbClient dynamoDbClient) {
        return dynamoDbClient.listTables().tableNames().stream().anyMatch(tableName->tableName.equals(Car.TABLE_NAME));
    }

    private void createTable(DynamoDbClient dynamoDbClient) {
        var carTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build()
        .table(Car.TABLE_NAME, TableSchema.fromBean(Car.class));
        carTable.createTable();
    }
}
