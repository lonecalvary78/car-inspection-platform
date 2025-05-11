package app.carinspection.platform.car.infrastructure.db;

import java.net.URI;
import java.util.logging.Logger;

import app.carinspection.platform.car.model.entity.Car;
import io.helidon.config.Config;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@UtilityClass
public class DatabaseUtil {
    private final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());
    private Config databaseConfig = Config.create().get("db");
    /**
     * To create a new db for connecting database
     * @return
     */
    public DynamoDbClient newClient() {
        var databaseMode = databaseConfig.get("mode").asString().get();
        if(DatabaseMode.fromString(databaseMode).equals(DatabaseMode.LOCALSTACK)) {
            var localstackConfig = databaseConfig.get("localstack");
            var endpoint = localstackConfig.get("endpoint").asString().get();
            var region = localstackConfig.get("region").asString().get();
            return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .region(Region.of(region))
                .build();
        }
        return DynamoDbClient.builder().build();
    }

    /**
     * To create table when it is not exist
     * @param dbClient
     */
    public void createTableWhenNotExist(DynamoDbClient dbClient) {
        var isTableExist = dbClient.listTables().tableNames().stream().anyMatch(tableName->tableName.equals(Car.TABLE_NAME));
        if(!isTableExist) {
            var carTable = DynamoDbEnhancedClient.builder().dynamoDbClient(dbClient).build().table(Car.TABLE_NAME, TableSchema.fromClass(Car.class));
            carTable.createTable();
        }    
    } 
}
