package app.carinspection.platform.inspection.infrastructure.db;

import java.net.URI;

import io.helidon.config.Config;
import lombok.experimental.UtilityClass;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@UtilityClass
public class DatabaseUtil {
    public DynamoDbClient newConnection() {
        var config = Config.create().get("db");
        var mode = config.get("mode").asString().orElse("localstack");
        if (mode.equals("localstack")) {
            return DynamoDbClient.builder()
                .endpointOverride(URI.create(config.get("localstack.endpoint").asString().get()))
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(config.get("localstack.access-key-id").asString().get(), config.get( "localstack.secret-access-key").asString().get())
                ))
                .region(Region.of(config.get("localstack.region").asString().get()))
                .build();
        } else {
            return DynamoDbClient.create();
        }
    }
}
