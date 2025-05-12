package app.carinspection.platform.car.infrastructure.health;

import app.carinspection.platform.car.infrastructure.db.DatabaseUtil;
import app.carinspection.platform.car.model.entity.Car;
import io.helidon.health.HealthCheck;
import io.helidon.health.HealthCheckResponse;
import io.helidon.health.HealthCheckResponse.Status;
import io.micrometer.common.util.StringUtils;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.TableStatus;
import io.helidon.health.HealthCheckType;

public class DatabaseHealthCheck implements HealthCheck {
    private static final String TABLE_NAME_KEY="table.name";
    private static 
    @Override
    public HealthCheckType type() {
        return io.helidon.health.HealthCheckType.READINESS;
    }

    @Override
    public HealthCheckResponse call() {
        var dbClient = DatabaseUtil.newClient();
        var databaseStatus = Status.ERROR;
        String tableName = null;
        try {
            var response = dbClient.describeTable(DescribeTableRequest.builder().tableName(Car.TABLE_NAME).build());
            if(response.table().tableStatus().equals(TableStatus.ACTIVE)) {
                databaseStatus = Status.UP;
                tableName = response.table().tableName();
            }
        } catch(Exception thrownException) {
            databaseStatus = Status.DOWN;
        }
        if(StringUtils.isNotBlank(tableName)) {
            return HealthCheckResponse.builder().status(databaseStatus).detail(TABLE_NAME_KEY, tableName).build();
        } else {
            return HealthCheckResponse.builder().status(databaseStatus).build();
        }
    }
}
