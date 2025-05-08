package app.carinspection.platform.inspection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import app.carinspection.platform.inspection.controller.InspectionController;
import app.carinspection.platform.inspection.infrastructure.db.DatabaseMigrationUtil;
import app.carinspection.platform.inspection.infrastructure.metrics.MetricsConfig;
import app.carinspection.platform.inspection.service.InspectionSchedulerService;
import io.helidon.config.Config;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import io.micrometer.core.instrument.MeterRegistry;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class InspectionApplication {
    public static void main(String[] args) {
        DatabaseMigrationUtil.migrate();

        var dynamoDbClient = DatabaseMigrationUtil.newConnection();
        createSchedulerService(dynamoDbClient, MetricsConfig.createRegistry());

        WebServer.builder()
            .config(Config.create().get("server"))
            .routing(rules -> configureRoutes(rules, dynamoDbClient))
            .build()
            .start();
    }

    private static void configureRoutes(HttpRouting.Builder rules, DynamoDbClient dynamoDbClient) {
        rules.register("/api/v1/inspections", new InspectionController(dynamoDbClient))
             .get("/metrics", (req, res) -> res.send(MetricsConfig.scrape()));
    }

    private static InspectionSchedulerService createSchedulerService(DynamoDbClient dynamoDbClient, MeterRegistry meterRegistry) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        return new InspectionSchedulerService(dynamoDbClient, scheduler, meterRegistry);
    }
}