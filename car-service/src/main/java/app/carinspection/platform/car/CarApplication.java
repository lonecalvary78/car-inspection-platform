package app.carinspection.platform.car;

import app.carinspection.platform.car.handler.CarResourceHandler;
import app.carinspection.platform.car.infrastructure.db.DatabaseUtil;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

public class CarApplication {
    private WebServer carServer;

    public static void main(String[] args) {
        new CarApplication();
    }

    private CarApplication() {
        LogConfig.configureRuntime();
        var applicationConfig = Config.create();
        start(applicationConfig);
    }

    private void start(Config applicationConfig) {
        this.carServer = WebServer.builder().config(applicationConfig.get("server")).routing(CarApplication::routing).build();
        this.carServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->carServer.stop()));
    }

    private static void routing(HttpRouting.Builder routingBuilder) {
        var dbClient = DatabaseUtil.newClient();
        DatabaseUtil.createTableWhenNotExist(dbClient);
        routingBuilder.register("/cars", new CarResourceHandler(dbClient));
    }
}
