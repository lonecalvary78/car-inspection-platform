package app.carinspection.platform.car;

import app.carinspection.platform.car.controller.CarController;
import app.carinspection.platform.car.infrastructure.db.DatabaseMigrationUtil;
import app.carinspection.platform.car.infrastructure.db.DatabaseUtil;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRules;

public class CarApplication {
    private WebServer server;
    public CarApplication() {}

    private void startUp() {
        LogConfig.configureRuntime();
        var applicationConfig = Config.create();
        server = WebServer.builder()
        .config(applicationConfig.get("server"))
        .routing(CarApplication::routing)
        .build();
        server.start();
        DatabaseMigrationUtil.migrate();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop()));
    }

    private static void routing(HttpRules rules) {
        rules.register("/cars", new CarController(DatabaseUtil.newConnection()));
    }

    public static void main(String[] args) {
        var carApplication = new CarApplication();
        carApplication.startUp();
    }
}
